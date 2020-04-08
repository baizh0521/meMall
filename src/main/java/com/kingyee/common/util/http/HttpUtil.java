package com.kingyee.common.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HttpUtil工具类
 *
 * @author ph
 * @version 2018-02-01
 */
public class HttpUtil {

    private static PoolingHttpClientConnectionManager manager = null;
    private static CloseableHttpClient httpClient = null;
    private static final int BUFFER_SIZE = 1024;

    private static String[] SPIDERS = {"Googlebot", "msnbot", "Baiduspider", "bingbot", "Sogou web spider",
            "Sogou inst spider", "Sogou Pic Spider", "JikeSpider", "Sosospider", "Slurp", "360Spider", "YodaoBot",
            "OutfoxBot", "fast-webcrawler", "lycos_spider", "scooter", "ia_archiver", "MJ12bot", "AhrefsBot"};

    /**
     * 判断是否是爬虫的访问请求
     */
    public static boolean isRequestFromSpider(HttpServletRequest request) {
        boolean isSpider = false;
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.trim().length() > 0) {
            userAgent = userAgent.trim().toLowerCase();
            for (String spider : SPIDERS) {
                if (userAgent.contains(spider.toLowerCase())) {
                    isSpider = true;
                    break;
                }
            }
        }
        return isSpider;
    }

    /**
     * 取得请求的IP地址
     */
    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isValidIpAddr(ip)) {
            return ip.split(",")[0];
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    /**
     * 判断IP地址是否有效
     */
    private static boolean isValidIpAddr(String ip) {
        return ip != null && !ip.isEmpty() && !ip.equalsIgnoreCase("unknown");
    }

    /**
     * 判断请求是否是Ajax
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }

    /**
     * 取得浏览的base路径
     */
    public static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath;
        if (port == 80) {
            basePath = request.getScheme() + "://"
                    + request.getServerName() + path + "/";
        } else {
            basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
        }

        return basePath;
    }

    /**
     * 取得当前请求的完全URL
     *
     * @param request
     * @return
     */
    public static String getFullUrl(HttpServletRequest request) {
        return getFullUrl(request, true);
    }

    /**
     * 取得当前请求的完全URL
     *
     * @param encode 是做urlencode
     */
    public static String getFullUrl(HttpServletRequest request, boolean encode) {
        String orginUrl = request.getRequestURL().toString();
        if (null != request.getQueryString()) {
            orginUrl += "?" + request.getQueryString();
        } else {
            Map<String, String[]> parpMap = request.getParameterMap();
            Set<String> keys = parpMap.keySet();
            String value;
            StringBuilder querys = new StringBuilder();
            for (String key : keys) {
                value = parpMap.get(key)[0];
                if (null != value && !value.isEmpty()) {
                    querys.append(key);
                    querys.append("=");
                    querys.append(value);
                    querys.append("&");
                }
            }
            if (querys.length() != 0) {
                orginUrl += "?" + querys.substring(0, querys.length() - 1);
            }
        }
        try {
            if (encode) {
                orginUrl = URLEncoder.encode(orginUrl, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orginUrl;
    }

    /**
     * 判断是否是微信浏览器的访问请求
     */
    public static boolean isWechat(HttpServletRequest request) {
        boolean isSpider = false;
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.trim().length() > 0) {
            userAgent = userAgent.trim().toLowerCase();
            if (userAgent.contains("micromessenger")) {
                isSpider = true;
            }
        }
        return isSpider;
    }


    public static void writeMsg(HttpServletResponse response, String msg) throws IOException {
        // 如果是异步请求 返回权限不足信息
        response.setContentType("text/plain;");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "inline");

        PrintWriter writer = response.getWriter();
        try (StringReader reader = new StringReader(msg)) {
            char[] buffer = new char[BUFFER_SIZE];
            int charRead;
            while ((charRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, charRead);
            }
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    public static String getMeliveByUserId(String s) {
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet(s);
            response = getHttpClient().execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                EntityUtils.consume(response.getEntity());
            } else {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 取得http并设置连接池
     *
     * @return
     */
    public static synchronized CloseableHttpClient getHttpClient(){
        if(httpClient == null){
            //注册访问协议相关的 Socket工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                    .build();
            // HttpConnection工厂:配置写请求/解析响应处理器
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory =
                    new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
                            DefaultHttpResponseParserFactory.INSTANCE);

            // 创建连接池管理器
            manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory);
            // 默认socket配置
            SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            manager.setDefaultSocketConfig(defaultSocketConfig);
            // 最大连接数
            manager.setMaxTotal(300);
            // 每路由最大连接数
            manager.setDefaultMaxPerRoute(200);
            // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
            manager.setValidateAfterInactivity(5 * 1000);

            RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(2000)
                    .setSocketTimeout(5000).setConnectionRequestTimeout(2000).build();

            // 创建httpClient
            httpClient = HttpClients.custom().setConnectionManager(manager)
                    // 连接池不是共享的
                    .setConnectionManagerShared(false)
                    // 定期回收空闲连接
                    .evictIdleConnections(60, TimeUnit.SECONDS)
                    // 定期过期空闲连接
                    .evictExpiredConnections()
                    // 连接存活时间，如果不设置，则根据长连接信息决定
                    .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    // 连接重用策略，即是否能keepAlive
                    .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                    // 长连接配置，即获取长连接生成多长时间
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                    // 重试次数，默认3次
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .build();

            // JVM重启或者停止时，关闭连接池释放掉连接（跟数据库连接池类似）
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    try{
                        httpClient.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });

        }

        return httpClient;
    }

}
