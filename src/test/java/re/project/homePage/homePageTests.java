package re.project.homePage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import re.project.base.initDriver;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class homePageTests extends initDriver {
    WebDriver driver;

    @Before
    public void initialize() throws InterruptedException {
        this.driver = initializeDriver();
        basicLogin(driver);
    }

    @Test
    public void homePageLogoTextTest() // проверка текста Лого приложения
    {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        String logoName = driver.findElement(By.xpath("//*[contains(@class, 'css-17fvs52')]")).getText();
        assertEquals("ТЕСТ", logoName);
    }

    @Test
    public void homePageNameTextTest() // проверка сообщения при входе на стартовой странице
    {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        String logoName = driver.findElement(By.xpath("//*[contains(@class, 'css-1e185z1')]")).getText();
        assertEquals("АвтоматическоеРабочееМесто", logoName);
    }

    @After
    public void driverQuit()
    {
        driver.quit();
    }

}
