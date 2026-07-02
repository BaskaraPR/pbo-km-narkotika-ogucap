import javax.swing.JOptionPane;

public class InputHandler {

    public String getString(String pesan) {
        String input = JOptionPane.showInputDialog(null, pesan);
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    public String getMandatoryString(String pesan) {
        String input = "";
        while (true) {
            input = JOptionPane.showInputDialog(null, pesan);
            if (input == null) {
                return "";
            }
            if (!input.trim().isEmpty()) {
                break;
            }
            JOptionPane.showMessageDialog(null, "Input ini wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return input.trim();
    }

    public int getInt(String pesan) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, pesan);
            if (input == null) {
                return -1;
            }
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
            if (input == null) {
                return -1.0;
            }
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input tidak valid! Harap masukkan angka desimal.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


