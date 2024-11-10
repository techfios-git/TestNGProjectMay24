package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmTest {

	WebDriver driver;
	String url;
	String userName;
	String password;

	// Element List
	By userNameField = By.xpath("//*[@id=\"user_name\"]");
	By passwordField = By.xpath("//*[@id=\"password\"]");
	By signInButtonField = By.xpath("//*[@id=\"login_submit\"]");
	By dashboardHeaderField = By.xpath("/html/body/div[1]/section/div/div[2]/div/div/header/div/strong");
	By customerMenuField = By.xpath("/html/body/div[1]/aside[1]/div/nav/ul[2]/li[2]/a/span");
	By addCustomerMenuField = By.xpath("//*[@id=\"customers\"]/li[2]/a/span");
	By addCustomerHeaderField = By
			.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By fullNameField = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By companyDropdownField = By.xpath("//select[@name='company_name']");
	By emailField = By.xpath("//*[@id=\"general_compnay\"]/div[3]/div/input");
	By phoneField = By.xpath("//*[@id=\"phone\"]");
	By countryDropdownField = By.xpath("//*[@id=\"general_compnay\"]/div[8]/div[1]/select");
	
	
	

	// Test Data //Mock Data
	String dashboardHeaderPage = "Dashboard";
	String expectedUserAlertMsg = "Please enter your user name";
	String expectedPasswordAlertMsg = "Please enter your password";
	String addCustomerHeader = "New Customer";
	String fullName = "Selenium May";
	String company = "tesla";
	String email = "abc1234@techfios.com";
	String phone = "12345678";
	String country = "Albania";

	@BeforeClass
	public void readConfig() {
		// InputStream //FileReader //BufferedReader //Scanner

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			url = prop.getProperty("url");
			System.out.println("Env selected: " + url);
			userName = prop.getProperty("user");
			password = prop.getProperty("password");

		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority=1) 
	public void testLogin() {

		driver.findElement(userNameField).sendKeys(userName);
		driver.findElement(passwordField).sendKeys(password);
		driver.findElement(signInButtonField).click();
		Assert.assertEquals(driver.findElement(dashboardHeaderField).getText(), dashboardHeaderPage,
				"Dashboard page is not found!!");
	}

	@Test(priority=2) 
	public void alertMessageTest() {

		driver.findElement(signInButtonField).click();
		String userAlertMess = driver.switchTo().alert().getText();
		Assert.assertEquals(userAlertMess, expectedUserAlertMsg, "Alert message do not match!!");
		driver.switchTo().alert().accept();

		driver.findElement(userNameField).sendKeys(userName);
		driver.findElement(signInButtonField).click();
		String passwordAlertMess = driver.switchTo().alert().getText();
		Assert.assertEquals(passwordAlertMess, expectedPasswordAlertMsg, "Alert message do not match!!");
		driver.switchTo().alert().accept();

	}

	@Test(priority=3) 
	public void addCustomerTest() {

		testLogin();
		driver.findElement(customerMenuField).click();
		driver.findElement(addCustomerMenuField).click();
		Assert.assertEquals(driver.findElement(addCustomerHeaderField).getText(), addCustomerHeader,
				"Add Customer Header do not match!!");
		
		driver.findElement(fullNameField).sendKeys(fullName + generateRandomNum(999));

		selectFromDropdown(companyDropdownField, company);

		driver.findElement(emailField).sendKeys(generateRandomNum(999) + email);

		driver.findElement(phoneField).sendKeys(phone + generateRandomNum(99));
						
		selectFromDropdown(countryDropdownField, country);

	}
	
	public void selectFromDropdown(By field, String visibleText) {
		Select sel1 = new Select(driver.findElement(field));
		sel1.selectByVisibleText(visibleText);
	}

	private int generateRandomNum(int bound) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(bound);
		return generatedNum;
	}

	

//	@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}

}
