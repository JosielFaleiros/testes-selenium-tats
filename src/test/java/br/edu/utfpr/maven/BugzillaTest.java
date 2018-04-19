package br.edu.utfpr.exemplomaven;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;

/**
 *
 * @author JosielFaleiros
 */
public class BugzillaTest {

    /**
     * Vc precisa identificar onde estah o chromedriver. Baixar de:
     * https://sites.google.com/a/chromium.org/chromedriver/downloads
     *
     * Versão utilizada do chromedriver: 2.35.528139
     */
    private static String CHROMEDRIVER_LOCATION = "/home/kbig/Downloads/chromedriver_236_linux64/chromedriver";

    private static int scId = 0;

    WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_LOCATION);
    }

    @Before
    public void before() {
        ChromeOptions chromeOptions = new ChromeOptions();
        //Opcao headless para MacOS e Linux
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1200x600");
        chromeOptions.addArguments("start-maximized");

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
    }

    @After
    public void after() {
        driver.close();
    }

    @Test
    public void testLoginInvalido() {
        WebElement btnLogin = driver.findElement(By.id("login_link_top"));
        btnLogin.click();
        WebElement textLogin = driver.findElement(By.id("Bugzilla_login_top"));
        textLogin.sendKeys("usuario@email.com");
        WebElement textPassword = driver.findElement(By.xpath("//*[@id=\"Bugzilla_password_top\"]"));
        textPassword.sendKeys("senhaforte");
        WebElement btnDoLogin = driver.findElement(By.xpath("//*[@id=\"log_in_top\"]"));
        btnDoLogin.click();

        try {
            WebElement msgErro = driver.findElement(By.xpath("//*[@id=\"error_msg\"]"));
            WebDriverWait wait = new WebDriverWait(driver, 10);
            assertTrue(msgErro.getText().contains("The login or password you entered is not valid."));
        } catch (NoSuchElementException e) {
            fail();
        }
    }

    @Test
    public void testNewAccount() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement btnNewAccount = driver.findElement(By.xpath("/html/body/div[1]/div[3]/ul/li[9]/a"));
        btnNewAccount.click();
        
        WebElement textEmail = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        textEmail.sendKeys("usuario@email.com");
        textEmail.submit();

        try {
            WebElement msgResposta = driver.findElement(By.xpath("/html/body/div[2]/p"));
            assertTrue(msgResposta.getText().contains(" A confirmation email has been sent containing a link to continue creating an account. The link will expire if an account is not created within 3 days."));

            btnNewAccount.click();
            textEmail.sendKeys("usuario@email.com");
            textEmail.submit();

        } catch (NoSuchElementException e) {
            fail();
        }
    }
}
