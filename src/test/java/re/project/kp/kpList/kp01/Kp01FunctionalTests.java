package re.project.kp.kpList.kp01;

import io.cucumber.java.mn.Харин;
import io.qameta.allure.Owner;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import re.project.base.initDriver;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Kp01FunctionalTests extends initDriver {
    WebDriver driver;

    public void goToKp01() {
        driver.findElement(By.xpath("//*[contains(text(), 'КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем на кнопку "КП" в боковом меню
        driver.findElement(By.xpath("//*[contains(text(), 'Список КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем кнопку "Список КП"
        List<WebElement> founderKP2; // с помощью массива мы сможем искать кнопку КП-02 в цикле, так как если элемент не будет найден на текущей странице через массив не будет выкинуто исключение
        WebElement nextButton; // объявляем переменную для кнопки "Вперед" что бы переключаться на следующую страницу
        for (int i = 0; i < 20; i++) // на всякий случай ограничу цикл 20ю итерациями
        {
            founderKP2 = driver.findElements(By.xpath(".//span[contains(text(), 'КП-01-АЗК')]")); //ищем кнопку КП-02
            if (founderKP2.size() > 0) // если нашли кнопку кп-02, то размер массива будет больше 0, поэтому нажимаем на первый элемент и выходим из цикла
            {
                founderKP2.get(0).click();
                break;
            }
            nextButton = driver.findElement(By.xpath(".//li[contains(@title, 'Вперед')]/button")); // сюда попадем если не нашли кнопку кп-02 на текущей странице, ищем кнопку "Вперед"
            if (!nextButton.isEnabled()) // Если мы пролистали до конца и ничего не нашли, то фейлим переход на КП-02. метод isEnable вернет true если кнопка доступна и false если кнопка не доступна, поэтому ставить отрицательное условия (!) - в начале и если кнопка не доступна, то мы пролистали до последней страницы
                assertEquals("Не найдена кнопка КП-01", "в Списке КП");
            nextButton.click(); // нажимаем кнопку вперед
        }
    }

    /*
    метод очистки поля input, на вход принимаем веб элемент
    и в цикле чистит по символьно

    так же добавлена дополнительная защита от бесконечного цикла
     */
    public void clearInputField(WebElement element) {
        int i = 0;
        for (; !element.getAttribute("value").equals(""); ) {
            element.sendKeys(Keys.BACK_SPACE);
            i++;
            if (i == 100) //
                break;
        }
    }

    @Before
    public void initialize() throws InterruptedException {
        this.driver = initializeDriver();
        basicLogin(driver);
        goToKp01();
    }

    @After
    public void driverQuit() {
        driver.quit();
        driver = null; // прогуглить надо ли это делать?
    }
    @Test
    @Owner("Vinnik VS")
    public void T_895_Parameter_2_CheckInputSpecialCharacter()
    {
        String specialCharacte;
        // отправляем %
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("%"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем ?
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("?"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем @
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("@"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода второго параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Test
    @Owner("Vinnik VS")
    public void T_890_Parameter_2_CheckInputLatinSymbols() {
        String cyrillicInputText;
        // отправляем одно слово
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("test"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем слово с Заглавной буквой
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("test Check"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем одну заглавную букву
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("Q"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Owner("Vinnik VS")
    @Test
    public void T_896_Parameter_2_CheckMathematicalOperationInTheParameterField()
    {
        String number;
        // отправляем "22 + 5"
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("22 + 5"); // находим поле ввода второго параметра и отправляем в него "22 + 5"
        number =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value
        assertEquals("225", number); // сравниваем полученное значение
    }
    @Test
    @Owner("Vinnik VS")
    public void T_892_Parameter_2_CheckInputNumberWithComma() {
        String numberWithComma;
        // отправляем ,
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys(","); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("", numberWithComma); // сравниваем полученное значение value с пустотой
        // отправляем ,3
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys(",3"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("3", numberWithComma); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 4,5
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("4,567"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("4567", numberWithComma); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 6,
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("6,"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("6", numberWithComma); // сравниваем полученное значение
    }
    @Test
    @Owner("Vinnik VS")
    public void T_891_Parameter_2_CheckInputNumberWithDot() {
        String numberWithPoint;
        // отправляем .
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("."); // находим поле ввода второго параметра и отправляем в него точку
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если точка не ввелась, то значение value будет пустым
        assertEquals(".", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем ..
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys(".."); // находим поле ввода второго параметра и отправляем в него две точки
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если две точки не ввелись, то значение value будет с одной точной
        assertEquals(".", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 6.84
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("6.84"); // находим поле ввода второго параметра и отправляем в него 6.84
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value
        assertEquals("6.84", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']"))); // очищаем введенные символы
    }
    @Test
    @Owner("Vinnik VS")
    public void T_889_Parameter_2_CheckInputCyrillicSymbols()
    {
        String cyrillicInputText;
        // отправляем два слова
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("тест проверка"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем слово с Заглавной буквой
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("тест Проверка"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем одну заглавную букву
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("Л"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода второго параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Owner("Vinnik VS")
    @Test
    public void T_884_Parameter_1_CheckMathematicalOperationInTheParameterField()
    {
        String number;
        // отправляем "22 + 5"
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("22 + 5"); // находим поле ввода второго параметра и отправляем в него "22 + 5"
        number =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value
        assertEquals("225", number); // сравниваем полученное значение
    }
    @Test
    @Owner("Vinnik VS")
    public void T_883_Parameter_1_CheckInputSpecialCharacter()
    {
        String specialCharacte;
        // отправляем %
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("%"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем ?
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("?"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем @
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("@"); // находим поле ввода второго параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода второго параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Test
    @Owner("Vinnik VS")
    public void T_882_Parameter_1_CheckInputLatinSymbols() {
        String cyrillicInputText;
        // отправляем одно слово
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("test"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем слово с Заглавной буквой
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("test Check"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем одну заглавную букву
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("Q"); // находим поле ввода параметра и отправляем в него латинские символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если латинские символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Test
    @Owner("Vinnik VS")
    public void T_881_Parameter_1_CheckInputCyrillicSymbols()
    {
        String cyrillicInputText;
        // отправляем два слова
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("тест проверка"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем слово с Заглавной буквой
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("тест Проверка"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // отправляем одну заглавную букву
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("Л"); // находим поле ввода второго параметра и отправляем в него кириллические символы
        cyrillicInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если кириллические символы не ввелись, то значение value будет пустым
        assertEquals("", cyrillicInputText); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода второго параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Test
    @Owner("Vinnik VS")
    public void T_872_Parameter_1_CheckInputNumberWithComma() {
        String numberWithComma;
        // отправляем ,
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys(","); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("", numberWithComma); // сравниваем полученное значение value с пустотой
        // отправляем ,3
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys(",3"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("3", numberWithComma); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 4,5
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("4,567"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("4567", numberWithComma); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 6,
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("6,"); // находим поле ввода второго параметра и отправляем в него цифры с запятой
        numberWithComma =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры с запятой не ввелись, то значение value будет пустым, либо если ввелись только цифры
        assertEquals("6", numberWithComma); // сравниваем полученное значение
    }
    @Test
    @Owner("Vinnik VS")
    public void T_871_Parameter_1_CheckInputNumberWithDot() {
        String numberWithPoint;
        // отправляем .
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("."); // находим поле ввода второго параметра и отправляем в него точку
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если точка не ввелась, то значение value будет пустым
        assertEquals(".", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем ..
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys(".."); // находим поле ввода второго параметра и отправляем в него две точки
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если две точки не ввелись, то значение value будет с одной точной
        assertEquals(".", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']"))); // очищаем введенные символы
        // отправляем 6.84
        driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).sendKeys("6.84"); // находим поле ввода второго параметра и отправляем в него 6.84
        numberWithPoint =  driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value
        assertEquals("6.84", numberWithPoint); // сравниваем полученное значение
        clearInputField(driver.findElement(By.cssSelector("input[id='singleOperationUnbalanceThreshold']"))); // очищаем введенные символы
    }
    @Test
    @Owner("Vinnik VS")
    public void T_606_CheckIfTheSelectedValuesInTheFilterFieldAreReset_FUEL() {
        String GroupNP;
        String BenzinFirst;
        String inputFieldText = "";

        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода Топливо открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        // проверяем что чек бокса Тип НП изначально деактивирован
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);

        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]/span/span")).click(); // кликаем на чек бокс Бензины АИ-95-К5
        //проверяем что чек бокс у бензина выбрался
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);
        //проверяем что в поле ввода отмечается выбранный бензин
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputFuelField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputFuelField.size(); i++)
        {
            inputFieldText = inputFuelField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины АИ-95-К5"))
                break;
        }
        assertEquals("Бензины АИ-95-К5", inputFieldText);
        // нажимаем на крестик
        driver.findElement(By.xpath("//span[contains(@class, 'ant-select-selection-item-remove')]/span[1]")).click();
        // проверяем что фильтр удалился
        inputFuelField.clear();
        inputFuelField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        assertEquals(0, inputFuelField.size());
        // проверяем что чек боксы не активны
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("false", BenzinFirst);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_599_CheckChoiceOneTypeFuelByClickingOnMarkNP()
    {
        String GroupNP;
        String BenzinFirst;
        String inputFieldText = "";

        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода Топливо открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        // проверяем что чек бокса Тип НП изначально деактивирован
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);

        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]/span/span")).click(); // кликаем на чек бокс Бензины АИ-95-К5
        //проверяем что чек бокс у бензина выбрался
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);
        //проверяем что в поле ввода отмечается выбранный бензин
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputFuelField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputFuelField.size(); i++)
        {
            inputFieldText = inputFuelField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины АИ-95-К5"))
                break;
        }
        assertEquals("Бензины АИ-95-К5", inputFieldText);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_723_CheckTheSelectionAllTMBrandsByClickingOnTMGroupWhenOneTMBrandIsSelected()
    {
        String GroupNP;
        String BenzinFirst;
        String BenzinSecond;
        String inputFieldText = "";

        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода Топливо открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        // проверяем что чек бокса Тип НП изначально деактивирован
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);

        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]/span/span")).click(); // кликаем на чек бокс Бензины АИ-95-К5
        //проверяем что чек бокс у бензина выбрался
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);
        //проверяем что в поле ввода отмечается выбранный бензин
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины АИ-95-К5"))
                break;
        }
        assertEquals("Бензины АИ-95-К5", inputFieldText);
        //Нажимаем на чек бокс группы НП - Бензины и проверяем что чек бокс выбрался у группы нп и у всех видов топлива у этой группы нп
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]/span/span")).click(); // кликаем на чек бокс группы нп
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        BenzinSecond = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-100-К5')]")).getAttribute("aria-checked");
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);
        assertEquals("true", BenzinSecond);
        assertEquals("true", GroupNP);
        // проверяем что в поле вводы отображается группа НП
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        inputAZKField.clear();
        inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины"))
                break;
        }
        assertEquals("Бензины", inputFieldText);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_721_CheckChoiceAllMarkNPByClickingOnMarkNP()
    {
        String GroupNP;
        String BenzinFirst;
        String BenzinSecond;
        String inputFieldText = "";

        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода Топливо открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        // проверяем что чек бокса Тип НП изначально деактивирован
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);

//        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]/span/span")).click(); // кликаем на чек бокс Бензины АИ-95-К5
        //проверяем что чек бокс у бензина выбрался
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);

        driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-100-К5')]/span/span")).click(); // кликаем на чек бокс Бензины АИ-100-К5
        //проверяем что чек бокс у бензина выбрался
        BenzinSecond = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-100-К5')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinSecond);

        // проверяем что чек бокса Тип НП выбран
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("true", GroupNP);


        //проверяем что в поле ввода отмечается выбранный бензин
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputFuelField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputFuelField.size(); i++)
        {
            inputFieldText = inputFuelField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины"))
                break;
        }
        assertEquals("Бензины", inputFieldText);
    }
    @Owner("Vinnik VS")
    @Test
    public void T_597_Fuel_CheckInputTextInTheField()
    {
        String text;
        // отправляем слово "тест"
        driver.findElement(By.xpath(".//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).sendKeys("тест");
        text = driver.findElement(By.xpath(".//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).getAttribute("innerText");
        assertEquals("тест", text);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_604_Fuel_CheckSelectedAllMarkNPBySelectGroupNP() {
        String GroupNP;
        String BenzinFirst;
        String BenzinSecond;
        String inputFieldText = "";
        List<WebElement> inputAZKField = new ArrayList<>();

        driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода Топливо открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Бензины')]")).click(); // открываем Группу НП
        // проверяем что чек бокса Тип НП и каждого бензина изначально деактивированы
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("false", GroupNP);
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-100-К5')]")).getAttribute("aria-checked");
        assertEquals("false", BenzinFirst);
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        assertEquals("false", BenzinFirst);

        //Нажимаем на чек бокс группы НП - Бензины и проверяем что чек бокс выбрался у группы нп и у всех видов топлива у этой группы нп
        driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]/span/span")).click(); // кликаем на чек бокс группы нп
        BenzinFirst = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-95-К5')]")).getAttribute("aria-checked");
        BenzinSecond = driver.findElement(By.xpath("//li[contains(@title, 'Бензины АИ-100-К5')]")).getAttribute("aria-checked");
        GroupNP = driver.findElement(By.xpath("//li[contains(@title, 'Бензины')]")).getAttribute("aria-checked");
        assertEquals("true", BenzinFirst);
        assertEquals("true", BenzinSecond);
        assertEquals("true", GroupNP);
        // проверяем что в поле вводы отображается группа НП
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Бензины"))
                break;
        }
        assertEquals("Бензины", inputFieldText);
    }
    @Owner("Vinnik VS")
    @Test
    public void T_747_AZKList_CheckInputTextInTheField()
    {
        String text;
        // отправляем слово "тест"
        driver.findElement(By.xpath("//*[contains(text(), 'Список А')]")).sendKeys("тест");
        text = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]")).getAttribute("innerText");
        assertEquals("тест", text);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_748_AZKList_CheckSelectAllFillingStationTanksBySelectingTanks()
    {
        driver.findElement(By.xpath("//div[1]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода список АЗК открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Регион 1-1')]")).click(); // открываем АЗС
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары
        driver.findElement(By.xpath("//*[contains(text(),'Резервуар 1-1-1-1-1')]")).click(); // кликаем на резервуар
        driver.findElement(By.xpath("//*[contains(text(),'Резервуар 1-1-1-1-2')]")).click(); // кликаем на резервуар
        // проверяем что отображается в поле ввода
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        String text = "";
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            text = inputAZKField.get(i).getAttribute("innerHTML");
            if(text.equals("АЗС 1-1-1-1"))
                break;
        }
        assertEquals("АЗС 1-1-1-1", text);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_749_CheckSelectionAllTanksBySelectingAZS()
    {
        String checkBoxStatusFirst;
        String checkBoxStatusSecond;
        String checkBoxAZS;

        driver.findElement(By.xpath("//div[1]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода список АЗК открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Регион 1-1')]")).click(); // открываем АЗС
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары

        // проверяем что изначально чек боксы резервуаров деактивированы
        checkBoxAZS = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-2')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxStatusFirst);
        assertEquals("false", checkBoxStatusSecond);
        assertEquals("false", checkBoxAZS);

        driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-1')]/span/span")).click(); // нажимаем на чек бокс АЗС

        // проверяем что чек боксы АЗС и резервуаров выбраны
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-2')]")).getAttribute("aria-checked");
        checkBoxAZS = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-1')]")).getAttribute("aria-checked");
        assertEquals("true", checkBoxStatusFirst);
        assertEquals("true", checkBoxStatusSecond);
        assertEquals("true", checkBoxAZS);

        // проверяем что отображается в поле ввода
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        String inputFieldText = "";
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("АЗС 1-1-1-1"))
                break;
        }
        assertEquals("АЗС 1-1-1-1", inputFieldText);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_751_CheckSelectOneTank()
    {
        driver.findElement(By.xpath("//div[1]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода список АЗК открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Регион 1-1')]")).click(); // открываем АЗС
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары

        driver.findElement(By.xpath("//*[contains(text(),'Резервуар 1-1-1-1-1')]")).click(); // кликаем на резервуар
        String checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked"); // берем значение чек бокса резервуара выбранного
        assertEquals("true", checkBoxStatusFirst); // проверяем что он выбран

        // проверяем что отображается в поле ввода
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        String inputFieldText = "";
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Резервуар 1-1-1-1-1"))
                break;
        }
        assertEquals("Резервуар 1-1-1-1-1", inputFieldText);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_753_CheckIfTheSelectedValuesInTheFilterFieldAreReset_AZKList() {
        String checkBoxStatusFirst;
        String checkBoxStatusSecond;
        String checkBoxAZSOne;
        String checkBoxAZSSecond;
        String checkBoxRegion;

        driver.findElement(By.xpath("//div[1]/div[2]/div/div/div/div[contains(@class,'ant-select-selector')]")).click(); // нажимаем на поле ввода список АЗК открывается выпадающий список
        driver.findElement(By.xpath("//*[contains(text(),'Регион 1-1')]")).click(); // открываем АЗС
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары
        // проверяем что чек бокса региона изначально деактивирован
        checkBoxRegion = driver.findElement(By.xpath("//li[contains(@title, 'Регион 1-1')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxRegion);

        // проверяем что изначально чек боксы резервуаров АЗС 1 деактивированы
        checkBoxAZSOne = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-2')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxStatusFirst);
        assertEquals("false", checkBoxStatusSecond);
        assertEquals("false", checkBoxAZSOne);

        // открываем резервуары второй АЗС
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-2')]")).click(); // открываем резервуары второй АЗС
        checkBoxAZSSecond = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-2')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-2')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxStatusFirst);
        assertEquals("false", checkBoxStatusSecond);
        assertEquals("false", checkBoxAZSSecond);


        driver.findElement(By.xpath("//li[contains(@title, 'Регион 1-1')]/span/span")).click(); // кликаем на чек бокс региона 1
        checkBoxRegion = driver.findElement(By.xpath("//li[contains(@title, 'Регион 1-1')]")).getAttribute("aria-checked");
        assertEquals("true", checkBoxRegion); // проверяем что он выбрался

        // проверяем что чек боксы АЗС 1 и резервуаров тоже выбрались
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары АЗС 1
        checkBoxAZSOne = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-2')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-2')]")).getAttribute("aria-checked");
        assertEquals("true", checkBoxStatusFirst);
        assertEquals("true", checkBoxStatusSecond);
        assertEquals("true", checkBoxAZSOne);

        // проверяем что чек боксы АЗС 2 и резервуаров тоже выбрались
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-2')]")).click(); // открываем резервуары второй АЗС
        checkBoxAZSSecond = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-2')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-2')]")).getAttribute("aria-checked");
        assertEquals("true", checkBoxStatusFirst);
        assertEquals("true", checkBoxStatusSecond);
        assertEquals("true", checkBoxAZSSecond);

        // проверяем что отображается в поле ввода
        // так как там массив из трех элементов то ищем по всем нужное и потом сравниваем
        List<WebElement> inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        String inputFieldText = "";
        for(int i = 0; i < inputAZKField.size(); i++)
        {
            inputFieldText = inputAZKField.get(i).getAttribute("innerHTML");
            if(inputFieldText.equals("Регион 1-1"))
                break;
        }
        assertEquals("Регион 1-1", inputFieldText);
        // нажимаем на крестик сброса
        driver.findElement(By.xpath("//span[contains(@class, 'ant-select-selection-item-remove')]/span[1]")).click();
        // проверяем что фильтр удалился
        inputAZKField.clear();
        inputAZKField = driver.findElements(By.xpath("//span[contains(@class, 'ant-select-selection-item')]/span[1]"));
        assertEquals(0, inputAZKField.size());

        // проверяем что все чек боксы деактивированы
        // проверяем что чек бокса региона деактивирован
        checkBoxRegion = driver.findElement(By.xpath("//li[contains(@title, 'Регион 1-1')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxRegion);

        // проверяем что чек боксы резервуаров АЗС 2 деактивированы
        checkBoxAZSOne = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-2')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-2-2')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxStatusFirst);
        assertEquals("false", checkBoxStatusSecond);
        assertEquals("false", checkBoxAZSOne);

        // открываем резервуары первой АЗС и проверяем что чек боксы резервуаров АЗС 2 деактивированы
        driver.findElement(By.xpath("//*[contains(text(),'АЗС 1-1-1-1')]")).click(); // открываем резервуары первой АЗС
        checkBoxAZSSecond = driver.findElement(By.xpath("//li[contains(@title, 'АЗС 1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusFirst = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-1')]")).getAttribute("aria-checked");
        checkBoxStatusSecond = driver.findElement(By.xpath("//li[contains(@title, 'Резервуар 1-1-1-1-2')]")).getAttribute("aria-checked");
        assertEquals("false", checkBoxStatusFirst);
        assertEquals("false", checkBoxStatusSecond);
        assertEquals("false", checkBoxAZSSecond);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_780_DateRange_CheckFilterCleaningWithTheButtonInTheFilterInputField() {
        // делаем объект класса эктион, так как через методы хром драйвера в календарь отправляется не корректное значение при запуске в безоконном режиме
        Actions action = new Actions(driver);
        /*
        находим поле ввода Начальной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']"))).click().sendKeys("2023-02-04").perform();
        String startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        /*
        находим поле ввода конечной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Конечная дата']"))).click().sendKeys("2023-02-10\n").perform(); // \n символ перевода на новую строку, в данном случае имитируем нажатие на энтер что бы зафиксировать введенное значение
        String endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
        // перемещаемся и кликаем на другой элемент
        WebElement AZKListField = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]"));
        action.moveToElement(AZKListField).click().perform();
        // перемещаемся к начальной дате и проверяем что введенное значение сохранилось
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        // перемещаемся к конечной дате и проверяем что введенное значение сохранилось
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);

        // находим и нажимаем на кнопку очистки
        driver.findElement(By.xpath("//span[@class='ant-picker-clear']")).click();

        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("", startDateValue);
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("", endDateValue);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_763_DateRange_CheckChangeStartDate() {
        // делаем объект класса эктион, так как через методы хром драйвера в календарь отправляется не корректное значение при запуске в безоконном режиме
        Actions action = new Actions(driver);
        /*
        находим поле ввода Начальной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']"))).click().sendKeys("2023-02-04").perform();
        String startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        /*
        находим поле ввода конечной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Конечная дата']"))).click().sendKeys("2023-02-10\n").perform(); // \n символ перевода на новую строку, в данном случае имитируем нажатие на энтер что бы зафиксировать введенное значение
        String endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
        // перемещаемся и кликаем на другой элемент
        WebElement AZKListField = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]"));
        action.moveToElement(AZKListField).click().perform();
        // перемещаемся к начальной дате и проверяем что введенное значение сохранилось
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        // перемещаемся к конечной дате и проверяем что введенное значение сохранилось
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
        // устанавливаем другую начальную дату для этого сначала удаляем начальную дату и вводим новую а после еще проверяем что конечная дата осталась
        clearInputField(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")));
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("", startDateValue);
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']"))).click().sendKeys("2023-02-05").perform();
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-05", startDateValue);
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_766_DateRange_CheckChangeEndDate() {
        // делаем объект класса эктион, так как через методы хром драйвера в календарь отправляется не корректное значение при запуске в безоконном режиме
        Actions action = new Actions(driver);
        /*
        находим поле ввода Начальной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']"))).click().sendKeys("2023-02-04").perform();
        String startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        /*
        находим поле ввода конечной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Конечная дата']"))).click().sendKeys("2023-02-10\n").perform(); // \n символ перевода на новую строку, в данном случае имитируем нажатие на энтер что бы зафиксировать введенное значение
        String endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
        // перемещаемся и кликаем на другой элемент
        WebElement AZKListField = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]"));
        action.moveToElement(AZKListField).click().perform();
        // перемещаемся к начальной дате и проверяем что введенное значение сохранилось
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        // перемещаемся к конечной дате и проверяем что введенное значение сохранилось
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-10", endDateValue);
        // устанавливаем другую конечную дату для этого сначала удаляем начальную дату и вводим новую а после еще проверяем что начальная дата осталась
        clearInputField(driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")));
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("", startDateValue);
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Конечная дата']"))).click().sendKeys("2023-02-11").perform();
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Конечная дата']")).getAttribute("value");
        assertEquals("2023-02-11", startDateValue);
        endDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", endDateValue);
    }
    @Test
    @Owner("Vinnik VS")
    public void T_768_DateRange_CheckInputOnlyStartDate()
    {
        // делаем объект класса эктион, так как через методы хром драйвера в календарь отправляется не корректное значение при запуске в безоконном режиме
        Actions action = new Actions(driver);
        /*
        находим поле ввода Начальной даты и вводим в него значение
        после чего проверяем что введенное значение отобразилось корректно
         */
        action.moveToElement(driver.findElement(By.cssSelector("input[placeholder='Начальная дата']"))).click().sendKeys("2023-02-04").perform();
        String startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("2023-02-04", startDateValue);
        // перемещаемся и кликаем на другой элемент
        WebElement AZKListField = driver.findElement(By.xpath("//*[contains(text(), 'Список А')]"));
        action.moveToElement(AZKListField).click().perform();
        // перемещаемся к конечной дате и проверяем что введенное значение удалилось
        startDateValue = driver.findElement(By.cssSelector("input[placeholder='Начальная дата']")).getAttribute("value");
        assertEquals("", startDateValue);
    }
}
