package util;

import javax.swing.JOptionPane;

public class InputHandler {

    public String getString(String pesan) {
        String input = JOptionPane.showInputDialog(null, pesan);
        return input == null ? "" : input.trim();
    }

    public String getMandatoryString(String pesan) {
        String input;
        while (true) {
            input = JOptionPane.showInputDialog(null, pesan);
            if (input == null) return "";
            if (!input.trim().isEmpty()) break;
            JOptionPane.showMessageDialog(null, "Input ini wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return input.trim();
    }

    public int getInt(String pesan) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, pesan);
            if (input == null) return -1;
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(null, "Input tidak valid! Harap masukkan angka bulat.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public double getDouble(String pesan) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, pesan);
            if (input == null) return -1.0;
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input tidak valid! Harap masukkan angka desimal.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /** Throws IllegalArgumentException with a message if blank. */
    public String validateMandatoryString(String fieldLabel, String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldLabel + " wajib diisi!");
        }
        return rawInput.trim();
    }

    public String validateOptionalString(String rawInput) {
        return rawInput == null ? "" : rawInput.trim();
    }

    public int validateInt(String fieldLabel, String rawInput) {
        try {
            return Integer.parseInt(rawInput.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldLabel + " harus berupa angka bulat!");
        }
    }

    public double validateDouble(String fieldLabel, String rawInput) {
        try {
            return Double.parseDouble(rawInput.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldLabel + " harus berupa angka desimal!");
        }
    }
}