package cn.sinapp.meutils.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

/**
 * HttpCient
 * 
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2013-8-19 上午10:23:30
 */
public class HttpClientPool {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    public static final HttpClientPool INSTANCE = new HttpClientPool(5);

    public static HttpClientPool getInstance() {
        return INSTANCE;
    }

    private final int poolSize;

    private HttpClientPool(int poolSize) {
        this.poolSize = poolSize;
    }

    public HttpClient getClient(String encode) {
        return generateClient(encode);
    }

    private HttpClient generateClient(String encode) {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);

        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset(encode);
        paramsBean.setUseExpectContinue(false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(
                schemeRegistry);
        connectionManager.setMaxTotal(poolSize);
        connectionManager.setDefaultMaxPerRoute(100);
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, params);
        httpClient.getParams().setIntParameter("http.socket.timeout", 60000);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
        return httpClient;
    }

}
