package re.project.base;

import io.qameta.allure.Owner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import re.project.base.initDriver;

public class loginPage extends initDriver {

    WebDriver driver;

    @Before
    public void initialize() throws InterruptedException {
        this.driver = initializeDriver();
//        simpleLoginForTests(driver);
    }
    @After
    public void driverQuit()
    {
        driver.quit();
        driver = null; // прогуглить надо ли это делать?
    }
    @Test
    @Owner("Vinnik VS")
    public void loginTest()
    {
        driver.get("http://test-msmz-app-n1.expertek.local/ui-v3/");        //переход на сайт
        String inputUserName = "admin";
        String inputUserPass = "1";
        WebElement userName = driver.findElement(By.id("basic_username"));  //находим поле логина
        userName.sendKeys(inputUserName); //вводим логин
        WebElement userPass = driver.findElement(By.id("basic_password"));  //находим поле пароля
        userPass.sendKeys(inputUserPass); //вводим пароль
        driver.findElement(By.cssSelector("button.ant-btn.ant-btn-primary.ant-btn-block")).click(); //нажимаем кнопку войти
    }

    @Test
    @Owner("Vinnik VS")
    public void loginTest2()
    {
        driver.get("http://test-msmz-app-n1.expertek.local/ui-v3/");        //переход на сайт
        String inputUserName = "admin";
        String inputUserPass = "2";
        WebElement userName = driver.findElement(By.id("basic_username"));  //находим поле логина
        userName.sendKeys(inputUserName); //вводим логин
        WebElement userPass = driver.findElement(By.id("basic_password"));  //находим поле пароля
        userPass.sendKeys(inputUserPass); //вводим пароль
        driver.findElement(By.cssSelector("button.ant-btn.ant-btn-primary.ant-btn-block")).click(); //нажимаем кнопку войти
    }
}
