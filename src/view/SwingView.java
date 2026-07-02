package view;

import controller.KnowledgeController;
import model.Putusan;
import model.StatistikPutusan;
import util.InputHandler; // 1. Import InputHandler

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SwingView extends JFrame {

    private final KnowledgeController controller;
    private final InputHandler inputHandler; // 2. Deklarasi InputHandler
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    // Constructor TETAP SAMA persis seperti yang dipanggil oleh Main.java
    public SwingView(KnowledgeController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler(); // 3. Inisialisasi InputHandler di dalam View

        // Setup Frame Utama
        setTitle("Sistem Manajemen Pengetahuan Putusan Narkoba");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen

        initComponents();
        refreshTable(); // Load data awal saat aplikasi dibuka
    }

    private void initComponents() {
        // 1. Header
        JLabel header = new JLabel("KMS PERKARA NARKOTIKA", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        // 2. Tabel untuk menampilkan data
        String[] columns = {"No. Perkara", "Terdakwa", "Jenis Narkoba", "Pengadilan", "Vonis (Bln)", "Denda (Rp)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Panel Tombol (Bawah)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnTambah = new JButton("Tambah Putusan");
        JButton btnHapus = new JButton("Hapus Terpilih");
        JButton btnCariNama = new JButton("Cari Nama");
        JButton btnFilterJenis = new JButton("Filter Jenis");
        JButton btnStatistik = new JButton("Lihat Statistik");
        JButton btnRefresh = new JButton("Refresh Semua");

        Font btnFont = new Font("Arial", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnTambah, btnHapus, btnCariNama, btnFilterJenis, btnStatistik, btnRefresh}) {
            btn.setFont(btnFont);
            buttonPanel.add(btn);
        }

        // 4. Status Bar
        statusLabel = new JLabel("Status: Siap");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(statusLabel, BorderLayout.SOUTH);
        add(southContainer, BorderLayout.SOUTH);

        // 5. Action Listeners
        btnTambah.addActionListener(e -> actionTambah());
        btnHapus.addActionListener(e -> actionHapus());
        btnCariNama.addActionListener(e -> actionCariNama());
        btnFilterJenis.addActionListener(e -> actionFilterJenis());
        btnStatistik.addActionListener(e -> actionStatistik());
        btnRefresh.addActionListener(e -> refreshTable());
    }

    // --- ACTION METHODS ---

    private void actionTambah() {
        Putusan p = showAddForm();
        if (p != null) {
            controller.tambahPutusan(p);
            refreshTable();
            statusLabel.setText("Status: Putusan berhasil ditambahkan.");
        }
    }

    private void actionHapus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomorPerkara = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus putusan " + nomorPerkara + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusPutusan(nomorPerkara);
            refreshTable();
            statusLabel.setText("Status: Putusan " + nomorPerkara + " berhasil dihapus.");
        }
    }

    // PENGGUNAAN INPUTHANDLER DIMULAI DI SINI
    private void actionCariNama() {
        // Validasi dipindahkan ke InputHandler (getMandatoryString akan loop jika kosong)
        String nama = inputHandler.getMandatoryString("Masukkan Nama Terdakwa: ");

        ArrayList<Putusan> hasil = controller.getRepository().cariByNama(nama);
        updateTable(hasil, "Status: Menampilkan hasil pencarian nama '" + nama + "' (" + hasil.size() + " data)");
    }

    private void actionFilterJenis() {
        // Validasi dipindahkan ke InputHandler
        String jenis = inputHandler.getMandatoryString("Masukkan Jenis Narkotika: ");

        ArrayList<Putusan> hasil = controller.getRepository().filterByJenis(jenis);
        updateTable(hasil, "Status: Menampilkan filter jenis '" + jenis + "' (" + hasil.size() + " data)");
    }

    private void actionStatistik() {
        ArrayList<Putusan> semuaData = controller.getRepository().getDaftarSemua();
        if (semuaData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada data untuk ditampilkan statistiknya.");
            return;
        }

        StatistikPutusan stat = new StatistikPutusan(semuaData);
        String msg = String.format(
                "Total Putusan: %d\nRata-rata Vonis: %.2f bulan\nRata-rata Denda: Rp %.2f\nJenis Terbanyak: %s\n\nDistribusi Peran:\n%s",
                stat.getTotalPutusan(),
                stat.getRataRataVonis(),
                stat.getRataRataDenda(),
                stat.getJenisNarkotikaTerbanyak(),
                String.join("\n", stat.getDistribusiPeran())
        );

        JOptionPane.showMessageDialog(this, msg, "Laporan Statistik Putusan", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- HELPER METHODS ---

    private void refreshTable() {
        ArrayList<Putusan> data = controller.getRepository().getDaftarSemua();
        updateTable(data, "Status: Menampilkan semua data (" + data.size() + " record)");
    }

    private void updateTable(ArrayList<Putusan> data, String statusText) {
        tableModel.setRowCount(0);
        for (Putusan p : data) {
            tableModel.addRow(new Object[]{
                    p.getNomorPerkara(),
                    p.getNamaTerdakwa(),
                    p.getJenisNarkotika(),
                    p.getPengadilan(),
                    p.getVonisHukuman(),
                    p.getVonisDenda()
            });
        }
        statusLabel.setText(statusText);
    }

    // FORM INPUT SEKARANG MENGGUNAKAN INPUTHANDLER (MUNCUL DI CONSOLE)
    private Putusan showAddForm() {
        System.out.println("\n--- FORM INPUT PUTUSAN BARU (Via Console) ---");

        // Semua validasi (wajib isi, format angka) sekarang dihandle oleh loop di dalam InputHandler!
        // Kita tidak perlu lagi try-catch atau cek isEmpty() di sini.
        String nomor    = inputHandler.getMandatoryString("Nomor Perkara (*): ");
        String pengadilan = inputHandler.getString("Pengadilan: ");
        String tanggal  = inputHandler.getString("Tanggal Putusan: ");
        String nama     = inputHandler.getMandatoryString("Nama Terdakwa (*): ");

        int umur        = inputHandler.getInt("Umur Terdakwa: ");
        String jenis    = inputHandler.getString("Jenis Narkotika: ");
        double berat    = inputHandler.getDouble("Berat Barang Bukti: ");

        String pasal    = inputHandler.getString("Pasal Dilanggar: ");
        String peran    = inputHandler.getString("Peran Terdakwa: ");

        int vonis       = inputHandler.getInt("Vonis Hukuman (bln): ");
        double denda    = inputHandler.getDouble("Vonis Denda (Rp): ");
        String hakim    = inputHandler.getString("Nama Hakim: ");

        // Kembalikan objek Putusan yang sudah pasti valid karena divalidasi oleh InputHandler
        return new Putusan(nomor, pengadilan, tanggal, nama, umur, jenis, berat, pasal, peran, vonis, denda, hakim);
    }
}