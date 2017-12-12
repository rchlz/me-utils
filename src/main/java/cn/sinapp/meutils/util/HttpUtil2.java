/**
 * 
 */
package cn.sinapp.meutils.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2013-8-16 下午5:27:57
 */
public class HttpUtil2 {

    //根据url获取页面信息
    public static String getStringByUrl(String url, String domain, String encoding) {
        HttpClient httpClient = HttpClientPool.getInstance().getClient(encoding);
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                handleGzip(httpResponse);
                String content = IOUtils.toString(httpResponse.getEntity().getContent(), encoding);
                return content;
            }

        } catch (Exception e) {

        }
        return null;
    }

    private static void handleGzip(HttpResponse httpResponse) {
        Header ceheader = httpResponse.getEntity().getContentEncoding();
        if (ceheader != null) {
            HeaderElement[] codecs = ceheader.getElements();
            for (int i = 0; i < codecs.length; i++) {
                if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                    httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
                }
            }
        }
    }

    //极少数页面需要登录后才能查看，所以需要用户名，密码和登陆地址
    public static String getStringByUrl(String url, String domain, String encoding,
            Map<String, String> params, String loginUrl) {
        String cookie = loginAndGetCookie(params, loginUrl, encoding);
        HttpClient httpClient = HttpClientPool.getInstance().getClient(encoding);
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("cookie", cookie);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                handleGzip(httpResponse);
                String content = IOUtils.toString(httpResponse.getEntity().getContent(), encoding);
                return content;
            }

        } catch (Exception e) {

        }
        return null;

    }

    public static String loginAndGetCookie(Map<String, String> params, String loginUrl,
            String encoding) {
        CookieStore c = loginAndgetCookie(loginUrl, params, encoding);
        String cookieStr = "";
        for (Cookie cookie : c.getCookies()) {
            if (cookie.getName().equals("UP")) {
                cookieStr = cookie.getName() + "=" + cookie.getValue();
            }
        }
        return cookieStr;
    }

    public static CookieStore loginAndgetCookie(String url, Map<String, String> params,
            String encoding) {
        DefaultHttpClient client = new DefaultHttpClient();
        ByteArrayOutputStream bos = null;
        HttpPost post = new HttpPost(url);
        byte[] data = null;
        String _contentType = params.get("content-type");
        bos = new ByteArrayOutputStream();
        if (_contentType != null) {
            params.remove("content-type");
            post.setHeader("Content-Type", _contentType);
        } else {
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        String postParam = encodeParameters(params, encoding);
        try {
            data = postParam.getBytes(encoding);
            bos.write(data);
            data = bos.toByteArray();
            bos.close();
            ByteArrayEntity formEntity = new ByteArrayEntity(data);
            post.setEntity(formEntity);
            HttpContext localContext = new BasicHttpContext();
            HttpResponse response = client.execute(post, localContext);
            StatusLine status = response.getStatusLine();
            response.getAllHeaders();
            int statusCode = status.getStatusCode();
            //statusCode 小于400的基本都可以表示为成功
            if (statusCode < 400) {
                CookieStore localCookies = client.getCookieStore();
                localContext.setAttribute(ClientContext.COOKIE_STORE, localCookies);
                return localCookies;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeParameters(Map<String, String> httpParams, String encoding) {
        if (null == httpParams) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int j = 0;
        for (Entry<String, String> entry : httpParams.entrySet()) {
            String key = entry.getKey();
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(key, encoding)).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encoding));
            } catch (java.io.UnsupportedEncodingException neverHappen) {}
            j++;
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        //        System.out
        //                .println(getPicFormatByUrl("http://www.cnncw.cn/Files/xinpic/2013/06/2013061408053528316.jpg"));
    }
}
