import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Scanner;

public class QueensBot {
    private WebDriver driver;
    private WebDriverWait wait;

    public QueensBot() {
        // Don't initialize driver here anymore
    }

    public String chooseLevel(){
        while(true){
            System.out.println("Enter the queens level (1-504) you want to solve:");
            Scanner levelScanner = new Scanner(System.in);
            String level = levelScanner.nextLine();
            int levelNumber = Integer.parseInt(level);
            if(levelNumber <= 504 && levelNumber >= 1){
                return level;
            } else{
                System.out.println("Invalid level, enter a level between 1 and 504");
            }
        }
    }

    public void start(){
        String levelChosen = chooseLevel();

        System.out.println("Starting in 3 seconds...");
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        // Initialize driver only when starting
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://queensgame.vercel.app/community-level/" + levelChosen);

        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void close(){
        if(driver != null){
            driver.quit();
        }
    }
}
