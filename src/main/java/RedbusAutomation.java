import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedbusAutomation {

	public static void main(String[] args) {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.get("https://www.redbus.in/");
		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
		By fromFieldLocator = By.xpath("//div[contains(@class,'srcDestWrapper') and @role='button']");
		WebElement fromField = wait.until(ExpectedConditions.visibilityOfElementLocated(fromFieldLocator));
		fromField.click();
		selectLocation(driver, wait, "Mumbai");
		selectLocation(driver, wait, "Pune");
		By dateLocator = By.xpath("//div[contains(@class,'dateInputWrapper') and @role='button']");
		By dateValueLocator = By.xpath("//div[contains(@class,'calendarDate')]");
		WebElement dateField = driver.findElement(dateLocator);
		dateField.click();
		By datePickerLocator = By.xpath("//div[contains(@class,'datepicker')]");
		WebElement datePicker = wait.until(ExpectedConditions.visibilityOfElementLocated(datePickerLocator));
		List<WebElement> dateList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dateValueLocator));
//		dateList.stream().map(date->date.getText()).forEach(System.out::println);
		dateList.stream().filter(date -> date.getText().equals("19"))
				.filter(date -> date.getAttribute("class").contains("available")).findFirst()
				.ifPresent(WebElement::click);
		By searchButtonLocator = By.xpath("//button[contains(@class,'searchButtonWrapper')]");
		WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(searchButtonLocator));
		searchButton.click();
		By busCountLocator = By.xpath("//span[contains(@class,'subtitle')]");
		WebElement busCount = null;
		By tupleWrapper = By.xpath("//li[contains(@class,'tupleWrapper')]");
		By busesNameLocator = By.xpath(".//div[contains(@class,'travelsName')]");
		selectFilter(wait, driver, filterList, tupleWrapper);
		if (wait.until(ExpectedConditions.textToBePresentInElementLocated(busCountLocator, "buses"))) {
			busCount = wait.until(ExpectedConditions.visibilityOfElementLocated(busCountLocator));
		}
		System.out.println(busCount.getText());
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (true) {
			List<WebElement> busNameList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(busesNameLocator));
			List<WebElement> endOfList = driver.findElements(By.xpath("//span[contains(@class,'endText')]"));
			if (!endOfList.isEmpty()) {
				break;
			}
			js.executeScript("arguments[0].scrollIntoView({behaviour:'smooth'})",
					busNameList.get(busNameList.size() - 3));
		}
		List<WebElement> busNameList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(busesNameLocator));
		busNameList.stream().map(bus -> bus.getText()).forEach(System.out::println);
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
