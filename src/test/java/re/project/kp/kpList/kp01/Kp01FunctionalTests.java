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

    public void goToKp01() //проверка перехода к необходимой экранной форме через выпадающее меню
    {
        driver.findElement(By.xpath("//*[contains(text(), 'КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем на кнопку "КП" в боковом меню
        driver.findElement(By.xpath("//*[contains(text(), 'Список КП') and contains(@class,'ant-menu-title-content')]")).click(); // нажимаем кнопку "Список КП"
        List<WebElement> founderKP1; // с помощью массива мы сможем искать кнопку КП-01 в цикле, так как если элемент не будет найден на текущей странице через массив не будет выкинуто исключение
        WebElement nextButton; // объявляем переменную для кнопки "Вперед" что бы переключаться на следующую страницу
        for (int i = 0; i < 20; i++) // на всякий случай ограничу цикл 20ю итерациями
        {
            founderKP1 = driver.findElements(By.xpath(".//span[contains(text(), 'КП-01')]")); //ищем кнопку КП-01
            if (founderKP1.size() > 0) // если нашли кнопку кп-01, то размер массива будет больше 0, поэтому нажимаем на первый элемент и выходим из цикла
            {
                founderKP1.get(0).click();
                break;
            }
            nextButton = driver.findElement(By.xpath(".//li[contains(@title, 'Вперед')]/button")); // сюда попадем если не нашли кнопку кп-01 на текущей странице, ищем кнопку "Вперед"
            if (!nextButton.isEnabled()) // Если мы пролистали до конца и ничего не нашли, то фейлим переход на КП-01. метод isEnable вернет true если кнопка доступна и false если кнопка не доступна, поэтому ставить отрицательное условия (!) - в начале и если кнопка не доступна, то мы пролистали до последней страницы
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
        driver = null;
    }
    @Test
    @Owner("Cherkasov EN")
    public void T_895_Parameter_1_CheckInputSpecialCharacter() //проверка ввода в поле ввода Параметра специальных символов
    {
        String specialCharacte;
        // отправляем %
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("%"); // находим поле ввода первого параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем ?
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("?"); // находим поле ввода первого параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // отправляем @
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("@"); // находим поле ввода первого параметра и отправляем в него спец символы
        specialCharacte =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если спец символы не ввелись, то значение value будет пустым
        assertEquals("", specialCharacte); // сравниваем полученное значение value с пустотой
        // для проверки попробуем отправить в поле ввода цифры
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("33"); // находим поле ввода первого параметра и отправляем в него цифры
        String numberInputText =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value, если цифры ввелись, то значение value будет равно отправленному значению
        assertEquals("33", numberInputText); // сравниваем полученное значение value с введенными цифрами
    }
    @Test
    @Owner("Cherkasov EN")
    public void T_890_Parameter_1_CheckInputLatinSymbols() //проверка ввода в поле ввода Параметра латинских букв
    {
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
    @Owner("Cherkasov EN")
    @Test
    public void T_896_Parameter_1_CheckMathematicalOperationInTheParameterField() //проверка выполнения математической операции в поле ввода Параметра
    {
        String number;
        // отправляем "22 + 5"
        driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).sendKeys("22 + 5"); // находим поле ввода второго параметра и отправляем в него "22 + 5"
        number =  driver.findElement(By.cssSelector("input[id='sumOperationUnbalanceThreshold']")).getAttribute("value"); // получаем значение атрибуту value
        assertEquals("225", number); // сравниваем полученное значение
    }
    @Test
    @Owner("Cherkasov EN")
    public void T_606_CheckIfTheSelectedValuesInTheFilterFieldAreReset_FUEL() //проверка установки и сброса чекбокса для Фильтра топлива
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
    @Owner("Cherkasov EN")
    public void T_723_CheckTheSelectionAllTMBrandsByClickingOnTMGroupWhenOneTMBrandIsSelected() //проверка установки чекбоксов в многоуровневом меню
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
    @Owner("Cherkasov EN") //проверка ввода временного периода - начальной и конечной дат
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
}
