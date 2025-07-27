package com.utility;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Browser;
import com.ui.pages.HomePage;

public class BrowserUtility {
	
	public HomePage homePage;
	Logger logger=LoggerUtility.getLogger(this.getClass());
	protected static ThreadLocal<WebDriver> driver=new ThreadLocal<WebDriver>();
	
	public WebDriver getDriver() {
		return driver.get();
	}

	public BrowserUtility(WebDriver driver) {
		super();
		this.driver.set(driver);
	}
	
	public BrowserUtility(Browser browserName) {
		logger.info("Launching browser for :"+browserName);
		if(browserName==Browser.CHROME) {
			
			driver.set(new ChromeDriver());
		}
		else if(browserName==Browser.FIREFOX) {
			driver.set(new FirefoxDriver());
		}
		else if(browserName==Browser.EDGE) {
			driver.set(new EdgeDriver());
		}
		else
		{
			logger.error("Invalid browser name");
		}
	}
	
	public BrowserUtility(Browser browserName,boolean isHeadless) {
		logger.info("Launching browser for :"+browserName);
		if(browserName==Browser.CHROME) {
			if(isHeadless)
			{
			ChromeOptions chromeOptions=new ChromeOptions();
			chromeOptions.addArguments("--headless=old");
			chromeOptions.addArguments("--window-size=1920,1080");
			driver.set(new ChromeDriver());
			}
			else
			{
				driver.set(new ChromeDriver());
			}
			
		}
		else if(browserName==Browser.FIREFOX) {
			if(isHeadless)
			{
			FirefoxOptions firefoxOptions=new FirefoxOptions();
			firefoxOptions.addArguments("--headless=old");
			firefoxOptions.addArguments("--window-size=1920,1080");
			driver.set(new FirefoxDriver());
			}
			else
			{
				driver.set(new FirefoxDriver());
			}
		}
		else if(browserName==Browser.EDGE) {
			if(isHeadless)
			{
			EdgeOptions edgeOptions=new EdgeOptions();
			edgeOptions.addArguments("--headless=old");
			edgeOptions.addArguments("--window-size=1920,1080");
			driver.set(new EdgeDriver());
			}
			else
			{
				driver.set(new EdgeDriver());
			}
		}
		else
		{
			logger.error("Invalid browser name");
		}
	}
	public BrowserUtility(String browserName) {
		logger.info("Launching browser for :"+browserName);
		if(browserName.equalsIgnoreCase("chrome")) {
			
			driver.set(new ChromeDriver());
		}
		else if(browserName.equalsIgnoreCase("firefox")) {
			driver.set(new FirefoxDriver());
		}
		else if(browserName.equalsIgnoreCase("edge")) {
			driver.set(new EdgeDriver());
		}
		else
		{
			logger.error("Invalid browser name");
		}
	}
	public void goToWebsite(String url) {
		driver.get().get(url);
	}
	
	public void maximizeWindow()
	{
		driver.get().manage().window().maximize();
	}
	
	public void clickOnWebElement(By locator) {
		WebElement element=waitForVisibilityOfElementLocated(locator);
		element.click();
	}
	
	public static WebDriverWait initializeWebDriverWait()
	{
		WebDriverWait wait=new WebDriverWait(driver.get(),Duration.ofSeconds(10));
		return wait;
		
	}
	public WebElement waitForVisibilityOfElementLocated(By locator)
	{
		return initializeWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public List<WebElement> waitForVisibilityOfAllElementsLocated(By locator){
		return initializeWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	
	public void enterText(By locator,String textToEnter) {
		WebElement element=waitForVisibilityOfElementLocated(locator);
		element.sendKeys(textToEnter);
	}
	
	public List<WebElement> waitForNumberOfElementsToBeMoreThan(By locator,Integer number) {
		return initializeWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
	}
	
	public void clickOnElementFromListOfElements(List<WebElement> elementList,String value) {
		elementList.stream().filter(element -> element.getText().equalsIgnoreCase(value))
		.findFirst().ifPresent(element->element.click());
	}
	
	public void waitForElementToBeInvisible(By locator)
	{
		initializeWebDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}
	public WebElement waitForElementToBeClickable(By locator) {
		return initializeWebDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public WebElement waitForElementToBeClickable(WebElement element) {
		return initializeWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	public boolean waitforTextToBePresentInElement(By locator, String elementText) {
		return initializeWebDriverWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, elementText));
	}
	
	public String getVisibleText(By locator) {
		WebElement element=driver.get().findElement(locator);
		return element.getText();
	}
	
	public void clickOnElementUsingActions(WebElement element) {
		Actions action = new Actions(driver.get());
		action.moveToElement(element).click(element).build().perform();
	}
	
	public String takeScreenshot(String fileName) {
		TakesScreenshot screenshot=(TakesScreenshot)driver.get();
		File screenshotData=screenshot.getScreenshotAs(OutputType.FILE);
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("HH-mm-ss");
		String timeStamp=dateFormat.format(date);
		String path=System.getProperty("user.dir")+"\\screenshots\\"+fileName+" - "+timeStamp+".png";
		File screenshotFile=new File(path);
		try {
			FileUtils.copyDirectory(screenshotFile, screenshotFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
	
	public void quitSession()
	{
		driver.get().quit();
	}
}
