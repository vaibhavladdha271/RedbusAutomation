package com.ui.pages;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedbusAutomation {

	public static void main(String[] args) {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.get("https://www.redbus.in/");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
		List<String> seats = Arrays.asList("9", "10");
		String boardingPoint = "Kandivali";
		String droppingPoint = "Jagtap Dairy Chowk";
		int finalPrice;
		By fromFieldLocator = By.xpath("//div[contains(@class,'srcDestWrapper') and @role='button']");
		By tomorrowButtonLocator = By.xpath("(//div[contains(@class,'buttonsWrapper')]//button)[2]");
		By bookingForWomanLocator = By.cssSelector("input#switch");
		By switchForWomanLocator = By.xpath("//label[contains(@class,'switchLabel')]");
		WebElement fromField = wait.until(ExpectedConditions.visibilityOfElementLocated(fromFieldLocator));
		fromField.click();
		selectLocation(driver, wait, "Mumbai");
		selectLocation(driver, wait, "Pune");
//		clickOnBookingForWoman(wait, driver, bookingForWomanLocator);
		WebElement switchForWoman = driver.findElement(switchForWomanLocator);
		By busCountLocator = By.xpath("//span[contains(@class,'subtitle')]");
		if (switchForWoman.getAttribute("class").contains("checked")) {
//			searchBusForTomorrow(driver, wait, tomorrowButtonLocator);
			selectDate("25", driver, wait);
			searchBuses(wait);
			waitforTextToBePresentInElement(wait, busCountLocator, "buses");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Exception caught");
			}
			searchBusForWomen(wait);
		} else {
			selectDate("25", driver, wait);
			searchBuses(wait);
		}
		
		// Code for selecting filters & bus based on time & price
		WebElement busCount = null;
		By tupleWrapper = By.xpath("//li[contains(@class,'tupleWrapper')]");
		By busesNameLocator = By.xpath(".//div[contains(@class,'travelsName')]");
		By dateValueLocator = By.xpath("//span[contains(@class,'doj')]");
		By busPriceLocator = By.xpath(".//div[contains(@class,'fareWrapper')]/p");
		By boardTimeLocator = By.xpath(".//div[contains(@class,'timeRow')]/p[contains(@class,'boardingTime')]");
		selectFilter(wait, driver, filterList, tupleWrapper);
		waitforTextToBePresentInElement(wait, busCountLocator, "buses");
		if (waitforTextToBePresentInElement(wait, busCountLocator, "buses")) {
			busCount = visibilityOfWebElement(wait, busCountLocator);
		}
		WebElement dateValue = driver.findElement(dateValueLocator);
		System.out.println(dateValue.getText());
		System.out.println(busCount.getText());
		By viewSeatsLocator = By.xpath(".//button[contains(@class,'viewSeatsBtn')]");
		By endOfListLocator = By.xpath("//span[contains(@class,'endText')]");
		scrollPage(wait, driver, endOfListLocator, busesNameLocator, js);
		List<WebElement> busNameList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(busesNameLocator));
		List<WebElement> busPriceList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(busPriceLocator));
		List<WebElement> boardingTimeList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(boardTimeLocator));
		List<WebElement> viewSeats = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(viewSeatsLocator));
		selectBusBasedOnTime(busNameList, busPriceList, boardingTimeList, "Dolphin travel house", "17:50", viewSeats,
				js, wait);
		
		// Code for selecting seats
		By seatContainerLocator = By.xpath("//div[@data-autoid='seatContainer']");
		By seatLocator = By.xpath(".//span[contains(@class,'sleeper__ind-seat-styles')]");
		WebElement seatContainer = visibilityOfWebElement(wait, seatContainerLocator);
		String noOfSeats = selectSeats(seatContainer, seatLocator, seats, driver);
		System.out.println("Number of seats selected :" + noOfSeats);
		By priceWrapperLocator = By.xpath("//div[contains(@class,'priceWrap__ind-seat-styles')]");
		WebElement priceWrapperContainer = driver.findElement(priceWrapperLocator);
		By countPriceLocator = By.xpath("//div[contains(@class,'countPriceWrap')]");
		WebElement countPriceContainer = priceWrapperContainer.findElement(countPriceLocator);
		By finalPriceButtonLocator = By.xpath("//div[contains(@class,'priceWrap') and @role='button']");
		finalPrice = Integer.parseInt(getPriceAfterBoardingPoint(priceWrapperContainer, finalPriceButtonLocator, wait, driver).replaceAll("[^\\d]", ""));
		
		// Code for selecting boarding & dropping points
		List<WebElement> boardingOrDroppingPointList = driver
				.findElements(By.xpath("//div[contains(@class,'bpdpList')]"));
		WebElement boardingPoints = boardingOrDroppingPointList.get(0);
		WebElement droppingPoints = boardingOrDroppingPointList.get(1);
		selectPoint(boardingPoints, driver, boardingPoint);
		selectPoint(droppingPoints, driver, droppingPoint);
		
		// Code for passenger info
		String genderValue = "Male";
		By countryCodeDropdownLocator = By.xpath("//div[contains(@class,'countryCode___')]");
		WebElement countryCodeDropdown = visibilityOfWebElement(wait, countryCodeDropdownLocator);
		countryCodeDropdown.click();
		By countryContainerLocator = By.xpath("//div[contains(@class,'countryBodyWrap')]");
		WebElement countryContainer = visibilityOfWebElement(wait, countryContainerLocator);
		List<WebElement> countryList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//div[contains(@class,'countryBodyWrap')]//div[contains(@class,'listHeader')]")));
		countryList.stream().filter(country -> country.getText().trim().equalsIgnoreCase("India (+91)")).findFirst()
				.ifPresent(WebElement::click);
		By phoneNumberLocator = By.xpath("//div[contains(@class,'inputBox')]/input[@type='number']");
		WebElement phoneNumber = driver.findElement(phoneNumberLocator);
		enterText(phoneNumber, "9021906548");
		By emailIdLocator = By.xpath("//input[@id='0_5']");
		WebElement emailId = driver.findElement(emailIdLocator);
		enterText(emailId, "vaibhavladdha271@gmail.com");
		By stateFieldLocator = By.xpath("//input[@id='0_201']");
		WebElement stateField = driver.findElement(stateFieldLocator);
		stateField.click();
		By stateListLocator = By.xpath("//div[contains(@class,'stateBodyWrap')]//div[contains(@class,'listItem')]");
		List<WebElement> stateList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(stateListLocator));
		stateList.stream().filter(state -> state.getText().trim().equalsIgnoreCase("Maharashtra")).findFirst()
				.ifPresent(WebElement::click);
		By whatsappToggleLocator = By.id("whatsapp_toggle");
		WebElement whatsappToggle = driver.findElement(whatsappToggleLocator);
		whatsappToggle.click();
		List<String> names = List.of("Vaibhav", "Namrata");
		List<Integer> ages = List.of(30, 32);
		List<String> genders = List.of("Male", "Female");
		fillPassengerDetails(driver, names, ages, genders);
		String countOfSeats = verifySeatDetails(driver);
		addFreeCancellation(driver, "No");
		addRedBusAssurance(driver, "Yes", finalPrice, countOfSeats);
		verifyBusDetails(driver);
		finalPrice=addRedBusDonation(driver,"Yes",finalPrice);
		WebElement priceAfterAddingPassengerdetails=driver.findElement(By.xpath("//div[contains(@class,'payAmt')]/span"));
		int price=Integer.parseInt(priceAfterAddingPassengerdetails.getText().replaceAll("[^\\d]", ""));
		System.out.println("Final ");
		WebElement continueBookingButton=driver.findElement(By.xpath("//div[contains(@class,'payNowBtn')]/button"));
		continueBookingButton.click();
		verifyPassengerAndBusDetails(driver);
	}
	
	public static void verifyPassengerAndBusDetails(WebDriver driver)
	{
		WebElement travelName=driver.findElement(By.xpath("//div[contains(@class,'travelsName___')]"));
		System.out.println(travelName.getText());
		WebElement boardingPoint=driver.findElement(By.xpath("//div[contains(@class,'bpDetailsWrap')]/span"));
		System.out.println(boardingPoint.getText());
		WebElement droppingPoint=driver.findElement(By.xpath("//div[contains(@class,'dpDetailsWrap')]/span"));
		System.out.println(droppingPoint.getText());
	}
	public static int addRedBusDonation(WebDriver driver,String confirmation,int finalPrice)
	{
		WebElement redBusCareCheckBox=driver.findElement(By.xpath("//input[@id='redCare']"));
		
		if(confirmation.equals("Yes"))
		{
			redBusCareCheckBox.click();
			if(driver.findElement(By.xpath("//div[contains(@class,'redCareWrap')]//label[@for='redCare']")).getAttribute("class").contains("checked"))
			{
				WebElement redBusCare=driver.findElement(By.xpath("//div[contains(@class,'redCareWrap')]//div[contains(@class,'listText')]"));
				String redBusCareText=redBusCare.getText();
				int redBusCarePrice=Integer.parseInt(redBusCareText.split(" ")[1].replace("₹",""));
				finalPrice+=redBusCarePrice;
			}
			System.out.println("Bus ticket price after red bus donation :"+finalPrice);
			
		}
		else if(confirmation.equals("No"))
		{
			System.out.println("Red bus donation is not added/considered");
		}
		return finalPrice;
		
	}
	public static void addRedBusAssurance(WebDriver driver, String assuranceConfirmationText, int finalPrice,
			String countOfSeats) {
		int insurancePerPerson;
		int priceWithInsurance;
		WebElement insurance = driver
				.findElement(By.xpath("//div[contains(@class,'insuranceWrapper')]//div[contains(@class,'subTitle')]"));
		insurancePerPerson = Integer.parseInt(insurance.getText().split(" ")[1]);
		if (assuranceConfirmationText.equals("Yes")) {
			WebElement confirmationRadioButton = driver
					.findElement(By.xpath("//div[@id='insuranceConfirmText']//div[contains(@class,'radioContainer')]"));
			confirmationRadioButton.click();
			int seat = Integer.parseInt(countOfSeats);
			priceWithInsurance = seat * insurancePerPerson;
			System.out.println("Insurance price : " + priceWithInsurance);
			finalPrice += priceWithInsurance;
			System.out.println("Ticket price with insurance :" + finalPrice);
		} else if (assuranceConfirmationText.equals("No")) {
			WebElement rejectionRadioButton = driver
					.findElement(By.xpath("//div[@id='insuranceRejectText']//div[contains(@class,'radioContainer')]"));
			rejectionRadioButton.click();
		}
	}

	public static void addFreeCancellation(WebDriver driver, String confirmationText) {
		if (confirmationText.equals("Yes")) {
			WebElement confirmationRadioButton = driver
					.findElement(By.xpath("//div[@id='fcConfirmText']//div[contains(@class,'radioContainer')]"));
			confirmationRadioButton.click();
		} else if (confirmationText.equals("No")) {
			WebElement rejectionRadioButton = driver
					.findElement(By.xpath("//div[@id='fcRejectText']//div[contains(@class,'radioContainer')]"));
			rejectionRadioButton.click();
		}
	}

	public static String verifySeatDetails(WebDriver driver) {
		WebElement seatCount = driver
				.findElement(By.xpath("//div[contains(@class,'seatDetailsHeader')]//div[contains(@class,'subTitle')]"));
		System.out.println("Number of seats: " + seatCount.getText());
		String countOfSeats = seatCount.getText();
		List<WebElement> seatDetails = driver
				.findElements(By.xpath("//div[contains(@class,'seatWrapper')]//div[contains(@class,'label')]"));
		seatDetails.stream().map(seat -> seat.getText()).forEach(System.out::println);
		return countOfSeats;
	}

	public static void verifyBusDetails(WebDriver driver) {
		WebElement busName = driver.findElement(By.xpath("//div[contains(@class,'travelsNameExp')]"));
		System.out.println(busName.getText());
		WebElement boardingPointName = driver.findElement(By.xpath("//div[contains(@class,'bpDetailsWrap')]/span"));
		System.out.println(boardingPointName.getText());
		WebElement droppingPointName = driver.findElement(By.xpath("//div[contains(@class,'dpDetailsWrap')]/span"));
		System.out.println(droppingPointName.getText());
	}

	public static void fillPassengerDetails(WebDriver driver, List<String> names, List<Integer> ages,
			List<String> genders) {
		IntStream.range(0, names.size()).forEach(i -> {
			String nameXpath = String.format("//input[@id='%d_4']", i);
			WebElement nameField = driver.findElement(By.xpath(nameXpath));
			nameField.clear();
			enterText(nameField, names.get(i));

			String ageXpath = String.format("//input[@id='%d_1']", i);
			WebElement ageField = driver.findElement(By.xpath(ageXpath));
			ageField.clear();
			enterText(ageField, String.valueOf(ages.get(i)));

			String genderXpath = String.format("//div[@id='%d_2']//label[text()='%s']/following-sibling::span", i,
					genders.get(i));
			WebElement genderField = driver.findElement(By.xpath(genderXpath));
			genderField.click();
		});
	}

	public static void enterText(WebElement element, String textToEnter) {
		element.sendKeys(textToEnter);
	}

	public static void selectPoint(WebElement boardingOrDroppingPoint, WebDriver driver, String pointValue) {
		List<WebElement> pointOptions = boardingOrDroppingPoint
				.findElements(By.xpath("//div[contains(@class,'bpdpSelection')]"));
		pointOptions.stream().filter(pointOption -> pointOption.getAttribute("aria-label").contains(pointValue))
				.findFirst().ifPresent(pointOption -> {
					WebElement radioButton = pointOption
							.findElement(By.xpath(".//div[contains(@class,'radioContainer')]"));
					clickOnElementUsingActions(driver, radioButton);
				});
	}

	public static String getPriceAfterBoardingPoint(WebElement priceWrapperContainer, By finalPriceButtonLocator, WebDriverWait wait,
			WebDriver driver) {
		WebElement finalPriceButton = priceWrapperContainer.findElement(finalPriceButtonLocator);
		finalPriceButton.click();
		By priceBreakUpContainerLocator = By.xpath("//div[contains(@class,'bottomSheetContainer')]");
		WebElement priceBreakUpContainer = visibilityOfWebElement(wait, priceBreakUpContainerLocator);
		By finalPriceLocator = By.xpath("//span[contains(@class,'finalPrice')]");
		By closeButtonLocator = By.xpath("//button[contains(@class,'actionButton')]");
		String finalPrice = priceBreakUpContainer.findElement(finalPriceLocator).getText();
		WebElement closeButton = priceBreakUpContainer.findElement(closeButtonLocator);
		clickOnElementUsingActions(driver, closeButton);
		By selectBoardingPointLocator = By.xpath(
				"//div[contains(@class,'priceWrap__ind-seat-styles')]//button[contains(@class,'primaryButton')]");
		WebElement selectBoardingPointButton = priceWrapperContainer.findElement(selectBoardingPointLocator);
		clickOnElementUsingActions(driver, selectBoardingPointButton);
		return finalPrice;
	}

	public static void clickOnElementUsingActions(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click(element).build().perform();
	}

	public static String selectSeats(WebElement seatContainer, By seatLocator, List<String> seats, WebDriver driver) {
		AtomicInteger totalPrice = new AtomicInteger(0);
		List<WebElement> seatsList = seatContainer.findElements(seatLocator);
		seatsList.stream()
				.filter(seat -> seat.findElements(By.xpath("./span[contains(@class,'sleeperSold')]")).isEmpty())
				.filter(seat -> seats.contains(seat.getAttribute("id"))).forEach(seat -> {
					System.out.println("Clicking on available seat" + seat.getAttribute("id"));
					seat.click();
					WebElement seatPrice = seat
							.findElement(By.xpath("./span[contains(@class,'selectedSleeperPrice')]"));
					int price = Integer.parseInt(seatPrice.getText().replaceAll("[^0-9]", ""));
					totalPrice.addAndGet(price);
					System.out.println("Seat ID: " + seat.getAttribute("id") + "|" + "Seat price: " + price);
				});
		WebElement seatCount = driver.findElement(By.xpath("//span[contains(@class,'seatCount')]"));
		List<String> seatCountDetails = Arrays.asList(seatCount.getText().split(" "));
		String countOfSeat = seatCountDetails.get(0);
		System.out.println("Total price for selected seats: " + totalPrice);
		return countOfSeat;
	}

	public static void searchBuses(WebDriverWait wait) {

		By searchButtonLocator = By.xpath("//button[contains(@class,'searchButtonWrapper')]");

		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(searchButtonLocator));

		searchButton.click();

	}

	public static void searchBusForWomen(WebDriverWait wait) {

		WebElement highlyRatedForWomanFilter = null;

		By switchForfilterlocator = By.xpath("//div[contains(text(),'Highly rated by women')]");

		highlyRatedForWomanFilter = waitForElementToBeClickable(wait, switchForfilterlocator);

		highlyRatedForWomanFilter.click();

	}

	public static void clickOnBookingForWoman(WebDriverWait wait, WebDriver driver, By locator) {

		By gotItLocator = By.xpath("//div[@data-autoid='womenFunnelInfo']//button[contains(@class,'primaryButton')]");

		WebElement bookingForWoman = driver.findElement(locator);

		bookingForWoman.click();

		By bookingForWomanNotificationLocator = By.xpath("//div[contains(@class,'snackbarprimary')]");

		wait.until(ExpectedConditions.invisibilityOfElementLocated(bookingForWomanNotificationLocator));

		WebElement gotItButton = wait.until(ExpectedConditions.visibilityOfElementLocated(gotItLocator));

		gotItButton.click();

	}

	public static WebElement visibilityOfWebElement(WebDriverWait wait, By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static boolean waitforTextToBePresentInElement(WebDriverWait wait, By locator, String elementText) {

		return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, elementText));

	}

	public static WebElement waitForElementToBeClickable(WebDriverWait wait, By locator) {

		return wait.until(ExpectedConditions.elementToBeClickable(locator));

	}

	public static void scrollPage(WebDriverWait wait, WebDriver driver, By endOfListlocator, By busesNameLocator,

			JavascriptExecutor js) {

		while (true) {

			List<WebElement> busNameList = wait

					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(busesNameLocator));

			List<WebElement> endOfList = driver.findElements(endOfListlocator);

			if (!endOfList.isEmpty()) {

				break;

			}

			js.executeScript("arguments[0].scrollIntoView({behaviour:'smooth'})",

					busNameList.get(busNameList.size() - 3));

		}

	}

	public static void selectBusBasedOnTime(List<WebElement> busNameList, List<WebElement> busPriceList,

			List<WebElement> boardingTimeList, String busName, String boardTime, List<WebElement> viewSeats,

			JavascriptExecutor js, WebDriverWait wait) {

		IntStream.range(0, busNameList.size())

				.filter(i -> busNameList.get(i).getText().trim().equalsIgnoreCase(busName))

				.filter(i -> boardingTimeList.get(i).getText().equals(boardTime)).findFirst().ifPresent(i -> {

					WebElement viewSeatButton = viewSeats.get(i);

					try {

						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",

								viewSeatButton);

						Thread.sleep(1000);

						wait.until(ExpectedConditions.elementToBeClickable(viewSeatButton)).click();

					} catch (Exception e) {

						js.executeScript("arguments[0].click();", viewSeatButton);

					}

				});

	}

	public static void searchBusForTomorrow(WebDriver driver, WebDriverWait wait, By locator) {

//		WebElement tomorrowButton = driver.findElement(locator);

		waitForElementToBeClickable(wait, locator).click();

//		tomorrowButton.click();

		String tomorrowDate = verifyTomorrowDate();

		System.out.println(tomorrowDate);

	}

	public static void selectDate(String dateValue, WebDriver driver, WebDriverWait wait) {

		By dateLocator = By.xpath("//div[contains(@class,'dateInputWrapper') and @role='button']");

		By dateValueLocator = By.xpath("//div[contains(@class,'calendarDate')]");

		WebElement dateField = driver.findElement(dateLocator);

		dateField.click();

		By datePickerLocator = By.xpath("//div[contains(@class,'datepicker')]");

		WebElement datePicker = wait.until(ExpectedConditions.visibilityOfElementLocated(datePickerLocator));

		List<WebElement> dateList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dateValueLocator));

