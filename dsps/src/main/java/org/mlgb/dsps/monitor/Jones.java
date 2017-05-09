package org.mlgb.dsps.monitor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.mlgb.dsps.utils.ClusterSummaryVO;
import org.mlgb.dsps.utils.Consts;
import org.mlgb.dsps.utils.MachinesStatsVO;
import org.mlgb.dsps.utils.MessagesStatsVO;
import org.mlgb.dsps.utils.TopologiesSummaryVO;
import org.mlgb.dsps.utils.TopologyProfileVO;

import com.google.gson.Gson;

import uni.akilis.helper.LoggerX;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * Monitor implementation module.
 * @author Leo
 *
 */
public class Jones implements NightsWatcher, Callback{

    public static final String TAG = Jones.class.getName();  
    private CloseableHttpClient httpclient;
    private MessagesStatsVO messagesStats;

    public Jones() {
        super();
        this.httpclient = HttpClients.createDefault();
        this.messagesStats = new MessagesStatsVO();
    }

    class OffsetExecutor implements Watcher{
        private ZooKeeper zk;
        private ZooKeeperConnection conn;
        private Jones metricCallback;
        private String znode;
        private String data;
        
        public OffsetExecutor(String znode, Jones metricCallback) throws InterruptedException, KeeperException {
            this.znode = znode;
            this.metricCallback = metricCallback;
            this.conn = new ZooKeeperConnection();
            try {
                this.zk = conn.connect(Consts.ZOOKEEPER_HOST_PORT);
                Stat stat = znode_exists(this.znode);
                if (stat != null) {
                    byte[] b = zk.getData(this.znode, this, null);
                    data = new String(b,
                            "UTF-8");
                    LoggerX.println(data);
                    this.metricCallback.updateConsumerOffset(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } 

        }

        public  Stat znode_exists(String path) throws 
        KeeperException,InterruptedException {
            return zk.exists(path,true);
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.None) {
                switch(event.getState()) {
                case Expired:
                    LoggerX.error("offset znode doesnot exist!");
                    break;
                default:
                    break;
                }
            } else {
                try {
                    byte[] bn = zk.getData(this.znode,
                            this, null);
                    data = new String(bn,
                            "UTF-8");
                    LoggerX.println(data);
                    this.metricCallback.updateConsumerOffset(data);
                } catch(Exception ex) {
                    LoggerX.println(ex.getMessage());
                }
            }            
        }
    }

    class WhiteWalkerExecutor implements Runnable{
        private WhiteWalkerProducer msgProducer;
        private Callback metricCallback;
        private Planning plan;

        public WhiteWalkerExecutor(WhiteWalkerProducer msgProducer, Planning plan, Callback metricCallback){
            this.msgProducer = msgProducer;
            this.metricCallback = metricCallback;
            this.plan = plan;
        }
        @Override
        public void run() {
            int planLen = this.plan.delayMilis.size();
            int planIdx = 0;
            int delay = this.plan.delayMilis.get(planIdx);
            long cnt = 0;
            while(true){
                if (cnt >= this.plan.messagesPerInterval) {
                    cnt = 0;
                    planIdx += 1;
                    if (planIdx >= planLen) {
                        planIdx = 0;
                    }
                    delay = this.plan.delayMilis.get(planIdx);
                }
                this.msgProducer.produceWhiteWaker(this.metricCallback);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cnt++;
            }
        }

    }

    public void uprising(String topicName, Planning plan){
        Thread walker = new Thread(new WhiteWalkerExecutor(new WhiteWalkerProducer(topicName), plan, this));
        walker.start();
        try {
            new OffsetExecutor(Consts.OFFSET_ZNODE, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        // TODO persist the statistics in MongoDB
    }

    public void updateConsumerOffset(String data) {
        // TODO Auto-generated method stub
        
    }

    private <T> Object getParams(URI uri, Class<T> cls){
        try {
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                httpresponse.close();
                String jsonStr = EntityUtils.toString(entity);
                LoggerX.println(TAG, "json response:\n" + jsonStr);
                return new Gson().fromJson(jsonStr, cls);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ClusterSummaryVO getClusterSummary(String url) {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPort(Consts.STORM_UI_PORT)
                    .setPath(url)
                    .build();
            return (ClusterSummaryVO) getParams(uri, ClusterSummaryVO.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TopologiesSummaryVO getTopologiesSummary(String url) {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPort(Consts.STORM_UI_PORT)
                    .setPath(url)
                    .build();
            return (TopologiesSummaryVO) getParams(uri, TopologiesSummaryVO.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TopologyProfileVO getTopologyProfile(String prefix, String id) {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPort(Consts.STORM_UI_PORT)
                    .setPath(prefix + id)
                    .build();
            return (TopologyProfileVO) getParams(uri, TopologyProfileVO.class);
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MachinesStatsVO getMachinesStats() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.ZAC_PROTOCAL)
                    .setHost(Consts.ZAC_HOST)
                    .setPort(Consts.ZAC_PORT)
                    .setPath(Consts.ZAC_PATH_MACHINES_STATS)
                    .build();
            return (MachinesStatsVO) getParams(uri, MachinesStatsVO.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessagesStatsVO getMessagesStats() {
        // producer.messagesTotal, zookeeper.messagesConsumed
        return this.messagesStats;
    }
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        this.messagesStats.setMessagesTotal(this.messagesStats.getMessagesTotal() + 1);
    }

}
