import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class QueensBot {
    private WebDriver driver;
    private WebDriverWait wait;
    private Board board;

    //public QueensBot() {
    //}

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

        // Parse the board
        parseBoard();

        // Print board info
        System.out.println("Board parsed successfully!");
        System.out.println("Board size: " + board.getSize() + "x" + board.getSize());
        System.out.println("Number of color regions: " + board.getRegions().size());

        QueensSolver solver = new QueensSolver(board);
        solver.solve();
    }

    public Board getBoard(){
        return this.board;
    }

    public void parseBoard() {
        // Find all square elements
        List<WebElement> squares = driver.findElements(By.cssSelector("div.square"));

        if (squares.isEmpty()) {
            throw new RuntimeException("No squares found");
        }

        // Determine board size
        int maxRow = 0;
        int maxCol = 0;

        for (WebElement square : squares) {
            String rowStr = square.getAttribute("data-row");
            String colStr = square.getAttribute("data-col");

            if (rowStr != null && colStr != null) {
                int row = Integer.parseInt(rowStr);
                int col = Integer.parseInt(colStr);
                maxRow = Math.max(maxRow, row);
                maxCol = Math.max(maxCol, col);
            }
        }

        int boardSize = maxRow + 1;
        board = new Board(boardSize);

        // Parse each square
        for (WebElement square : squares) {
            String rowStr = square.getAttribute("data-row");
            String colStr = square.getAttribute("data-col");
            String style = square.getAttribute("style");

            if (rowStr == null || colStr == null || style == null) {
                continue;
            }

            int row = Integer.parseInt(rowStr);
            int col = Integer.parseInt(colStr);

            // Extract RGB color from style attribute
            String color = extractColor(style);

            if (color != null) {
                board.setCell(row, col, color);
            }
        }

        System.out.println("Parsed " + squares.size() + " squares");
    }

    public String extractColor(String style) {
        int rgbStart = style.indexOf("rgb(");
        if (rgbStart == -1) {
            return null;
        }

        int rgbEnd = style.indexOf(")", rgbStart);
        if (rgbEnd == -1) {
            return null;
        }

        // Return the full rgb(...) string
        return style.substring(rgbStart, rgbEnd + 1);
    }

    public void close(){
        if(driver != null){
            driver.quit();
        }
    }
}
