package com.ui.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Browser;
import static com.constants.Env.*;
import com.utility.BrowserUtility;
import com.utility.LoggerUtility;

import static com.utility.PropertiesUtil.*;

public class HomePage extends BrowserUtility {

	Logger logger = LoggerUtility.getLogger(this.getClass());
	private static By searchSuggestions = By.xpath("//div[contains(@class,'searchSuggestionWrapper')]");
	private static By fromFieldLocator = By.xpath("//div[contains(@class,'srcDestWrapper') and @role='button']");
	private static By tomorrowButtonLocator = By.xpath("(//div[contains(@class,'buttonsWrapper')]//button)[2]");
	private static By bookingForWomanLocator = By.cssSelector("input#switch");
	private static By BookingForWomanLocatorCheckbox = By.xpath("//label[contains(@class,'switchLabel')]");
	private static By searchButtonLocator = By.xpath("//button[contains(@class,'searchButtonWrapper')]");
	private static By inputTextBoxFieldLocator = By.id("srcDest");
	private static By searchCategoryLocator = By.xpath("//div[contains(@class,'searchCategory')]");
	private static By locationNameLocator = By.xpath(".//div[contains(@class,'listHeader')]");
	private static By gotItLocator = By
			.xpath("//div[@data-autoid='womenFunnelInfo']//button[contains(@class,'primaryButton')]");
	private static By bookingForWomanNotificationLocator = By.xpath("//div[contains(@class,'snackbarprimary')]");
	private static By dateLocator = By.xpath("//div[contains(@class,'dateInputWrapper') and @role='button']");
	private static By dateValueLocator = By.xpath("//div[contains(@class,'calendarDate')]");
	private static By datePickerLocator = By.xpath("//div[contains(@class,'datepicker')]");

	public HomePage(Browser browserName,boolean isHeadless) {
		super(browserName,isHeadless);
		goToWebsite(getproperty(QA, "URL"));
	}

	public HomePage(WebDriver driver) {
		super(driver);
		goToWebsite(getproperty(QA, "URL"));
		// TODO Auto-generated constructor stub
	}

	public SearchBusesPage clickOnSearchBusButton() {
		searchBuses(searchButtonLocator);
		logger.info("Element found with locator: " + searchButtonLocator + "& click action is performed");
		SearchBusesPage searchBus = new SearchBusesPage(getDriver());
		return searchBus;
	}

	public void searchBuses(By searchButtonLocator) {
		logger.info("Finding element with locator: " + searchButtonLocator);
		clickOnWebElement(searchButtonLocator);
	}

	public HomePage clickOnBookingForWoman(By locator) {
		WebElement bookingForWoman = driver.get().findElement(locator);
		clickOnWebElement(locator);
		waitForElementToBeInvisible(bookingForWomanNotificationLocator);
		WebElement gotItButton = waitForVisibilityOfElementLocated(gotItLocator);
		clickOnWebElement(gotItLocator);
		return this;
	}

	public HomePage selectLocation( String locationValue) {
		WebElement searchSuggestion = waitForVisibilityOfElementLocated( searchSuggestions);
		WebElement fromInputField = waitForVisibilityOfElementLocated(inputTextBoxFieldLocator);
		logger.info("Located element with locator :" + fromInputField);
		enterText(inputTextBoxFieldLocator, locationValue);
		logger.info("Value entered in locator " + fromInputField + " is :" + locationValue);
		List<WebElement> searchSuggestionsList = waitForNumberOfElementsToBeMoreThan(searchCategoryLocator, 2);
		logger.info("Located the search list with size: " + searchSuggestionsList.size());
		WebElement locationSearchList = searchSuggestionsList.get(0);
		List<WebElement> fromFieldSearchResults = locationSearchList.findElements(locationNameLocator);
		logger.info("Search result available for from field :" + fromFieldSearchResults.size());
		fromFieldSearchResults.stream().filter(location -> location.getText().equalsIgnoreCase(locationValue))
				.findFirst().ifPresent(WebElement::click);
		logger.info("location is selected");
		return this;
	}

	public SearchBusesPage checkBookingForWomen() {
		clickOnBookingForWoman(bookingForWomanLocator);
		WebElement bookingForWomenCheckbox = driver.get().findElement(BookingForWomanLocatorCheckbox);
		if (bookingForWomenCheckbox.getAttribute("class").contains("checked")) {
//			searchBusForTomorrow(driver, wait, tomorrowButtonLocator);
			searchBuses(searchButtonLocator);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Exception caught");
			}
		}
		SearchBusesPage searchBus = new SearchBusesPage(getDriver());
		return searchBus;
	}

	public HomePage selectDate(String dateValue) {
		clickOnWebElement(dateLocator);
		WebElement datePicker = waitForVisibilityOfElementLocated(datePickerLocator);
		List<WebElement> dateList = waitForVisibilityOfAllElementsLocated(dateValueLocator);
		logger.info("Date element is located");
//		dateList.stream().map(date->date.getText()).forEach(System.out::println);
		dateList.stream().filter(date -> date.getText().equals(dateValue))
				.filter(date -> date.getAttribute("class").contains("available")).findFirst()
				.ifPresent(WebElement::click);
		logger.info("Date is selected");
		return this;
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

	public HomePage clickOnSourceOrDestinationField() {
		clickOnWebElement(fromFieldLocator);
		return this;
	}
}
