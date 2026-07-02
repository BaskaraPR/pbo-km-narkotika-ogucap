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

    public int getInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = scanner.nextLine().trim();
            try {
                // Menggunakan parse untuk mencegah masalah buffer pada Scanner.nextInt()
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Input tidak valid! Harap masukkan angka bulat (integer).");
            }
        }
    }

    public double getDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Input tidak valid! Harap masukkan angka desimal (double).");
                System.out.println("Catatan: Gunakan tanda titik (.) untuk pecahan desimal.");
            }
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}