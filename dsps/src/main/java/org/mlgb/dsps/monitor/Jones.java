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
import org.mlgb.dsps.utils.ClusterSummaryVO;
import org.mlgb.dsps.utils.Consts;
import org.mlgb.dsps.utils.MachinesStatsVO;
import org.mlgb.dsps.utils.MessagesStatsVO;
import org.mlgb.dsps.utils.TopologiesSummaryVO;
import org.mlgb.dsps.utils.TopologyProfileVO;

import com.google.gson.Gson;

import uni.akilis.helper.LoggerX;

/**
 * Monitor implementation module.
 * @author Leo
 *
 */
public class Jones implements NightsWatcher{
    
    public static final String TAG = Jones.class.getName();  
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    
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
        // TODO Auto-generated method stub
        // producer.messagesTotal - zookeeper.messagesConsumed
        return null;
    }

}
