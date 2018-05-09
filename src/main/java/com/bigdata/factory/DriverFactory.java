package com.bigdata.factory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriverFactory {

    @Value("${phantomjs.path}")
    private  String path;


    /**
     * 生成一个模拟浏览器
     * @return
     */
    public  PhantomJSDriver getPhantomJSDriver(){
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", false);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        dcaps.setCapability("loadImages",false);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,path);
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return  driver;
    }

    // 默认超时一分钟,可以-Dhtmlunit.http.timeout=1000设置
    public static WebClient createClient(ProxyConfig proxy) {
        String timeout = System.getProperty("htmlunit.http.timeout", "40000");
        WebClient client = new WebClient(BrowserVersion.CHROME);
        WebClientOptions options = client.getOptions();
        options.setThrowExceptionOnFailingStatusCode(false);
        options.setPrintContentOnFailingStatusCode(false);
        options.setThrowExceptionOnScriptError(false);
        options.setTimeout(Integer.parseInt(timeout));
        options.setPopupBlockerEnabled(false);
        options.setGeolocationEnabled(false);
        options.setJavaScriptEnabled(false);
        options.setDownloadImages(false);
        options.setActiveXNative(false);
        options.setAppletEnabled(false);
        options.setCssEnabled(false);
        options.setHistorySizeLimit(0);
        if (proxy != null) {
            options.setProxyConfig(proxy);
        }
        return client;
    }
}
