import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Date;

public class RedbusAutomation {

	public static void main(String[] args) {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.get("https://www.redbus.in/");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> filterList = Arrays.asList("AC", "SLEEPER");
		By fromFieldLocator = By.xpath("//div[contains(@class,'srcDestWrapper') and @role='button']");
		By tomorrowButtonLocator = By.xpath("(//div[contains(@class,'buttonsWrapper')]//button)[2]");
		By bookingForWomanLocator = By.cssSelector("input#switch");
		By switchForWomanLocator = By.xpath("//label[contains(@class,'switchLabel')]");
		WebElement fromField = wait.until(ExpectedConditions.visibilityOfElementLocated(fromFieldLocator));
		fromField.click();
		selectLocation(driver, wait, "Mumbai");
		selectLocation(driver, wait, "Pune");
		clickOnBookingForWoman(wait, driver, bookingForWomanLocator);
		WebElement switchForWoman = driver.findElement(switchForWomanLocator);
		By busCountLocator = By.xpath("//span[contains(@class,'subtitle')]");
		if (switchForWoman.getAttribute("class").contains("checked")) {
			searchBusForTomorrow(driver, wait, tomorrowButtonLocator);
			waitforTextToBePresentInElement(wait, busCountLocator, "buses");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Exception caught");
			}
			searchBusForWomen(wait);
		} else {
			selectDate("19", driver, wait);
			searchBuses(wait);
		}

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
		By seatContainerLocator = By.xpath("//div[@data-autoid='seatContainer']");
		By seatLocator = By.xpath(".//span[contains(@class,'sleeper__ind-seat-styles')]");
		WebElement seatContainer = visibilityOfWebElement(wait, seatContainerLocator);
		List<WebElement> seatsList = seatContainer.findElements(seatLocator);
//				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(seatLocator));
		seatsList.stream()
				.filter(seat -> seat.findElements(By.xpath("./span[contains(@class,'sleeperSold')]")).isEmpty())
				.filter(seat -> seat.getAttribute("id").equalsIgnoreCase("9")).findFirst().ifPresent(seat -> {
					System.out.println("Clicking on available seat" + seat.getAttribute("id"));
					seat.click();
				});

//		.forEach(seat->{
//			String seatId=seat.getAttribute("id");
//			System.out.println("Sold seats: "+seatId);
//		});

//		IntStream.range(0, seatsList.size())
//			.filter(i->seatsList.get(i).findElement(By.xpath("/span")).getAttribute("class").contains("Sold"))
//			.map(i->seatsList.get(i).getAttribute("id").toString())
//			.forEach(System.out::println);
//		seatsList.stream().map(seat->seat.getAttribute("id").toString()).sorted().forEach(System.out::println);
//		for(WebElement bus:busNameList)
//		{
//			String busName=bus.getText().trim();
//				if(busName.equalsIgnoreCase("Dolphin travel house"))
//				{
//					
//					System.out.println("Bus price :"+busPrice.getText());	
//				}
//			}
//			
//		}
//		busNameList.stream().map(bus -> bus.getText()).forEach(System.out::println);
//		busPriceList.stream().map(price->price.getText()).forEach(System.out::println);
	}

	public static void clickOnViewSeat() {

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
		WebElement tomorrowButton = driver.findElement(locator);
		tomorrowButton.click();
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
