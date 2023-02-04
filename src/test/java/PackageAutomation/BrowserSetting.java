package PackageAutomation;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class BrowserSetting  {
    protected Browser browser;
    protected Page pageBrowser;
    protected BrowserContext BrowserContext;
    protected Boolean isTraceEnabled = false;



    @BeforeClass
    public void setUp() {
        //инициализация браузера
        browser = Playwright
                .create()
                .chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));


        BrowserContext = browser.newContext();

        //тсоздаем трейсинг

        BrowserContext.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(false));

        isTraceEnabled=true;

        //создаем новую страницу
        pageBrowser = BrowserContext.newPage();
    }


    @AfterClass
    public void tearDown() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
    }




    @AfterMethod
    public void attachFilesToFailedTest(ITestResult result) throws IOException {
        if (!result.isSuccess()) {
            String uuid = UUID.randomUUID().toString();
            byte[] screenshot = pageBrowser.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("build/allure-results/screenshot_" + uuid + "screenshot.png"))
                    .setFullPage(true));

            Allure.addAttachment(uuid, new ByteArrayInputStream(screenshot));
            Allure.addAttachment("source.html", "text/html", pageBrowser.content());

            if (isTraceEnabled) {
                String traceFileName = String.format("build/%s_trace.zip", uuid);
                Path tracePath = Paths.get(traceFileName);
                BrowserContext.tracing()
                        .stop(new Tracing.StopOptions()
                                .setPath(tracePath));
                Allure.addAttachment("trace.zip", new ByteArrayInputStream(Files.readAllBytes(tracePath)));
            }
        }
    }


}
