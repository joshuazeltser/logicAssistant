package view;

import model.Component;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Component.Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@Ignore
public class FrontEndTests  {

    @Value("${local.server.port}")
    private int serverPort;
    private WebElement textArea1;
    private WebElement textArea2;
    private WebElement submit;
    private WebElement errorBox;
    private WebElement indentButton;
    private WebElement hintButton;
    private WebElement hintBox;
    private WebElement validityButton;
    private WebElement validityTextArea;
    private WebElement validityResultBox;
    private WebElement checkValidityButton;
    private WebElement validityOutpurBox;

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
        refreshElements();
//        refreshValidityElements();
    }

    private void refreshElements() {
        int attempts = 0;
        while  (attempts < 5) {
            try {
                textArea1 = driver.findElement(By.id("Text1"));
                textArea2 = driver.findElement(By.id("Text2"));
                submit = driver.findElement(By.id("submit"));
                errorBox = driver.findElement(By.name("result"));
                indentButton = driver.findElement(By.id("b14"));
//                resultBox = driver.findElement(By.id("resultBox"));
                hintButton = driver.findElement(By.id("hintButton"));
                hintBox = driver.findElement(By.id("hintBox"));
                validityButton = driver.findElement(By.id("validityCheck"));
            } catch (StaleElementReferenceException s) {

            }
            attempts++;
        }
    }

    private void refreshValidityElements() {
        int attempts = 0;
        while  (attempts < 5) {
            try {
                validityTextArea = driver.findElement(By.id("Text1"));
                validityResultBox = driver.findElement(By.id("resultBox"));
                checkValidityButton = driver.findElement(By.name("submit2"));
                validityOutpurBox = driver.findElement(By.id("errorBox"));
            } catch (StaleElementReferenceException s) {

            }
            attempts++;
        }
    }



    @Test
    public void simpleProofTest() throws InterruptedException {

        textArea1.sendKeys("A ^ B\nA");
        textArea2.sendKeys("GIVEN\nAnd-Elim (1)");
        submit.click();

        refreshElements();
        assertTrue(errorBox.getText().equals("Proof is Valid"));

    }

    @Test
    public void complexProofTest1() throws InterruptedException {

        textArea1.sendKeys("!(A | !A)\n" +
                "A\n" +
                "A | !A\n" +
                "FALSE\n" +
                "!A\n" +
                "A | !A\n" +
                "FALSE\n" +
                "!!(A | !A)\n" +
                "A | !A\n");
        textArea2.sendKeys("ASSUMPTION\n" +
                "ASSUMPTION\n" +
                "Or-Intro (2)\n" +
                "Not-Elim (1,3)\n" +
                "Not-Intro (2,4)\n" +
                "Or-Intro (5)\n" +
                "Not-Elim (1,6)\n" +
                "Not-Intro (1,7)\n" +
                "DoubleNot-Elim (8)");
        submit.click();

        refreshElements();

        assertTrue(errorBox.getText().equals("Proof is Valid"));
    }

    @Test
    public void complexProofTest2() throws InterruptedException {

        textArea1.sendKeys("B ^ E\n" +
                "E -> C\n" +
                "A\n" +
                "B\n" +
                "E\n" +
                "C\n" +
                "B -> C\n" +
                "A -> (B -> C)\n");
        textArea2.sendKeys("GIVEN\n" +
                "GIVEN\n" +
                "ASSUMPTION\n" +
                "ASSUMPTION\n" +
                "And-Elim (1)\n" +
                "Implies-Elim (2,5)\n" +
                "Implies-Intro (4,6)\n" +
                "Implies-Intro (3,7)\n");
        submit.click();

        refreshElements();

        assertTrue(errorBox.getText().equals("Proof is Valid"));
    }



    @Test
    public void syntaxErrorTest1() throws InterruptedException {

        textArea1.sendKeys("| !(A | B)\n" +
                "----  A\n" +
                "----  A | B\n" +
                "----  FALSE\n" +
                "!A");
        textArea2.sendKeys("GIVEN\n" +
                "ASSUMPTION\n" +
                "Or-Intro (2)\n" +
                "Not-Elim (1,3)\n" +
                "Not-Intro (2,4)\n");

        submit.click();

        refreshElements();

        System.out.println(errorBox.getText());
        assertTrue(errorBox.getText().equals("LINE 1 - Syntax Error: You cannot use ∨ operator at this " +
                "part of an expression"));
    }

    @Test
    public void syntaxErrorTest2() throws InterruptedException {

        textArea1.sendKeys("A -> (B -> C)\n" +
                "A ^ B\n" +
                "A\n" +
                "B -> (C\n" +
                "B\n" +
                "C\n" +
                "A ^ B -> C");
        textArea2.sendKeys("GIVEN\n" +
                "ASSUMPTION\n" +
                "And-Elim (2)\n" +
                "Implies-Elim (1,3)\n" +
                "And-Elim (2)\n" +
                "Implies-Elim (4,5)\n" +
                "Implies-Intro (2,6)");

        submit.click();

        refreshElements();

        assertTrue(errorBox.getText().equals("LINE 4 - Syntax Error: Mismatched brackets"));
    }

    @Test
    public void ruleErrorTest1() throws InterruptedException {

        textArea1.sendKeys("A -> (B -> C)\n" +
                "A ^ B\n" +
                "A\n" +
                "B -> C\n" +
                "B\n" +
                "C\n" +
                "A ^ B -> C");
        textArea2.sendKeys("GIVEN\n" +
                "ASSUMPTION\n" +
                "And-Elim (2)\n" +
                "Implies-Elim (1,3)\n" +
                "And-Elim (2)\n" +
                "Implies-Elim (4,2)\n" +
                "Implies-Intro (2,3)");

        submit.click();

        refreshElements();

        assertTrue(errorBox.getText().equals("LINE 6 - RULE ERROR: This reference cannot be used for Implies " +
                "Elimination\nLINE 7 - RULE ERROR: You cannot reference a line that is inside a completed box"));
    }

    @Test
    public void ruleErrorTest2() throws InterruptedException {

        textArea1.sendKeys("B ^ E\n" +
                "E -> C\n" +
                "----  A\n" +
                "--------  B\n" +
                "--------  E\n" +
                "--------  C\n" +
                "----  B -> C\n" +
                "A -> (B -> C)");
        textArea2.sendKeys("GIVEN\n" +
                "GIVEN\n" +
                "ASSUMPTION\n" +
                "ASSUMPTION\n" +
                "And-Elim (1)\n" +
                "Implies-Elim (2,5)\n" +
                "Implies-Intro (4)\n" +
                "Implies-Intro (3,7)");

        submit.click();

        refreshElements();

        assertTrue(errorBox.getText().equals("LINE 7 - RULE ERROR: Two valid lines must be referenced " +
                "to use this rule"));
    }

    @Test
    public void simpleHintTest() throws InterruptedException {

        textArea1.sendKeys("A ^ B");
        textArea2.sendKeys("GIVEN");

        textArea1.sendKeys("\nA");

        hintButton.click();
        submit.click();

        refreshElements();

        assertTrue(hintBox.getText().equals("Hint: And Elimination"));
    }



    @After
    public void shutDown() {
        driver.close();
        driver.quit();
    }
}