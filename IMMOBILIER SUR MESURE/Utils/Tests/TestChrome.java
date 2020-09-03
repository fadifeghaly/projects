package Test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
public class TestChrome {
	public WebDriver driver;
	
	@BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\Programme\\Selenium\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

	@Test
	public void openApp() {
		driver.get("http://18.221.143.96/front/");
	}
	
	@Test
	public void movePageCritere() {
		try {
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);			
			driver.findElement(By.xpath("//img[@src='image/icons/down.png']")).click();
		}catch(NoSuchElementException e) {
			System.out.println("Erreur de déplacer la page critère! ");
		}

	}
	
	@Test
	public void selectCrimes() {
		driver.findElement(By.id("crimes")).click();
	}
	
	@Test
	public void setCrimeRange() throws InterruptedException {
//		WebElement slider = driver.findElement(By.id("crimeRange"));
//		Actions move = new Actions(driver);
//		Action action = move.dragAndDropBy(slider, -40, 0).build();
//		action.perform();	
		
		WebElement slider = driver.findElement(By.id("crimeRange")); 

	    Actions action= new Actions(driver);
	    action.click(slider).build().perform();
	    Thread.sleep(1000);
	    for (int i = 0; i < 2; i++) 
	    {
	        action.sendKeys(Keys.ARROW_RIGHT).build().perform();
	        Thread.sleep(200);
	    }
	}
	
	@Test
	public void selectCrimeRange() {
		driver.findElement(By.id("crimeRange")).click();
	}
	
	@Test
	public void selectEcoles() {
		driver.findElement(By.id("ecoles")).click();
	}
	
	@Test
	public void setEcolesRange() {
		WebElement slider = driver.findElement(By.id("ecolesRange"));
		Actions move = new Actions(driver);
		Action action = move.dragAndDropBy(slider, 40, 0).build();
		action.perform();		
	}
	
	@Test
	public void selectEcolesRange() {
		driver.findElement(By.id("ecolesRange")).click();
	}
	
	@Test
	public void selectStations() {
		driver.findElement(By.id("stationsMetro")).click();
	}
	
	@Test
	public void setStationRange() {
		WebElement slider = driver.findElement(By.id("stationRange"));
		Actions move = new Actions(driver);
		Action action = move.dragAndDropBy(slider, 60, 0).build();
		action.perform();		
	}
	
	@Test
	public void selectStationRange() {
		driver.findElement(By.id("stationRange")).click();
	}
	
	@Test
	public void selectPunaises() {
		driver.findElement(By.id("punaises")).click();
	}
	
	@Test
	public void setPunaisesRange() {
		WebElement slider = driver.findElement(By.id("punaisesRange"));
		Actions move = new Actions(driver);
		Action action = move.dragAndDropBy(slider, -60, 0).build();
		action.perform();		
	}
	
	@Test
	public void selectPunaisesRange() {
		driver.findElement(By.id("punaisesRange")).click();
	}
	
	@Test
	public void selectAdress() {
		driver.findElement(By.id("adresse")).click();
		driver.findElement(By.id("adresse")).sendKeys("1861 Saint-Catherine Street West, Montreal, QC, Canada");
	}
	
	@Test
	public void submitFormule() {
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Test
	public void listChaqueScore() {
		try {
			driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Voici le score de chaque critère'])[1]/following::img[1]")).click();
		}catch(NoSuchElementException e) {
			System.out.println("Erreur de afficher la liste de chaque score! ");
		}
	}
	
	@Test
	public void returnApp() {
		try {
			driver.findElement(By.xpath("//a[@href='../../index.html']")).click();
		}catch(NoSuchElementException e) {
			System.out.println("Erreur de retouner la page principale! ");
		}
	}
	
	@AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }
}
