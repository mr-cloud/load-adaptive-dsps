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

import com.google.gson.Gson;

import uni.akilis.helper.LoggerX;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.bson.Document;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.dao.MetricsDAO;
import org.mlgb.dsps.util.dao.MyMongoDatabaseFactory;
import org.mlgb.dsps.util.vo.ClusterSummaryVO;
import org.mlgb.dsps.util.vo.ConsumerZnodeVO;
import org.mlgb.dsps.util.vo.MachinesStatsVO;
import org.mlgb.dsps.util.vo.MessagesStatsVO;
import org.mlgb.dsps.util.vo.TopologiesSummaryVO;
import org.mlgb.dsps.util.vo.TopologyProfileVO;
/**
 * Monitor implementation module.
 * @author Leo
 *
 */
public class Jones implements NightsWatcher, Callback{

    public static final String TAG = Jones.class.getName();  
    private CloseableHttpClient httpclient;
    private MessagesStatsVO messagesStats;
    private Thread walker;
    private OffsetExecutor guard;
    private Thread collector;


    public Jones() {
        super();
        this.httpclient = HttpClients.createDefault();
        this.messagesStats = new MessagesStatsVO();
    }
    /**
     * Thread for watching the offset of consumer on Zookeeper.
     * 
     */
    class OffsetExecutor implements Watcher{
        private ZooKeeper zk;
        private ZooKeeperConnection conn;
        private Jones metricCallback;
        private String znode;
        private String data;
        private boolean isWatching;


        public boolean isWatching() {
            return isWatching;
        }

        public void setWatching(boolean isWatching) {
            this.isWatching = isWatching;
        }

        public OffsetExecutor(String znode, Jones metricCallback) throws InterruptedException, KeeperException {
            this.znode = znode;
            this.metricCallback = metricCallback;
            this.conn = new ZooKeeperConnection();
            this.isWatching = true;
            try {
                this.zk = conn.connect(Consts.ZOOKEEPER_HOST_PORT);
                Stat stat = znode_exists(this.znode);
                if (stat != null) {
                    byte[] b;
                    if (this.isWatching) {
                        b = zk.getData(this.znode, this, null);                        
                    }
                    else{
                        b = zk.getData(this.znode, null, null);
                    }
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
                    byte[] bn;
                    if (this.isWatching) {
                        bn = zk.getData(this.znode, this, null);                        
                    }
                    else{
                        bn = zk.getData(this.znode, null, null);
                    }
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
    /**
     * Thread for producing messages into Kafka.
     *
     */
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
            while(!Thread.interrupted()){
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
            this.msgProducer.closeProducer();
        }

    }

    /**
     * Thread for collecting and persisting metrics. 
     *
     */
    class StatsCollector implements Runnable{
        private MetricsDAO metricsDAO;
        private Jones jones;
        public StatsCollector(Jones jones) {
            this.jones = jones;
            this.metricsDAO = new MetricsDAO(MyMongoDatabaseFactory.getMongoDatabase(Consts.MONGO_DB_STORM));
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                MachinesStatsVO machines = this.jones.getMachinesStats();
                synchronized(this.jones.messagesStats){
                    Document doc = new Document()
                            .append(Consts.COLLECTION_METRIC_MACHINES_TOTAL, machines.getMachinesTotal())
                            .append(Consts.COLLECTION_METRIC_MACHINES_RUNNING, machines.getMachinesRunning())
                            .append(Consts.COLLECTION_METRIC_MESSAGES_TOTAL, this.jones.messagesStats.getMessagesTotal())
                            .append(Consts.COLLECTION_METRIC_MESSAGES_RUNNING, this.jones.messagesStats.getMessagesConsumed());
                    this.metricsDAO.saveMetrics(doc); 
                }
                try {
                    Thread.sleep(Consts.METRICS_HEARTBEAT_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LoggerX.println(TAG, "Stop collecting metrics.");
        }

    }
    public void uprising(String topicName, Planning plan){
        walker = new Thread(new WhiteWalkerExecutor(new WhiteWalkerProducer(topicName), plan, this));
        walker.start();
        try {
            guard = new OffsetExecutor(Consts.OFFSET_ZNODE, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LoggerX.println(TAG, "Cancel watching.");
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        // persist the statistics into MongoDB
        collector = new Thread(new StatsCollector(this));
        collector.start();
    }

    public void updateConsumerOffset(String jsonStr) {
        ConsumerZnodeVO vo = (ConsumerZnodeVO)new Gson().fromJson(jsonStr, ConsumerZnodeVO.class);
        this.messagesStats.setMessagesConsumed(vo.getOffset() - 1);
    }

    private <T> Object getParams(URI uri, Class<T> cls){
        try {
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                LoggerX.println(TAG, "json response:\n" + jsonStr);
                httpresponse.close();
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

    public void hibernate() {
        if (this.httpclient != null) {
            try {
                this.httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.walker != null) {
            this.walker.interrupt();            
        }  
        if (this.guard != null) {
            this.guard.setWatching(false);            
        }
        if (this.collector != null) {
            this.collector.interrupt();
        }
    }

}
