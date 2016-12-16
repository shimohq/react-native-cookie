package im.shimo.react.cookie;


import android.util.Log;
import android.webkit.CookieSyncManager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.network.ForwardingCookieHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class CookieManagerModule extends ReactContextBaseJavaModule {

    private static final String COOKIE_HEADER = "Cookie";
    private static final String VERSION_ZERO_HEADER = "Set-cookie";

    private static final String EXPIRES_FIELD = "=; Expires=Thu, 01 Jan 1970 00:00:00 GMT";
    private static final String DOMAIN_FIELD = "; Domain=";
    private static final String PATH_FIELD = "; Path=";

    private ForwardingCookieHandler mCookieHandler;

    public CookieManagerModule(ReactApplicationContext context) {
        super(context);
        mCookieHandler = new ForwardingCookieHandler(context);
    }

    public String getName() {
        return "RNCookieManager";
    }

    @ReactMethod
    public void setCookie(String url, String value, final Promise promise) throws URISyntaxException, IOException {
        setCookie(url, value);
        promise.resolve(null);
    }


    @ReactMethod
    public void getCookie(String url, Promise promise) throws URISyntaxException, IOException {
        List<String> cookieList = getCookie(url);
        promise.resolve(cookieList != null ? cookieList.get(0) : null);
    }

    @ReactMethod
    public void clearCookie(String url, final Promise promise) throws URISyntaxException, IOException {
        if (url == null) {
            mCookieHandler.clearCookies(new Callback() {
                public void invoke(Object... args) {
                    promise.resolve(null);
                }
            });
        } else {
            List<String> cookieList = getCookie(url);
            URI uri = new URI(url);
            String domainURI = uri.getScheme() + "://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort());

            if (cookieList != null) {
                String[] cookies = cookieList.get(0).split(";");

                // Expires each cookie with every possible domain and path option
                for (String cookie : cookies) {
                    String[] parts = cookie.split("=");
                    String path = "";
                    String[] subPaths = uri.getPath().split("/");
                    String name = parts[0].trim();
                    String base = name + EXPIRES_FIELD;

                    setCookie(domainURI, base);

                    if (subPaths.length == 0) {
                        subPaths = new String[]{""};
                    }

                    for (String subPath : subPaths) {
                        path += "/" + subPath;

                        String[] domains = uri.getHost().split("\\.");
                        String domain = domains[domains.length - 1];

                        for (int i = domains.length - 1; i > 0; i--) {
                            domain = domains[i - 1] + "." + domain;
                            setCookie(domainURI, base + DOMAIN_FIELD + "." + domain + PATH_FIELD + path);
                            setCookie(domainURI, base + DOMAIN_FIELD + "." + domain + PATH_FIELD + path);
                        }

                        setCookie(domainURI, base + DOMAIN_FIELD + domain + PATH_FIELD + path);

                        if (path.equals("/")) {
                            path = "";
                        }
                    }
                }
            }

            promise.resolve(null);
        }
    }

    private
    @Nullable
    List<String> getCookie(String url) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        Map<String, List<String>> cookieMap = mCookieHandler.get(uri, new HashMap<String, List<String>>());
        return cookieMap.get(COOKIE_HEADER);
    }

    private void setCookie(String url, String value) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        Map<String, List<String>> cookieMap = new HashMap<>();
        cookieMap.put(VERSION_ZERO_HEADER, Collections.singletonList(value));
        mCookieHandler.put(uri, cookieMap);
    }

}
