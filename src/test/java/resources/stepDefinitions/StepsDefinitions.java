package resources.stepDefinitions;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Verify;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import resources.CommonUtilities.UIUtilities.ObjectMapManager;

public class StepsDefinitions {
	private WebDriver driver;
	private String baseUrl = "https://apnews.com/";
	boolean notInvokedByAnotherMethod = true;
	String author = "None";
	String articleLink = "None";
	String dateTimePosted = "None";
	String imageRelatedPath = "None";
	String workingDir=System.getProperty("user.dir");
	int pageTimeOut=1,driverWait=5,pageLoadWait=1000;
	public ObjectMapManager objmap = new ObjectMapManager(workingDir+"\\src\\test\\resources\\WebElementsMaps\\CommonWebElements.properties");


	@Given("^I Open the browser$")
	public void openBrowser() throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver_win32.2.41.exe");
		driver = new ChromeDriver();
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(pageTimeOut, TimeUnit.SECONDS);
		System.out.println("baseUrl "+baseUrl);
	}

	@Given("^I Close the browser$")
	public void closeBrowser() throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		driver.quit();
	}

	@Given("^I Validate that the page at least displayed the item \"([^\"]*)\"$")
	public void validateAtLeastWebElemetXDisplayed(String $arg1) throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		driver.manage().timeouts().pageLoadTimeout(pageTimeOut, TimeUnit.SECONDS);
		try {
			WebElement MainWebElementDisplayed = driver.findElement(objmap.getLocator($arg1));
		} catch (Exception e) {
			Verify.verify(false, "An exception occurred", "Exception = "+e);
			e.printStackTrace();
			driver.quit();					
		}
	}

	@Given("^I Validate that Page Title is \"([^\"]*)\"$")
	public void validatePageTile(String $arg1) throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		driver.manage().timeouts().pageLoadTimeout(pageTimeOut, TimeUnit.SECONDS);
		try {	
			Verify.verify(driver.getTitle().matches($arg1));
			System.out.println("Current Title: "+driver.getTitle());					
		} catch (Exception e) {
			Verify.verify(false, "An exception occurred", "Exception = "+e);	
			e.printStackTrace();
			driver.quit();	
		}
	}

	@When("^I Close popup window if it appears$")
	public void closePopupWndowIfAppears() throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		boolean status = false;
		try {	
			driver.manage().window().maximize();
			driver.switchTo().activeElement();
			WebDriverWait wait = new WebDriverWait(driver, driverWait);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("sailthru-overlay-close")));
			WebElement MainWebElementDisplayed = driver.findElement(By.className("sailthru-overlay-close"));
			status = MainWebElementDisplayed.isEnabled();	
			if (!status) {
				driver.switchTo().activeElement();
				MainWebElementDisplayed = driver.findElement(By.className("sailthru-overlay-close"));
			}
			if (status) {			
				MainWebElementDisplayed.click();
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}

	@When("^I Added the search text \"([^\"]*)\"$")
	public List <WebElement> enterSearchText(String $arg1) throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		WebElement mainWebElementDisplayed =null;
		List <WebElement> resultList =null;
		try {
			driver.manage().window().maximize();
			WebDriverWait wait = new WebDriverWait(driver, driverWait);
			driver.manage().timeouts().pageLoadTimeout(pageTimeOut, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("search.button")));
			driver.findElement(objmap.getLocator("search.button")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("search.field")));
			mainWebElementDisplayed = driver.findElement(objmap.getLocator("search.field"));
			mainWebElementDisplayed.clear();
			mainWebElementDisplayed.sendKeys($arg1);
			wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("search.results")));
			resultList = driver.findElements(objmap.getLocator("search.results"));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@When("^I Gather data each page result that regards to \"([^\"]*)\"$")
	public void navigateToPageResults(String $arg1) throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		List <WebElement> resultList =enterSearchText($arg1);
		WebDriverWait wait = new WebDriverWait(driver, driverWait);
		if (!resultList.isEmpty()) {
			int i =1;
			for (WebElement element:resultList) {
				try {						
					System.out.println("Current element text: "+element.getText().toString());
					System.out.println("I for enter text: "+i);
					element.click();	
					wait.until(ExpectedConditions.urlContains($arg1));
					writeDownData();
					if (i<resultList.size())
						enterSearchText($arg1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				++i;
			}
			try {
				File htmlFile = new File(workingDir+"\\ResultsDashboard\\MediaExecutionEvidences\\FinalList.html");
				Desktop.getDesktop().browse(htmlFile.toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}			
	}

	@Then("^I Write down the data$")
	public void writeDownData() throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());	
		driver.manage().timeouts().pageLoadTimeout(pageTimeOut, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, driverWait);
		List <WebElement> authors = null,dateTimePosted = null,articleLink = null;
		File file = new File(workingDir+"\\ResultsDashboard\\MediaExecutionEvidences\\FinalList.html");
		//if (!file.exists()) {
		file.createNewFile();
		//}
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);	
		bw.write("<html>");  
		bw.write("<head>"); 
		bw.write("<title>");  
		bw.write("CodeChallenge");
		bw.write("</title>");  
		bw.write("</head>");
		bw.write("<body>");
		List<List<String>> dataToPrint = new ArrayList<List<String>>();
		boolean pageNotFoundError = false;
		for(int i=0; i < 4; i++) {	
			dataToPrint.add(new ArrayList<String>());
		}
		dataToPrint.get(0).add("Author");
		dataToPrint.get(1).add("dateTimePosted");
		dataToPrint.get(2).add("articleLink");
		dataToPrint.get(3).add("imageLink");	
		try {
			waitPageLoad();	
			WebElement pageNotFound = driver.findElement(By.xpath("//*[contains(text(), 'PAGE NOT FOUND')]"));
			if (pageNotFound!=null)
				pageNotFoundError=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {	
			if (!pageNotFoundError) {
				wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("author.results")));
				wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("datePost.results")));
				wait.until(ExpectedConditions.presenceOfElementLocated(objmap.getLocator("articleLink.results")));	
			};
			authors = driver.findElements(objmap.getLocator("author.results"));
			dateTimePosted = driver.findElements(objmap.getLocator("datePost.results"));
			articleLink = driver.findElements(objmap.getLocator("articleLink.results"));				
			int[] arraySizes = {authors.size(), dateTimePosted.size(), articleLink.size()};
			Arrays.sort(arraySizes);
			try {
				if (authors.size()>0) {
					for (WebElement element:authors) {
						try {
							this.author = element.getText();
							dataToPrint.get(0).add(this.author);
						} catch (Exception e) {
							System.out.println("Current element author text: "+this.author);	
							System.out.println("e: "+e.toString());
							e.printStackTrace();
						} 
					}
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (dateTimePosted.size()>0) {
					for (WebElement element:dateTimePosted) {
						try {
							this.dateTimePosted = element.getText();
							dataToPrint.get(1).add(this.dateTimePosted);
						} catch (Exception e) {
							System.out.println("Current element dateTimePosted text: "+this.dateTimePosted);
							System.out.println("e: "+e.toString());
							e.printStackTrace();
						} 
					}
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}			

			try {
				if (articleLink.size()>0) {
					int imageIndex =0;
					for (WebElement element:articleLink) {
						try {
							this.articleLink = element.getAttribute("href");
							dataToPrint.get(2).add(this.articleLink);
							List<WebElement> images = driver.findElements(objmap.getLocator("image.results"));						
							if (images.get(imageIndex).getAttribute("src").length()>0)
								this.imageRelatedPath = images.get(imageIndex).getAttribute("src");
							else if (images.get(0).getAttribute("src").length()>0)
								this.imageRelatedPath = images.get(0).getAttribute("src");					
							else{
								this.imageRelatedPath = "NA";
							}
							imageRelatedPath= imageRelatedPath.replace("http", "<img src=\"http");
							imageRelatedPath= imageRelatedPath.replace(".jpeg", ".jpeg\" style=\"width: 200px;\"/>");	
							dataToPrint.get(3).add(this.imageRelatedPath);
						} catch (Exception e) {
							System.out.println("Current element articleLink text: "+this.articleLink);	
							System.out.println("e: "+e.toString());
							e.printStackTrace();
						} 
					}
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}

			int maxArraySizes = arraySizes[2];
			String authorsCol="", dateTimePostedCol="", articleLinkCol="", imageLinkCol="";
			for (int j = 0; j <=maxArraySizes; j++) {
				try {
					if (dataToPrint.get(0).get(j).length()<=0) 
						authorsCol = "NA";
					else authorsCol = dataToPrint.get(0).get(j);
					if (dataToPrint.get(1).get(j).length()<=0) 
						dateTimePostedCol = "NA";
					else dateTimePostedCol = dataToPrint.get(1).get(j);		        
					if (dataToPrint.get(2).get(j).length()<=0) 
						articleLinkCol = "NA";
					else articleLinkCol = dataToPrint.get(2).get(j);	
					if (dataToPrint.get(3).get(j).length()<=0) 
						imageLinkCol = "NA";
					else imageLinkCol = dataToPrint.get(3).get(j);	
					bw.write(" | "+authorsCol+" | "+dateTimePostedCol+" | "+articleLinkCol+" | "+imageLinkCol+" |<br>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("e: "+e.toString());
			e.printStackTrace();
		}
		bw.write("</body>");
		bw.write("</html>");
		bw.flush();
		bw.close();
	}

	@When("^I Wait until the page finishes loading$")
	public void waitPageLoad() throws Throwable {
		System.out.println("Running step: "+new Object(){}.getClass().getEnclosingMethod().getName());
		JavascriptExecutor j = (JavascriptExecutor)driver;
		try {
			while (!j.executeScript("return document.readyState").toString().equals("complete")){
				System.out.println("Page loading...");
				Thread.sleep(pageLoadWait);		    	 
			}
			System.out.println("Page loaded properly.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
