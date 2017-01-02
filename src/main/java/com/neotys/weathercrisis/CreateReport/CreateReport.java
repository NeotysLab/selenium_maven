package com.neotys.weathercrisis.CreateReport;

import java.io.File;
import java.util.concurrent.TimeUnit;
import static com.neotys.selenium.proxies.NLWebDriverFactory.addProxyCapabilitiesIfNecessary;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.neotys.selenium.proxies.NLWebDriver;
import com.neotys.selenium.proxies.NLWebDriverFactory;
import com.neotys.selenium.proxies.WebDriverProxy;



public class CreateReport {
	// TODO update the path to your Chrome driver and Phantom JS.
	private static final String CHROME_DRIVER_PATH = "C:\\Selenium\\chromedriver.exe";
	private static final String PHANTOM_JS_EXE_PATH = "C:\\phantomjs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";

	private static final Options OPTIONS;
	private static final String DRIVER = "driver";
	private static final String PHANTOM_JS = "PhantomJs";
	private static final String CHROME = "Chrome";
	private static final String FIREFOX = "FireFox";
	 private static  String baseUrl;
		//  private boolean acceptNextAlert = true;
	 private static  String UshahidiLocation;
	 static NLWebDriver driver;
	 static WebDriver webdriver;

	static {
		OPTIONS = new Options();
		initOptions();
	}

	//@SuppressWarnings("static-access")
	private static void initOptions() {
		final Option optionDriver = OptionBuilder.withLongOpt(DRIVER).withArgName("DRIVER").hasArg()
				.withDescription("The webdriver to use: " + FIREFOX + ", " + CHROME + " or " + PHANTOM_JS).isRequired()
				.create("d");
		final Option optionNoNeoLoad = OptionBuilder.withLongOpt("noNeoLoad")
				.withDescription("To launch driver without NeoLoad").create("no");
		final Option optionURL = OptionBuilder.withLongOpt("TestintUrl")
				.withDescription("Url of the weather crisis appliication").create("u");
		final Option optionLoction = OptionBuilder.withLongOpt("Location")
				.withDescription("Location of the report").create("l");
		final Option optionHelp = OptionBuilder.withLongOpt("help").create("h");

		OPTIONS.addOption(optionDriver);
		OPTIONS.addOption(optionNoNeoLoad);
		OPTIONS.addOption(optionURL);
		OPTIONS.addOption(optionLoction);
		OPTIONS.addOption(optionHelp);
	}

	public static void main(final String[] args) {

		final CommandLine commandLine;
		try {
			commandLine = new GnuParser().parse(OPTIONS, args);
		} catch (ParseException e) {
			new HelpFormatter().printHelp("CreateReport", OPTIONS, true);
			return;
		}

		if ((commandLine.hasOption("h"))) {
			new HelpFormatter().printHelp("CreateReport", OPTIONS, true);
		}

		switch (commandLine.getOptionValue(DRIVER)) {
		case FIREFOX:
			webdriver = new FirefoxDriver();
			break;
		case CHROME:
			final File file = new File(CHROME_DRIVER_PATH);
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			webdriver = new ChromeDriver();
			break;
		case PHANTOM_JS:
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setJavascriptEnabled(true);
			cap.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOM_JS_EXE_PATH);
			webdriver = new PhantomJSDriver(cap);
			break;

		}

		if(commandLine.hasOption("TestintUrl"))
			setBaseUrl(commandLine.getOptionValue("TestintUrl"));
		else
			setBaseUrl("http://cpv5.intranet.neotys.com/");
		
		if(commandLine.hasOption("Location"))
			UshahidiLocation=commandLine.getOptionValue("Location");
		else
			UshahidiLocation="Marseille";
		
		if (!commandLine.hasOption("no")) {
			// use web driver proxy to sent data to NeoLoad.
			driver = WebDriverProxy.newEueInstance(webdriver);
		}

		setUp();
		try {
			testUshahidi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@ClassRule
	public static   TestName testName = new TestName();
	

	
	
	
	  public static  void testUshahidi() throws Exception {
		 setUp();
		 driver.startTransaction("Home Page");

		 driver.get(getBaseUrl());
	    
		 driver.startTransaction("Submit a report");

	    driver.get(getBaseUrl()+"reports/submit");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		
	    driver.findElement(By.id("incident_title")).clear();
	    driver.findElement(By.id("incident_title")).sendKeys("Test selenium");
	    driver.findElement(By.id("incident_description")).clear();
	    driver.findElement(By.id("incident_description")).sendKeys("description");
	    
	    driver.findElement(By.cssSelector("input[type='checkbox'][value='2']")).click();
	    
	    driver.findElement(By.id("location_find")).clear();
	    driver.findElement(By.id("location_find")).sendKeys(UshahidiLocation);
	  
	    driver.findElement(By.id("button")).click();
	    
	    //driver.wait(2000);
	    Thread.sleep(2000);
	    driver.startTransaction("Submit");

	    driver.findElement(By.className("btn_submit")).click();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    driver.startTransaction("Report page");

	    driver.findElement(By.linkText("Return to the reports page")).click();
	    
	    
	    driver.close();
		
	    
	    
		
		
	  }
		public static String getBaseUrl() {
			if( baseUrl ==null)
					baseUrl="http://cpv5.intranet.neotys.com/";
			return baseUrl;
		}
		public static void  setBaseUrl(String baseUrl) {
			CreateReport.baseUrl = baseUrl;
		}
	public static  void setUp() {
		driver = NLWebDriverFactory.newNLWebDriver(webdriver, testName.getMethodName() );
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		UshahidiLocation="Marseille";
	}

	@After
    public  void afterTest() {
        if (driver != null) {
            driver.quit();
        }
	}
}
