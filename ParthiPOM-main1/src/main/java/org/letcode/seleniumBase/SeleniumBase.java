package org.letcode.seleniumBase;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;



public class SeleniumBase implements SeleniumAPI {
	// Time out for the implicitly wait and timeout
	long TimeOuts = 60; // maximum time 60 to 90
	long MaxWaitTime = 40;

	Duration timeOuts = Duration.ofSeconds(TimeOuts);
	Duration maxWaitTime = Duration.ofSeconds(MaxWaitTime);

	ChromeOptions options = new ChromeOptions();

	RemoteWebDriver driver = null;
	WebDriverWait wait = null;

	public int pointX, pointY;
	public int height, width;
	public Point point;
	public String screenShotDst = null;

	/**
	 * @author Parthi
	 * @description : launch the chrome browser
	 */
	@Override
	public void setUp(String url) {
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
		options.addArguments("--remote-allow-origins=*");
		// options.setBinary("/snap/bin/chromium");
		// options.addArguments("--headless");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeOuts);
		driver.get(url);
		wait = new WebDriverWait(driver, maxWaitTime);

		// For the page resize
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_MINUS);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_MINUS);
		} catch (AWTException e) {
			System.err.println("Robot in error.");
			e.printStackTrace();
		}

	}

	/**
	 * @author : Parthi
	 * @description : launch the chrome browser with incoginto
	 */
	@Override
	public void setUpWithIncoginto(String url) {
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("incognito");
		options.addArguments("disable-popup-blocking");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeOuts);
		driver.get(url);
		wait = new WebDriverWait(driver, maxWaitTime);
	}

	/**
	 * @author : Parthi
	 * @description : launch the different browser
	 */
	@Override
	public void setUp(Browser browserName, String url) {
		options.addArguments("--remote-allow-origins=*");
		switch (browserName) {
			case CHROME:
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
				driver = new ChromeDriver(options);
				break;
			case FIREFOX:
				System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver");
				driver = new FirefoxDriver();
				break;
			case EDGE:
				driver = new EdgeDriver();
				break;
			default:
				System.err.println("Driver is not defined");
				break;
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeOuts);
		driver.get(url);
		wait = new WebDriverWait(driver, maxWaitTime);
	}

	/**
	 * @author : Parthi
	 * @description : launch only the url
	 */
	@Override
	public void set(String url) {
		driver.manage().timeouts().implicitlyWait(timeOuts);
		driver.get(url);
		wait = new WebDriverWait(driver, maxWaitTime);
	}

	/**
	 * @author : Parthi
	 * @description : different locators to pass then get the result
	 */
	@Override
	public WebElement element(Locators type, String value) {
		try {
			switch (type) {
				case id:
					return driver.findElement(By.id(value));
				case name:
					return driver.findElement(By.name(value));
				case xpath:
					return driver.findElement(By.xpath(value));
				case link:
					return driver.findElement(By.linkText(value));
				case className:
					return driver.findElement(By.className(value));
				case css:
					return driver.findElement(By.cssSelector(value));
				case tagName:
					return driver.findElement(By.tagName(value));
				default:
					break;
			}
		} catch (NoSuchElementException e) {
			System.err.println("Element not found.. " + e.getMessage());
			throw new NoSuchElementException("Element not found");
		} catch (WebDriverException e) {
			System.err.println("Web driver exception.." + e.getMessage());
			throw new WebDriverException("Some unknown webdriver error");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * @author : Parthi
	 * @description : findelements to access the locators
	 */
	@Override
	public List<WebElement> elements(Locators type, String value) {
		List<WebElement> elements = null;
		try {
			switch (type) {
				case id:
					return elements = driver.findElements(By.id(value));
				case name:
					return elements = driver.findElements(By.name(value));
				case xpath:
					return elements = driver.findElements(By.xpath(value));
				case link:
					return elements = driver.findElements(By.linkText(value));
				case className:
					return elements = driver.findElements(By.className(value));
				case css:
					return elements = driver.findElements(By.cssSelector(value));
				default:
					break;
			}
		} catch (NoSuchElementException e) {
			System.err.println("Elements not found => " + e.getMessage());
			throw new NoSuchElementException("Element not found");
		} catch (WebDriverException e) {
			System.err.println(e.getMessage());
			throw new WebDriverException("Some unknown webdriver error");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return elements;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in click function
	 */
	@Override
	public void click(WebElement ele) {
		WebElement element = wait.withMessage("Element is not clickable")
				.until(ExpectedConditions.elementToBeClickable(ele));
		element.click();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in submit function
	 */
	@Override
	public void submit(WebElement ele) {
		WebElement element = isElementVisible(ele);
		element.submit();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in action to click function
	 */
	@Override
	public void rightClick(WebElement ele) {
		WebElement element = isElementVisible(ele);
		Actions builder = new Actions(driver);
		builder.contextClick(element).perform();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in right click function
	 */
	@Override
	public void Actions(WebElement ele) {
		Actions actions = new Actions(driver);
		actions.moveToElement(ele).click().build().perform();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in sendkeys function
	 */
	@Override
	public void type(WebElement ele, String testData) {
		try {
			WebElement element = isElementVisible(ele);
			element.clear();
			element.sendKeys(testData);
		} catch (NullPointerException e) {
			System.out.println("Element might be null => " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @author : Parthi
	 * @description : append text type with selenium sendkeys function
	 */
	@Override
	public void appendText(WebElement ele, String testData) {
		WebElement element = isElementVisible(ele);
		element.sendKeys(testData);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in sendkeys with keys function
	 */
	@Override
	public void typeWithKey(WebElement ele, String testData, Keys keys) {
		try {
			WebElement element = isElementVisible(ele);
			element.clear();
			element.sendKeys(testData, keys);
		} catch (NullPointerException e) {
			System.out.println("Element might be null => " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @author : Parthi
	 * @description : selenium in keys function use of sendkeys
	 */
	@Override
	public void keys(WebElement ele, Keys keys) {
		try {
			WebElement element = isElementVisible(ele);
			element.sendKeys("", keys);
		} catch (NullPointerException e) {
			System.out.println("Element might be null => " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @author : Parthi
	 * @description : selenium in active element to sendkeys function
	 */
	@Override
	public void activeToType(WebElement ele, String testData) {
		WebElement element = isElementVisible(ele);
		element = driver.switchTo().activeElement();
		element.clear();
		element.sendKeys(testData);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in active element to sendkeys with keys function
	 */
	@Override
	public void activeWithKey(WebElement ele, String testData, Keys keys) {
		WebElement element = isElementVisible(ele);
		element = driver.switchTo().activeElement();
		element.clear();
		element.sendKeys(testData, keys);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in select by value(dropdown) function
	 */
	@Override
	public void selectValue(WebElement ele, String value) {
		WebElement element = isElementVisible(ele);
		new Select(element).selectByValue(value);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in select by text(dropdown) function
	 */
	@Override
	public void selectText(WebElement ele, String value) {
		WebElement element = isElementVisible(ele);
		new Select(element).selectByVisibleText(value);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in index position(dropdown) function
	 */
	@Override
	public void selectIndex(WebElement ele, int position) {
		WebElement element = isElementVisible(ele);
		new Select(element).selectByIndex(position);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in window handles function
	 */
	@Override
	public void switchToWindow(int i) {
		Set<String> windowHandles = driver.getWindowHandles();
		ArrayList<String> list = new ArrayList<String>(windowHandles);
		driver.switchTo().window(list.get(i));
	}

	/**
	 * @author : Parthi
	 * @description : selenium in drag & drop with element function
	 */
	@Override
	public void dragAndDrop(WebElement ele, WebElement ele2) {
		WebElement source = isElementVisible(ele);
		WebElement target = isElementVisible(ele2);
		Actions builder = new Actions(driver);
		builder.dragAndDrop(source, target).build().perform();
	}

	/**
	 * @author
	 * @description : selenium in drag & drop with points function
	 */
	@Override
	public void dragAndDropWithPoint(WebElement ele, int a, int b) {
		WebElement source = isElementVisible(ele);
		Actions builder = new Actions(driver);
		builder.dragAndDropBy(source, a, b).build().perform();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in get title function
	 */
	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in get URL function
	 */
	@Override
	public String getURL() {
		return driver.getCurrentUrl();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in get text function
	 */
	@Override
	public String getText(WebElement ele) {
		WebElement element = isElementVisible(ele);
		return element.getText();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in get attribute function
	 */
	@Override
	public String getAttribute(WebElement ele, String type) {
		WebElement element = isElementVisible(ele);
		return element.getAttribute(type);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in isDisplay function
	 */
	@Override
	public boolean isDisplayed(WebElement ele) {
		return ele.isDisplayed();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in isSelected function
	 */
	@Override
	public boolean isSelected(WebElement ele) {
		return ele.isSelected();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in isEnabled function
	 */
	@Override
	public boolean isEnabled(WebElement ele) {
		return ele.isEnabled();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in alert accept function
	 */
	@Override
	public void alertAccept() {
		driver.switchTo().alert().accept();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in alert dismiss function
	 */
	@Override
	public void alertDismiss() {
		driver.switchTo().alert().dismiss();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in alert get text function
	 */
	@Override
	public String alertGetText() {
		String text = driver.switchTo().alert().getText();
		return text;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in alert type(sendkeys) function
	 */
	@Override
	public void alertType(String testData) {
		driver.switchTo().alert().sendKeys(testData);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in switch frame function
	 */
	@Override
	public void switchToFrame(int i) {
		driver.switchTo().frame(i);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in switch default function
	 */
	@Override
	public void switchToDefault() {
		driver.switchTo().defaultContent();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in point(x,y) use element function
	 */
	@Override
	public void getLocationpoints(WebElement ele) {
		WebElement element = isElementVisible(ele);
		Point point = element.getLocation();
		int x = point.getX();
		int y = point.getY();

		this.pointX = x;
		this.pointY = y;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in find the element height, width and point(x,y)
	 *              function
	 */
	@Override
	public void getHeightWidth(WebElement ele) {
		WebElement element = isElementVisible(ele);
		Rectangle rect = element.getRect();
		int width = rect.getWidth();
		int height = rect.getHeight();
		Point point = rect.getPoint();
		this.width = width;
		this.height = height;
		this.point = point;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in element of the color value function
	 */
	@Override
	public String getColor(WebElement ele) {
		WebElement element = isElementVisible(ele);
		String color = element.getCssValue("background-color");
		return color;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in mouseover use of action function
	 */
	@Override
	public void mouseover(WebElement ele) {
		WebElement ele1 = ele;
		Actions builder = new Actions(driver);
		builder.moveToElement(ele1).perform();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in browser close function
	 */
	@Override
	public void close() {
		driver.close();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in broser quit function
	 */
	@Override
	public void quit() {
		driver.quit();
	}

	/**
	 * @author Parthi
	 * @description : selenium in navigate of forward funcation
	 */
	@Override
	public void forward() {
		driver.navigate().forward();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in navigate of back funcation
	 */
	@Override
	public void back() {
		driver.navigate().back();
	}

	/**
	 * @author : Parthi
	 * @description : selenium in navigate To function
	 */
	@Override
	public void navigateto(String url) {
		driver.navigate().to(url);
	}

	/**
	 * @author : Parthi
	 * @description : selenium in navigate of refresh function
	 */
	@Override
	public void refresh() {
		driver.navigate().refresh();
	}

	/**
	 * @author : Parthi
	 * @description : page up use(action) function
	 */
	@Override
	public void pageUp() {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.PAGE_UP).build().perform();
	}

	/**
	 * @author : Parthi
	 * @description : page down use(action) function
	 */
	@Override
	public void pageDown() {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.PAGE_DOWN).build().perform();
	}

	/**
	 * @author : Parthi
	 * @description : pariticular element screenshot function
	 */
	@Override
	public void getScreenshotAsElement(WebElement ele, String dst) {
		WebElement element = isElementVisible(ele);
		File eleSrc = element.getScreenshotAs(OutputType.FILE);
		File eleDst = new File(dst);
		try {
			FileHandler.copy(eleSrc, eleDst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author : Parthi
	 * @description : page screenshot function
	 */
	@Override
	public void driverGetScreenshotAsPage(String dst) {
		File eleSrc = driver.getScreenshotAs(OutputType.FILE);
		File eleDst = new File(dst);
		try {
			FileHandler.copy(eleSrc, eleDst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.screenShotDst = String.valueOf(eleDst);
	}

	/**
	 * @author : Parthi
	 * @description : page with scroll down screenshot function
	 */
	@Override
	public void driverGetScreenshotAsFullPage(String dst) {
		Shutterbug.shootPage(driver, Capture.FULL, true).withName(dst).save("./sanps");
		this.screenShotDst = "./sanps/" + dst + ".png";
	}

	/**
	 * @author : Parthi
	 * @description : thread sleep function
	 */
	public void sleep(long a) {
		try {
			Thread.sleep(a);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author : Parthi
	 * @description : selenium in fluent wait function
	 */
	private WebElement isElementVisible(WebElement ele) {
		WebElement element = wait.withMessage("Element is not visible").until(ExpectedConditions.visibilityOf(ele));
		return element;
	}

	/**
	 * @author : Parthi
	 * @description : selenium in javascript executor with date function
	 */
	@Override
	public void date(String script, WebElement ele, String date) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(script, ele, date);
	}

}
