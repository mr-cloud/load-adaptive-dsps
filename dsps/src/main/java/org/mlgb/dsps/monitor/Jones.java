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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mlgb.dsps.utils.Consts;

import uni.akilis.helper.LoggerX;

/**
 * Monitor implementation module.
 * @author Leo
 *
 */
public class Jones implements NightsWatcher{
    
    public static final String TAG = Jones.class.getName();  
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    
    
    @Override
    public JSONObject getClusterSummary(String url) {
        try {
            URI uri = new URIBuilder()
                    .setScheme(Consts.STORM_UI_PROTOCAL)
                    .setHost(Consts.STORM_UI_HOST)
                    .setPath(url)
                    .build();
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse httpresponse = this.httpclient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                httpresponse.close();
                String jsonStr = EntityUtils.toString(entity);
                LoggerX.println(TAG, jsonStr);
                return (JSONObject) new JSONParser().parse(jsonStr);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
        
    }

    @Override
    public JSONObject getTopologiesSummary(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getTopologyProfile(String prefix, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getMachinesNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getMessagesNumberInQue() {
        // TODO Auto-generated method stub
        return 0;
    }

}