//		dateList.stream().map(date->date.getText()).forEach(System.out::println);

		dateList.stream().filter(date -> date.getText().equals(dateValue))

				.filter(date -> date.getAttribute("class").contains("available")).findFirst()

				.ifPresent(WebElement::click);

	}

	public static String verifyTomorrowDate() {

		Date today = new Date();

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(today);

		calendar.add(Calendar.DATE, 1);

		Date tomorrow = calendar.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");

		String tomorrowDate = dateFormat.format(tomorrow);

		return tomorrowDate;

	}

	public static void selectFilter(WebDriverWait wait, WebDriver driver, List<String> filterList, By tupleWrapper) {

		WebElement filter = null;

		for (String filterValue : filterList) {

			By filterlocator = By.xpath("//div[contains(text(),'" + filterValue + "')]");

			filter = wait.until(ExpectedConditions.elementToBeClickable(filterlocator));

			filter.click();

			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tupleWrapper));

		}

	}

	public static void selectFilterForWomen() {

	}

	public static void selectLocation(WebDriver driver, WebDriverWait wait, String locationValue) {

		By searchSuggestions = By.xpath("//div[contains(@class,'searchSuggestionWrapper')]");

		WebElement searchSuggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(searchSuggestions));

		By inputTextBoxFieldLocator = By.id("srcDest");

		WebElement fromInputField = driver.findElement(inputTextBoxFieldLocator);

		fromInputField.sendKeys(locationValue);

		By searchCategoryLocator = By.xpath("//div[contains(@class,'searchCategory')]");

		List<WebElement> searchSuggestionsList = wait

				.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchCategoryLocator, 2));

		System.out.println(searchSuggestionsList.size());

		WebElement locationSearchList = searchSuggestionsList.get(0);

		By locationNameLocator = By.xpath(".//div[contains(@class,'listHeader')]");

		List<WebElement> fromFieldSearchResults = locationSearchList.findElements(locationNameLocator);

		System.out.println(fromFieldSearchResults.size());

		fromFieldSearchResults.stream().filter(location -> location.getText().equalsIgnoreCase(locationValue))

				.findFirst().ifPresent(WebElement::click);

	}
}
