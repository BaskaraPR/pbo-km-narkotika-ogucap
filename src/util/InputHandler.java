package util;

import java.util.Scanner;

public class InputHandler {

    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String getString(String pesan) {
        System.out.print(pesan);
        return scanner.nextLine().trim();
    }}