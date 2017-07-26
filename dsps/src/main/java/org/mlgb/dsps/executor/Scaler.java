package org.mlgb.dsps.executor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.mlgb.dsps.util.LoggerUtil;
import org.mlgb.dsps.util.vo.RebalanceSuccessResultVO;
import org.mlgb.dsps.util.vo.ScalingSuccessResultVO;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import uni.akilis.helper.LoggerX;

public class Scaler implements ScalingExecutor{
    public static final String TAG = Scaler.class.getName();  
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private Logger myLogger = LoggerUtil.getLogger();
    
    @Override
    public boolean scaleOut() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.ZAC_PROTOCAL)
                    .setHost(Consts.ZAC_HOST)
                    .setPort(Consts.ZAC_PORT)
                    .setPath(Consts.ZAC_PATH_SCALE_OUT)
                    .build();
            ScalingSuccessResultVO rst = (ScalingSuccessResultVO) getParams(uri, ScalingSuccessResultVO.class);
            LoggerX.println(TAG, "Results:\n" + rst.toString());
            if (!"success".equalsIgnoreCase(rst.getRst())) {
                return false;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (JsonSyntaxException e) {
            LoggerX.error(TAG, "Scale out failed!");
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean scaleIn() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.ZAC_PROTOCAL)
                    .setHost(Consts.ZAC_HOST)
                    .setPort(Consts.ZAC_PORT)
                    .setPath(Consts.ZAC_PATH_SCALE_IN)
                    .build();
            ScalingSuccessResultVO rst = (ScalingSuccessResultVO) getParams(uri, ScalingSuccessResultVO.class);
            LoggerX.println(TAG, "Results:\n" + rst.toString());
            if (!"success".equalsIgnoreCase(rst.getRst())) {
                return false;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (JsonSyntaxException e) {
            LoggerX.error(TAG, "Scale in failed!");
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean rebalanceCluster(Properties prop) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (prop != null) {
            sb.append("\"")
                .append(Consts.REBALANCE_PARAMETER_options)
                .append("\":{");
            if (prop.containsKey(Consts.REBALANCE_PARAMETER_numWorkers)) {
                sb.append("\"")
                    .append(Consts.REBALANCE_PARAMETER_numWorkers)
                    .append("\":")
                    .append(prop.get(Consts.REBALANCE_PARAMETER_numWorkers));
            }
            else {
                sb.append("\"")
                    .append(Consts.REBALANCE_PARAMETER_executors)
                    .append("\":{\"")
                    .append(prop.getProperty(Consts.REBALANCE_PARAMETER_bolt_id))
                    .append("\":")
                    .append(prop.get(Consts.REBALANCE_PARAMETER_numExecutors))
                    .append("}");
            }
            sb.append("}");
        }
        sb.append("}");
        LoggerX.println(TAG, "Rebalance options: " + sb.toString());
        this.myLogger.log(Level.INFO, "Rebalance options: " + sb.toString());
        // Post method to rebalance cluster.
        String path = Consts.TOPOLOGY_PROFILE_PREFIX 
                        + prop.getProperty(Consts.REBALANCE_PARAMETER_id)
                        + "/rebalance/"
                        + prop.get(Consts.REBALANCE_PARAMETER_wait_time);
        
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPort(Consts.STORM_UI_PORT)
                    .setPath(path)
                    .build();
            RebalanceSuccessResultVO rst = (RebalanceSuccessResultVO) postParams(uri, RebalanceSuccessResultVO.class, sb.toString());
            LoggerX.println(TAG, "Results:\n" + rst.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (JsonSyntaxException e) {
            LoggerX.error(TAG, "Rebalance failed!");
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private <T> Object getParams(URI uri, Class<T> cls) throws JsonSyntaxException{
        try {
            LoggerX.println(TAG, "Request URI: " + uri.toString());
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                String jsonStr = EntityUtils.toString(entity);
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
    
    private <T> Object postParams(URI uri, Class<T> cls, String jsonContent) throws JsonSyntaxException{
        StringEntity requestEntity = new StringEntity(
                jsonContent,
                ContentType.APPLICATION_JSON);
        try {
            LoggerX.println(TAG, "Request URI: " + uri.toString());
            HttpPost httppost = new HttpPost(uri);
            httppost.setEntity(requestEntity);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httppost);
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                String jsonStr = EntityUtils.toString(entity);
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
    public boolean scaleOut(Properties prop) {
        this.myLogger.log(Level.INFO, "*** scaleOut with rebalance START ***");
        if (!this.scaleOut()) {
            this.myLogger.log(Level.WARNING, "scaleOut fail");
            this.myLogger.log(Level.INFO, "*** scaleOut with rebalance END ***");
            return false;
        }
        // Wait for Storm cluster size transformed. 
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!this.rebalanceCluster(prop)) {
            LoggerX.error(TAG, "Rebalance failed for the first time!");
            this.myLogger.log(Level.WARNING, "Rebalance fail 1st!");
            // Rebalance twice for robustness.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.rebalanceCluster(prop)) {
                this.myLogger.log(Level.WARNING, "Rebalance fail 2nd!");
                this.myLogger.log(Level.INFO, "*** scaleOut with rebalance END ***");
                return false;
            }
        }
        this.myLogger.log(Level.INFO, "*** scaleOut with rebalance END ***");
        return true;
    }

    @Override
    public boolean scaleIn(Properties prop) {
        this.myLogger.log(Level.INFO, "*** scaleIn with rebalance START ***");
        if (!this.scaleIn()) {
            this.myLogger.log(Level.WARNING, "scaleIn fail");
            this.myLogger.log(Level.INFO, "*** scaleIn with rebalance END ***");
            return false;
        }
        // Wait for Storm cluster size transformed. 
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!this.rebalanceCluster(prop)) {
            LoggerX.error(TAG, "Rebalance failed for the first time!");
            this.myLogger.log(Level.WARNING, "Rebalance fail 1st!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
            // Rebalance twice for robustness.
            if (!this.rebalanceCluster(prop)) {
                this.myLogger.log(Level.WARNING, "Rebalance fail 2nd!");
                this.myLogger.log(Level.INFO, "*** scaleIn with rebalance END ***");
                return false;
            }
        }
        this.myLogger.log(Level.INFO, "*** scaleIn with rebalance END ***");
        return true;        
    }

    @Override
    public void shutdown() {
        if (this.httpclient != null) {
            try {
                this.httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
