package com.ui.pages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Browser;
import com.utility.BrowserUtility;

public class SearchBusesPage extends BrowserUtility {

	
	private static By tupleWrapper = By.xpath("//li[contains(@class,'tupleWrapper')]");
	private static By busesNameLocator = By.xpath(".//div[contains(@class,'travelsName')]");
	private static By dateValueLocator = By.xpath("//span[contains(@class,'doj')]");
	private static By busPriceLocator = By.xpath(".//div[contains(@class,'fareWrapper')]/p");
	private static By boardTimeLocator = By
			.xpath(".//div[contains(@class,'timeRow')]/p[contains(@class,'boardingTime')]");
	private static By viewSeatsLocator = By.xpath(".//button[contains(@class,'viewSeatsBtn')]");
	private static By endOfListLocator = By.xpath("//span[contains(@class,'endText')]");
	private static By highlyRatedByWomanFilterlocator = By.xpath("//div[contains(text(),'Highly rated by women')]");
	private static By busCountLocator = By.xpath("//span[contains(@class,'subtitle')]");

	public SearchBusesPage(WebDriver driver) {
		super(driver);
	}

	public void waitForBusCount() {
		waitforTextToBePresentInElement(getDriver(), busCountLocator, "buses");
	}

	public String getBusCount() {
		waitForBusCount();
		return getVisibleText(busCountLocator);
	}

	public void searchBusForWomen(WebDriverWait wait) {
		WebElement highlyRatedForWomanFilter = null;
		highlyRatedForWomanFilter = waitForElementToBeClickable(highlyRatedByWomanFilterlocator);
		highlyRatedForWomanFilter.click();
	}

	public SearchBusesPage scrollPage(WebDriver driver,
			JavascriptExecutor js,List<WebElement> busNameList) {
		while (true) {
			busNameList=waitForVisibilityOfAllElementsLocated(driver, busesNameLocator);
			List<WebElement> endOfList = driver.findElements(endOfListLocator);
//					waitForVisibilityOfAllElementsLocated(driver, endOfListLocator);
			if (!endOfList.isEmpty()) {
				break;
			}
			js.executeScript("arguments[0].scrollIntoView({behaviour:'smooth'})",
					busNameList.get(busNameList.size() - 3));
		}
		return new SearchBusesPage(getDriver());
	}

	public void selectBusBasedOnTime(WebDriver driver,List<WebElement> busNameList, List<WebElement> busPriceList,
			List<WebElement> boardingTimeList, String busName, String boardingTime, List<WebElement> viewSeats,
			JavascriptExecutor js) {
		
		IntStream.range(0, busNameList.size())
				.filter(i -> busNameList.get(i).getText().trim().equalsIgnoreCase(busName))
				.filter(i -> boardingTimeList.get(i).getText().equals(boardingTime)).findFirst().ifPresent(i -> {
					WebElement viewSeatButton = viewSeats.get(i);
					try {
						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
								viewSeatButton);
						Thread.sleep(1000);
						waitForElementToBeClickable(driver,viewSeatButton).click();
					} catch (Exception e) {
						js.executeScript("arguments[0].click();", viewSeatButton);
					}
				});
		
	}

	public SelectSeatsPage clickOnViewSeatsButton(WebDriver driver,String busName,String boardingTime) {
		List<WebElement> busNameList = waitForVisibilityOfAllElementsLocated(driver, busesNameLocator);
		List<WebElement> busPriceList = waitForVisibilityOfAllElementsLocated(driver, busPriceLocator);
		List<WebElement> boardingTimeList = waitForVisibilityOfAllElementsLocated(driver, boardTimeLocator);
		List<WebElement> viewSeats = waitForVisibilityOfAllElementsLocated(driver, viewSeatsLocator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		scrollPage(driver, js,busNameList);
		selectBusBasedOnTime(driver,busNameList,busPriceList, boardingTimeList, busName, boardingTime, viewSeats, js);
		return new SelectSeatsPage(getDriver());
	}
	public SearchBusesPage selectFilter(WebDriver driver, List<String> filterList) {
		String busCount=getBusCount();
		System.out.println("Number of buses :"+busCount);
		WebElement filter = null;
		for (String filterValue : filterList) {
			By filterlocator = By.xpath("//div[contains(text(),'" + filterValue + "')]");
			filter = waitForElementToBeClickable(filterlocator);
			filter.click();
			waitForVisibilityOfAllElementsLocated(driver, tupleWrapper);
//			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tupleWrapper));
		}
		return new SearchBusesPage(getDriver());
	}
}
