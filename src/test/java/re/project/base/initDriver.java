package re.project.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class initDriver {
//    @Test

    protected static WebDriver driver;
//    protected static WebDriverWait wait;
    Integer countWait;

    public WebDriver initializeDriver() throws InterruptedException {
        countWait = 3;
        WebDriverManager.chromedriver().setup(); // почитать
        System.setProperty("webdriver.chrome.whitelistedIps", ""); // почитать
        ChromeOptions options=new ChromeOptions(); // это опции командной строки, полный список можно посмотреть здеся - https://peter.sh/experiments/chromium-command-line-switches/  урок 23-24
        options.setHeadless(true); // true - безоконный режим браузера, false - оконный режим браузера
        options.addArguments("--disable-dev-shm-usage"); // почитать
        options.addArguments("--no-sandbox"); // почитать
        driver = new ChromeDriver(options);
//        wait = new WebDriverWait(driver, 2);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(countWait, TimeUnit.SECONDS); // неявное ожидание
        // этот метод достаточно вызывать один раз при инициализации драйвера
        // так как этот метод дает задержку для появления элемента, то есть он ждет его появление
        // короче можно вызвать его тут один раз, что бы не вызывать в каждом тест-кейсе
        return (driver);
    }
    public void basicLogin(WebDriver driver)
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
    /*
    метод для проверки наличия элемента, в случае если элемент есть на странице, вернет true
    для поиска так же будет задействовано неявное ожидание

    если элемент найден, то размер массива найденных элементов будет больше 0

    используем именно метод findElements, а не findElement
    так как если искать только один элемент, то при его отсутствии будет выброшено исключение
     */
    public boolean isElementPresent(WebDriver driver, By locator)
    {
        return driver.findElements(locator).size() > 0;
    }
    /*
    метод для проверки отсутствия элемента, в случае если элемента нет на странице, вернет true
    перед поиском выключаем неявное ожидание,
    проводим поиск и после обратно включаем неявное ожидание

    если элемент не найден, то размер массива найденных элементов будет равен 0

    используем именно метод findElements, а не findElement
    так как если искать только один элемент, то при его отсутствии будет выброшено исключение
     */
    public boolean isElementNotPresent(WebDriver driver, By locator)
    {
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            return  driver.findElements(locator).size() == 0;
        }
        finally {
            driver.manage().timeouts().implicitlyWait(countWait, TimeUnit.SECONDS);
        }
    }
}


