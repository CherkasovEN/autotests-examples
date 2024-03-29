package re.project.kp.kpList.kp01;

import io.qameta.allure.Owner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import re.project.base.initDriver;

import java.util.List;

import static org.junit.Assert.assertEquals;
public class Kp01InterfaceTests extends initDriver {
    WebDriver driver;
    public void goToKp01() //проверка перехода к необходимой экранной форме через выпадающее меню
    {
        driver.findElement(By.xpath("//*[contains(text(), 'КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем на кнопку "КП" в боковом меню
        driver.findElement(By.xpath("//*[contains(text(), 'Список КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем кнопку "Список КП"
        List<WebElement> founderKP2; // с помощью массива мы сможем искать кнопку КП-01 в цикле, так как если элемент не будет найден на текущей странице через массив не будет выкинуто исключение
        WebElement nextButton; // объявляем переменную для кнопки "Вперед" что бы переключаться на следующую страницу
        for (int i = 0; i < 20; i++) // на всякий случай ограничу цикл 20ю итерациями
        {
            founderKP2 = driver.findElements(By.xpath(".//span[contains(text(), 'КП-01')]")); //ищем кнопку КП-01
            if(founderKP2.size() > 0) // если нашли кнопку кп-01, то размер массива будет больше 0, поэтому нажимаем на первый элемент и выходим из цикла
            {
                founderKP2.get(0).click();
                break;
            }
            nextButton = driver.findElement(By.xpath(".//li[contains(@title, 'Вперед')]/button")); // сюда попадем если не нашли кнопку кп-01 на текущей странице, ищем кнопку "Вперед"
            if(!nextButton.isEnabled()) // Если мы пролистали до конца и ничего не нашли, то фейлим переход на КП-01. метод isEnable вернет true если кнопка доступна и false если кнопка не доступна, поэтому ставить отрицательное условия (!) - в начале и если кнопка не доступна, то мы пролистали до последней страницы
                assertEquals("Не найдена кнопка КП-01", "в Списке КП");
            nextButton.click(); // нажимаем кнопку вперед
        }
    }
    @Before
    public void initialize() throws InterruptedException
    {
        this.driver = initializeDriver();
        basicLogin(driver);
        goToKp01();
    }
    @After
    public void driverQuit()
    {
        driver.quit();
        driver = null;
    }

    @Test
    @Owner("Cherkasov EN")
    public void T_722_tooltips_AZK_not_in_SPECA() //проверка всплывающей подсказки и корректности текста, содержащегося в ней
    {
        Actions action = new Actions (driver);
        WebElement azkTooltips = driver.findElement(By.xpath("/html/body/div[1]/section/section/section/main/main/div/div[2]/div/div[1]/div/form/div[1]/div[1]/div/div/div[1]/div[2]/div/div/span")); //поиск значка всплывающей подсказки
        action.clickAndHold().moveToElement(azkTooltips); //клик с удержанием на значке всплывающей подсказки
        action.moveToElement(azkTooltips).build().perform();
        WebElement tooltips = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/div"));
        String azkTooltipsText = tooltips.getAttribute("innerText");//получение текста с всплывающей подсказки
        // перемещаемся к другому элемент, что бы скрыть всплывающую подсказку
        WebElement AZKListField = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]"));
        action.moveToElement(AZKListField).perform();
        // получаем значение аттрибута pointer-events
        String getHideAttributeTooltips = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/div")).getCssValue("pointer-events");
        // мы ожидаем, что убрав мышку с всплывающей подсказки, значение аттрибута будет - none, поэтому ставим отрицательное условие и если там не none, то фейлим тест
        if(!getHideAttributeTooltips.equals("none"))
            assertEquals("none", getHideAttributeTooltips);
        // проверяем текст всплывающей подсказки
        assertEquals("Список элементов участвующих в расчетах", azkTooltipsText);//сравнение текста с всплывающей подсказки с образцом
    }
    @Test
    @Owner("Cherkasov EN")
    public void T_95_breadcrumb_check() //проверка "хлебных крошек"
    {
        List<WebElement> breadcrumbs_elements = driver.findElements(By.xpath("//ol/li"));
        assertEquals("wrong breadcrumbs count", 4, breadcrumbs_elements.size()); // проверяем что найдено 4 единицы измерения, так как всего 4 звена в хлебных крошках
        for(int i =0; i < breadcrumbs_elements.size(); i++)
        {
            if(!breadcrumbs_elements.get(i).getText().equals("") && // первое звено хлебных крошек значок домика, текст при этом отсутствует, поэтому проверяем на пустоту
                    !breadcrumbs_elements.get(i).getText().equals("КП") &&
                    !breadcrumbs_elements.get(i).getText().equals("Список КП") &&
                    !breadcrumbs_elements.get(i).getText().equals("Карточка КП-01")) // проверяем, если звено хлебных крошек не из допустимых, то зайдем в это условие и выдадим ошибку
            {
                String message = "expected КП, Список КП, Карточка КП-01, but in " + (i + 1) + " element = ";
                assertEquals(message, breadcrumbs_elements.get(i).getAttribute("innerHTML"));
            }
        }
    }
}
