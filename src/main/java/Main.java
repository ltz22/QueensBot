import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        QueensBot bot = null;
        try(Scanner scanner = new Scanner(System.in);) {
            bot = new QueensBot();
            bot.start();

            System.out.println("Press Enter to close the browser...");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if(bot != null){
                bot.close();
            }
        }
    }
}
