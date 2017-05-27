package org.mlgb.dsps.executor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.vo.RebalanceSuccessResultVO;
import org.mlgb.dsps.util.vo.ScalingSuccessResultVO;

import com.google.gson.Gson;

import uni.akilis.helper.LoggerX;

public class Scaler implements ScalingExecutor{
    public static final String TAG = Scaler.class.getName();  
    private CloseableHttpClient httpclient;

    
    public Scaler() {
        this.httpclient = HttpClients.createDefault();
    }

    @Override
    public void scaleOut() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.ZAC_PROTOCAL)
                    .setHost(Consts.ZAC_HOST)
                    .setPort(Consts.ZAC_PORT)
                    .setPath(Consts.ZAC_PATH_SCALE_OUT)
                    .build();
            ScalingSuccessResultVO rst = (ScalingSuccessResultVO) getParams(uri, ScalingSuccessResultVO.class);
            LoggerX.println(rst.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scaleIn() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.ZAC_PROTOCAL)
                    .setHost(Consts.ZAC_HOST)
                    .setPort(Consts.ZAC_PORT)
                    .setPath(Consts.ZAC_PATH_SCALE_IN)
                    .build();
            ScalingSuccessResultVO rst = (ScalingSuccessResultVO) getParams(uri, ScalingSuccessResultVO.class);
            LoggerX.println(rst.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void reblanceCluster(Properties props) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (props != null) {
            sb.append("\"")
                .append(Consts.REBALANCE_PARAMETER_options)
                .append("\":{");
            if (props.containsKey(Consts.REBALANCE_PARAMETER_numWorkers)) {
                sb.append("\"")
                    .append(Consts.REBALANCE_PARAMETER_numWorkers)
                    .append("\":")
                    .append(props.get(Consts.REBALANCE_PARAMETER_numWorkers));
            }
            else {
                sb.append("\"")
                    .append(Consts.REBALANCE_PARAMETER_executors)
                    .append("\":{\"")
                    .append(props.getProperty(Consts.REBALANCE_PARAMETER_bolt_id))
                    .append("\":")
                    .append(props.get(Consts.REBALANCE_PARAMETER_numExecutors))
                    .append("}");
            }
            sb.append("}");
        }
        sb.append("}");
        LoggerX.println("Rebalance options: " + sb.toString());
        // Post method to rebalance cluster.
        String path = Consts.TOPOLOGY_PROFILE_PREFIX 
                        + props.getProperty(Consts.REBALANCE_PARAMETER_id)
                        + "/rebalance"
                        + props.get(Consts.REBALANCE_PARAMETER_wait_time);
        
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPort(Consts.STORM_UI_PORT)
                    .setPath(path)
                    .build();
            RebalanceSuccessResultVO rst = (RebalanceSuccessResultVO) postParams(uri, RebalanceSuccessResultVO.class, sb.toString());
            LoggerX.println(rst.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
    
    private <T> Object postParams(URI uri, Class<T> cls, String jsonContent){
        StringEntity requestEntity = new StringEntity(
                jsonContent,
                ContentType.APPLICATION_JSON);
        try {
            HttpPost httppost = new HttpPost(uri);
            httppost.setEntity(requestEntity);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httppost);
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
}
