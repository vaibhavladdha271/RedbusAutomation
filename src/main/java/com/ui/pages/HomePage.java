package com.ui.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Browser;
import static com.constants.Env.*;
import com.utility.BrowserUtility;
import static com.utility.PropertiesUtil.*;

public class HomePage extends BrowserUtility {
	
	private static By searchSuggestions = By.xpath("//div[contains(@class,'searchSuggestionWrapper')]");
	private static By fromFieldLocator = By.xpath("//div[contains(@class,'srcDestWrapper') and @role='button']");
	private static By tomorrowButtonLocator = By.xpath("(//div[contains(@class,'buttonsWrapper')]//button)[2]");
	private static By bookingForWomanLocator = By.cssSelector("input#switch");
	private static By BookingForWomanLocatorCheckbox = By.xpath("//label[contains(@class,'switchLabel')]");
	private static By searchButtonLocator = By.xpath("//button[contains(@class,'searchButtonWrapper')]");
	private static By inputTextBoxFieldLocator = By.id("srcDest");
	private static By searchCategoryLocator = By.xpath("//div[contains(@class,'searchCategory')]");
	private static By locationNameLocator = By.xpath(".//div[contains(@class,'listHeader')]");
	private static By gotItLocator = By.xpath("//div[@data-autoid='womenFunnelInfo']//button[contains(@class,'primaryButton')]");
	private static By bookingForWomanNotificationLocator = By.xpath("//div[contains(@class,'snackbarprimary')]");
	private static By dateLocator = By.xpath("//div[contains(@class,'dateInputWrapper') and @role='button']");
	private static By dateValueLocator = By.xpath("//div[contains(@class,'calendarDate')]");
	private static By datePickerLocator = By.xpath("//div[contains(@class,'datepicker')]");
	
	
	public HomePage(Browser browserName) {
		super(browserName);
		goToWebsite(getproperty(QA, "URL"));
	}
	
	public HomePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public SearchBusesPage clickOnSearchBusButton(WebDriver driver) {
		searchBuses(driver, searchButtonLocator);
		SearchBusesPage searchBus=new SearchBusesPage(getDriver());
		return searchBus;
	}
	public void searchBuses(WebDriver driver, By searchButtonLocator) {
		clickOnWebElement(driver, searchButtonLocator);
	}
	
	public HomePage clickOnBookingForWoman(WebDriver driver, By locator) {
		WebElement bookingForWoman = driver.findElement(locator);
		clickOnWebElement(driver, locator);
		waitForElementToBeInvisible(driver, bookingForWomanNotificationLocator);
		WebElement gotItButton = waitForVisibilityOfElementLocated(driver, gotItLocator);
		clickOnWebElement(driver, gotItLocator);
		return new HomePage(getDriver());
	}
	public HomePage selectLocation(WebDriver driver,String locationValue) {
		WebElement searchSuggestion = waitForVisibilityOfElementLocated(driver, searchSuggestions);
		WebElement fromInputField = waitForVisibilityOfElementLocated(driver, inputTextBoxFieldLocator);
		enterText(driver, inputTextBoxFieldLocator, locationValue);
		List<WebElement> searchSuggestionsList = waitForNumberOfElementsToBeMoreThan(driver, searchCategoryLocator,
				2);
		System.out.println(searchSuggestionsList.size());
		WebElement locationSearchList = searchSuggestionsList.get(0);
		List<WebElement> fromFieldSearchResults = locationSearchList.findElements(locationNameLocator);
		System.out.println(fromFieldSearchResults.size());
		fromFieldSearchResults.stream().filter(location -> location.getText().equalsIgnoreCase(locationValue))
				.findFirst().ifPresent(WebElement::click);
		return new HomePage(getDriver());
	}
	
	public SearchBusesPage checkBookingForWomen(WebDriver driver) {
		clickOnBookingForWoman(driver, bookingForWomanLocator);
		WebElement bookingForWomenCheckbox = driver.findElement(BookingForWomanLocatorCheckbox);
		if (bookingForWomenCheckbox.getAttribute("class").contains("checked")) {
//			searchBusForTomorrow(driver, wait, tomorrowButtonLocator);
			searchBuses(driver, searchButtonLocator);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Exception caught");
			}
		}
		SearchBusesPage searchBus=new SearchBusesPage(getDriver());
		return searchBus;
	}
	
	public HomePage selectDate(String dateValue, WebDriver driver) {
		clickOnWebElement(driver, dateLocator);
		WebElement datePicker = waitForVisibilityOfElementLocated(driver, datePickerLocator);
		List<WebElement> dateList = waitForVisibilityOfAllElementsLocated(driver, dateValueLocator);
//		dateList.stream().map(date->date.getText()).forEach(System.out::println);
		dateList.stream().filter(date -> date.getText().equals(dateValue))
				.filter(date -> date.getAttribute("class").contains("available")).findFirst()
				.ifPresent(WebElement::click);
		return new HomePage(getDriver());
	}
	
	public String verifyTomorrowDate() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, 1);
		Date tomorrow = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
		String tomorrowDate = dateFormat.format(tomorrow);
		return tomorrowDate;
	}
	
	public HomePage clickOnSourceOrDestinationField(WebDriver driver) {
		clickOnWebElement(driver, fromFieldLocator);
		return new HomePage(getDriver());
	}
}
