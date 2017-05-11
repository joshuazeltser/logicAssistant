package view;


import model.Component;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;
import static java.lang.Thread.sleep;
import static org.openqa.selenium.os.CommandLine.find;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Component.Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class FrontEndTests  {

    @Value("${local.server.port}")
    private int serverPort;
    private WebElement textArea1;
    private WebElement textArea2;
    private WebElement submit;
    private WebElement errorBox;

    private String getUrl() {
        return "http://localhost:" + serverPort;
    }

    private String PROXY = "localhost:8000";

    private org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();

    private DesiredCapabilities cap = new DesiredCapabilities();

    private WebDriver driver = new ChromeDriver(cap);


    private void setUpProxy() {
        proxy.setHttpProxy(PROXY)
                .setFtpProxy(PROXY)
                .setSslProxy(PROXY);

        cap.setCapability(CapabilityType.PROXY, proxy);
        driver.get(getUrl());

    }


    @Before
    public void setup() {
        setUpProxy();
        setUpProxy();

        textArea1 = driver.findElement(By.id("Text1"));
        textArea2 = driver.findElement(By.id("Text2"));
        submit = driver.findElement(By.id("submit"));
        errorBox = driver.findElement(By.name("result"));
    }

    @Test
    public void simpleProofTest() throws InterruptedException {

        textArea1.sendKeys("A ^ B\nA");
        textArea2.sendKeys("GIVEN\nAnd-Elim (1)");
        submit.click();

        int attempts = 0;
        while  (attempts < 5) {
            try {
                errorBox = driver.findElement(By.name("result"));
            } catch (StaleElementReferenceException s) {

            }
            attempts++;
        }

        assertTrue(errorBox.getText().equals("Proof is Valid"));

    }

    @After
    public void shutDown() {
        driver.close();
        driver.quit();
    }
}