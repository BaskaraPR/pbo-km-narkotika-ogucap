package view;

import controller.KnowledgeController;
import model.Putusan;
import model.StatistikPutusan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SwingView extends JFrame {

    private KnowledgeController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SwingView() {
        // Inisialisasi Controller
        this.controller = new KnowledgeController();

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
                return false; // Mencegah user mengedit tabel langsung
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

        // Styling tombol agar lebih enak dilihat
        Font btnFont = new Font("Arial", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnTambah, btnHapus, btnCariNama, btnFilterJenis, btnStatistik, btnRefresh}) {
            btn.setFont(btnFont);
            buttonPanel.add(btn);
        }

        // 4. Status Bar
        statusLabel = new JLabel("Status: Siap");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        // Menggabungkan Button Panel dan Status Bar
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(statusLabel, BorderLayout.SOUTH);
        add(southContainer, BorderLayout.SOUTH);

        // 5. Action Listeners (Menghubungkan tombol ke method)
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

    private void actionCariNama() {
        String nama = JOptionPane.showInputDialog(this, "Masukkan Nama Terdakwa:");
        if (nama != null && !nama.trim().isEmpty()) {
            // Mengambil data dari repository untuk ditampilkan di tabel
            ArrayList<Putusan> hasil = controller.getRepository().cariByNama(nama.trim());
            updateTable(hasil, "Status: Menampilkan hasil pencarian nama '" + nama + "' (" + hasil.size() + " data)");
        }
    }

    private void actionFilterJenis() {
        String jenis = JOptionPane.showInputDialog(this, "Masukkan Jenis Narkotika:");
        if (jenis != null && !jenis.trim().isEmpty()) {
            ArrayList<Putusan> hasil = controller.getRepository().filterByJenis(jenis.trim());
            updateTable(hasil, "Status: Menampilkan filter jenis '" + jenis + "' (" + hasil.size() + " data)");
        }
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
        tableModel.setRowCount(0); // Kosongkan tabel dulu
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

    // Form Dialog untuk Tambah Putusan (12 Input)
    private Putusan showAddForm() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));
        panel.setPreferredSize(new Dimension(400, 350));

        JTextField tfNomor = new JTextField();
        JTextField tfPengadilan = new JTextField();
        JTextField tfTanggal = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfUmur = new JTextField();
        JTextField tfJenis = new JTextField();
        JTextField tfBerat = new JTextField();
        JTextField tfPasal = new JTextField();
        JTextField tfPeran = new JTextField();
        JTextField tfVonis = new JTextField();
        JTextField tfDenda = new JTextField();
        JTextField tfHakim = new JTextField();

        panel.add(new JLabel("Nomor Perkara:")); panel.add(tfNomor);
        panel.add(new JLabel("Pengadilan:")); panel.add(tfPengadilan);
        panel.add(new JLabel("Tanggal Putusan:")); panel.add(tfTanggal);
        panel.add(new JLabel("Nama Terdakwa:")); panel.add(tfNama);
        panel.add(new JLabel("Umur Terdakwa:")); panel.add(tfUmur);
        panel.add(new JLabel("Jenis Narkotika:")); panel.add(tfJenis);
        panel.add(new JLabel("Berat Barang Bukti:")); panel.add(tfBerat);
        panel.add(new JLabel("Pasal Dilanggar:")); panel.add(tfPasal);
        panel.add(new JLabel("Peran Terdakwa:")); panel.add(tfPeran);
        panel.add(new JLabel("Vonis Hukuman (bln):")); panel.add(tfVonis);
        panel.add(new JLabel("Vonis Denda (Rp):")); panel.add(tfDenda);
        panel.add(new JLabel("Nama Hakim:")); panel.add(tfHakim);

        int result = JOptionPane.showConfirmDialog(this, panel, "Form Tambah Putusan Baru",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validasi input wajib (Exception Handling)
                if (tfNomor.getText().trim().isEmpty() || tfNama.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nomor Perkara dan Nama Terdakwa wajib diisi!", "Error Validasi", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                // Membuat objek Putusan
                return new Putusan(
                        tfNomor.getText().trim(),
                        tfPengadilan.getText().trim(),
                        tfTanggal.getText().trim(),
                        tfNama.getText().trim(),
                        Integer.parseInt(tfUmur.getText().trim()),
                        tfJenis.getText().trim(),
                        Double.parseDouble(tfBerat.getText().trim()),
                        tfPasal.getText().trim(),
                        tfPeran.getText().trim(),
                        Integer.parseInt(tfVonis.getText().trim()),
                        Double.parseDouble(tfDenda.getText().trim()),
                        tfHakim.getText().trim()
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Format angka tidak valid untuk Umur, Berat, Vonis, atau Denda!\nGunakan titik (.) untuk desimal.", "Error Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }
}