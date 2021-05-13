package hr.fer.progi.worldpiis.backend;
import hr.fer.progi.worldpiis.backend.model.util.TaskPriority;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class SystemTests {


  public static void main(String[] args) {

    ArrayList<String> successes =new ArrayList<>();
    System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Chrome Driver\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    driver.get("localhost:8000");

    String username = "mhren";
    String password = "loz";

    String newPhone = "091000000";
    String newMail = "novimail@gmail.com";
    successes.add(editProfileCorrectly(driver,username,password,newPhone,newMail));

    successes.add(editProfileInorrectly(driver));


    username = "epav";
    String taskName = "Testiranje sustava";
    String description = "Potrebno napisati system i unit testove.";
    TaskPriority priority = TaskPriority.HIGH;
    String deadline = "2020-02-02T17:00";
    successes.add(addTaskCorrectly(driver,username,password,taskName,description,priority,deadline));

    taskName = "Pisanje korisničke dokumentacije.";
    description = "Potrebno napisati dokumentaciju za korisnika";
    successes.add(addTaskIncorrectly(driver,taskName,description));

    description = "Potrebno dizajnirati stranicu.";
    successes.add(editTaskCorrectly(driver,description));

    successes.add(editTaskIncorrectly(driver));

    driver.quit();

    successes.forEach(success->System.out.println(success));

  }

  public static String editProfileCorrectly( WebDriver driver,String username,String password,String newPhone,String newMail) {

    login(driver,username,password);

    driver.findElement(By.id("openProfile")).click();
    driver.findElement(By.id("editProfile")).click();

    WebElement element = driver.findElement(By.id("brMob"));
    element.clear();
    element.sendKeys(newPhone);

    element = driver.findElement(By.id("eMail"));
    element.clear();
    element.sendKeys(newMail);

    driver.findElement(By.id("spremi")).click();
    boolean success = driver.findElement(By.id("mail")).getText().equals(newMail) &&
            driver.findElement(By.id("mob")).getText().equals(newPhone);

    return "editProfileCorrectly success: " + success;

  }

  public static String editProfileInorrectly(WebDriver driver) {

    driver.findElement(By.id("editProfile")).click();
    WebElement element = driver.findElement(By.id("korIme"));
    element.clear();
    element.sendKeys(null);
    driver.findElement(By.id("spremi")).click();

    boolean success;

    try {
      WebDriverWait wait = new WebDriverWait(driver, 2);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
      success = true;
    }catch(TimeoutException ex){
      success = false;
    }

    driver.findElement(By.id("zatvori")).click();
    logout(driver);
    return "editProfileIncorrectly success: " + success;

  }

  public static String addTaskCorrectly(WebDriver driver, String username, String password, String taskName, String description,
                                        TaskPriority priority, String deadline){

    login(driver,username,password);

    int numberOfTasks = driver.findElements(By.id("task")).size();
    driver.findElement(By.id("addTask")).click();

    driver.findElement(By.id("taskName")).sendKeys(taskName);
    driver.findElement(By.id("description")).sendKeys(description);
    driver.findElement(By.id("priority")).sendKeys(priority.toString());
    driver.findElement(By.id("deadline")).sendKeys(deadline);
    driver.findElement(By.id("add")).click();

    boolean success = driver.findElements(By.id("task")).size() == numberOfTasks + 1;
    return "addTaskCorrectly success: " + success;

  }

  public static String addTaskIncorrectly(WebDriver driver, String taskName, String description){

    driver.findElement(By.id("addTask")).click();

    driver.findElement(By.id("taskName")).sendKeys(taskName);
    driver.findElement(By.id("description")).sendKeys(description);
    driver.findElement(By.id("priority")).sendKeys("unknown");
    driver.findElement(By.id("deadline")).sendKeys("unknown");
    driver.findElement(By.id("add")).click();

    boolean success;

    try {
      WebDriverWait wait = new WebDriverWait(driver, 2);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
      success = true;
    }catch(TimeoutException ex){
      success = false;
    }
    return "addTaskIncorrectly success: " + success;

  }

  public static String editTaskCorrectly(WebDriver driver, String description){

    ArrayList<WebElement> editButtons = (ArrayList<WebElement>)driver.findElements(By.id("editTask"));
    for(WebElement editButton:editButtons){
      if(editButton.isEnabled()){
        editButton.click();
        break;
      }
    }

    WebElement element = driver.findElement(By.id("description"));
    element.clear();
    element.sendKeys(description);

    driver.findElement(By.id("saveTask")).click();

    boolean success;
    try {
      WebDriverWait wait = new WebDriverWait(driver, 2);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
      success = false;
    }catch(TimeoutException ex){
      success = true;
    }
    return "editTaskCorrectly success: " + success;

  }

  public static String editTaskIncorrectly(WebDriver driver){

    ArrayList<WebElement> editButtons = (ArrayList<WebElement>)driver.findElements(By.id("editTask"));
    for(WebElement editButton:editButtons){
      if(editButton.isEnabled()){
        editButton.click();
        break;
      }
    }

    WebElement element = driver.findElement(By.id("priority"));
    element.clear();

    driver.findElement(By.id("saveTask")).click();

    boolean success;
    try {
      WebDriverWait wait = new WebDriverWait(driver, 2);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
      success = true;
    }catch(TimeoutException ex){
      success = false;
    }
    return "editTaskInorrectly success: " + success;

  }

  public static void login(WebDriver driver,String username,String password){

    driver.findElement(By.id("username")).sendKeys(username);
    driver.findElement(By.id("password")).sendKeys(password);
    driver.findElement(By.id("login")).click();

  }

  public static void logout(WebDriver driver){

    driver.findElement(By.id("logout")).click();

  }

}
            
