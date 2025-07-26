package com.utility;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Browser;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.edge.EdgeDriver;
import java.util.List;

public class BrowserUtility {
	
	private WebDriver driver;
	
	public WebDriver getDriver() {
		return driver;
	}

	public BrowserUtility(WebDriver driver) {
		super();
		this.driver=driver;
	}
	
	public BrowserUtility(Browser browserName) {
		if(browserName==Browser.CHROME) {
			driver=new ChromeDriver();
		}
		else if(browserName==Browser.FIREFOX) {
			driver=new FirefoxDriver();
		}
		else if(browserName==Browser.EDGE) {
			driver=new EdgeDriver();
		}
		else
		{
			System.err.println("Invalid browser name");
		}
	}
	public void goToWebsite(String url) {
		driver.get(url);
	}
	
	public void maximizeWindow()
	{
		driver.manage().window().maximize();
	}
	
	public void clickOnWebElement(WebDriver driver,By locator) {
		WebElement element=waitForVisibilityOfElementLocated(driver, locator);
		element.click();
	}
	
	public static WebDriverWait initializeWebDriverWait(WebDriver driver)
	{
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		return wait;
		
	}
	public WebElement waitForVisibilityOfElementLocated(WebDriver driver,By locator)
	{
		return initializeWebDriverWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public List<WebElement> waitForVisibilityOfAllElementsLocated(WebDriver driver,By locator){
		return initializeWebDriverWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	
	public void enterText(WebDriver driver,By locator,String textToEnter) {
		WebElement element=waitForVisibilityOfElementLocated(driver, locator);
		element.sendKeys(textToEnter);
	}
	
	public List<WebElement> waitForNumberOfElementsToBeMoreThan(WebDriver driver,By locator,Integer number) {
		return initializeWebDriverWait(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
	}
	
	public void clickOnElementFromListOfElements(WebDriver driver,List<WebElement> elementList,String value) {
		elementList.stream().filter(element -> element.getText().equalsIgnoreCase(value))
		.findFirst().ifPresent(element->element.click());
	}
	
	public void waitForElementToBeInvisible(WebDriver driver,By locator)
	{
		initializeWebDriverWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}
	public WebElement waitForElementToBeClickable(By locator) {
		return initializeWebDriverWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public WebElement waitForElementToBeClickable(WebDriver driver,WebElement element) {
		return initializeWebDriverWait(driver).until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	public boolean waitforTextToBePresentInElement(WebDriver driver, By locator, String elementText) {
		return initializeWebDriverWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, elementText));
	}
	
	public String getVisibleText(By locator) {
		WebElement element=driver.findElement(locator);
		return element.getText();
	}
	
	public void clickOnElementUsingActions(WebDriver driver,WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click(element).build().perform();
	}
}
