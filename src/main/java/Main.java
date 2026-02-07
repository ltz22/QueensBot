import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        QueensBot bot = null;
        try(Scanner scanner = new Scanner(System.in);) {
            System.out.println("\nSwitch to the browser window.");
            System.out.println("Press Enter when ready to start...");
            scanner.nextLine();
            bot = new QueensBot();
            bot.start();

            //System.out.println("Starting in 3 seconds...");
            //Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if(bot != null){
                bot.close();
            }
        }
    }
}
