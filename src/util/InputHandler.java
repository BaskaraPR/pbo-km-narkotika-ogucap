import java.util.Scanner;

public class InputHandler {

    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String getString(String pesan) {
        System.out.print(pesan);
        return scanner.nextLine().trim();
    }

    public String getMandatoryString(String pesan) {
        String input = "";
        while (true) {
            System.out.print(pesan);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            }
            System.out.println("Error: Input ini wajib diisi dan tidak boleh kosong!");
        }
        return input;
    }