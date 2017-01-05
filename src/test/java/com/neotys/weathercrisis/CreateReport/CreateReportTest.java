package com.neotys.weathercrisis.CreateReport;

import java.io.File;
import java.util.concurrent.TimeUnit;
import static com.neotys.selenium.proxies.NLWebDriverFactory.addProxyCapabilitiesIfNecessary;
import org.junit.After;
import org.junit.Before;
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

public class CreateReportTest	 {
	private static final String CHROME_DRIVER_PATH = "C:\\Selenium\\chromedriver.exe";
	private static final String PHANTOM_JS_EXE_PATH = "/opt/phantomjs/bin/phantomjs";

	/*private static final Options OPTIONS;
	private static final String DRIVER = "driver";*/
	private static final String PHANTOM_JS = "PhantomJs";
	private static final String CHROME = "Chrome";
	private static final String FIREFOX = "FireFox";
	 private  String baseUrl;
		//  private boolean acceptNextAlert = true;
	 private  String UshahidiLocation;
	 static NLWebDriver driver;
	 static WebDriver webdriver;
	
	@Rule
    public    TestName testName = new TestName();

	
	@Before
    public  void before() {
        
        // projectPath used to open NeoLoad project, null to use currently opened Project.
        final String projectPath = "C:\\Users\\Administrator\\Documents\\NeoLoad Projects\\Global_Demo\\Global_Demo.nlp";
       // webdriver = new FirefoxDriver(addProxyCapabilitiesIfNecessary(new DesiredCapabilities()));
       /* final File file = new File(CHROME_DRIVER_PATH);
        * 
        * 
        
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		webdriver = new ChromeDriver(addProxyCapabilitiesIfNecessary(new DesiredCapabilities()));*/
        DesiredCapabilities cap = new DesiredCapabilities();
		cap.setJavascriptEnabled(true);
		cap.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOM_JS_EXE_PATH);
		webdriver = new PhantomJSDriver(addProxyCapabilitiesIfNecessary(cap));
        driver = NLWebDriverFactory.newNLWebDriver(webdriver, testName.getMethodName(), projectPath);
       
	}
	
	 @Test
	  public  void testUshahidi() throws Exception {
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
		public String getBaseUrl() {
			if( baseUrl ==null)
					baseUrl="http://cpv5.intranet.neotys.com/";
			return baseUrl;
		}
		public void  setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}
	public  void setUp() {
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
