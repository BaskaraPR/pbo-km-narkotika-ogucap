package view;

import controller.KnowledgeController;
import model.Putusan;
import model.StatistikPutusan;
import util.InputHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SwingView extends JFrame {

    private final KnowledgeController controller;
    private final InputHandler inputHandler;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SwingView(KnowledgeController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler();

        setTitle("Sistem Manajemen Pengetahuan Putusan Narkoba");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        JLabel header = new JLabel("KMS PERKARA NARKOTIKA", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

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
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        JButton btnTambah = new JButton("Tambah Putusan");
        JButton btnHapus = new JButton("Hapus Terpilih");
        JButton btnCariNama = new JButton("Cari Nama");
        JButton btnFilterJenis = new JButton("Filter Jenis");
        JButton btnFilterPengadilan = new JButton("Filter Pengadilan");
        JButton btnDetail = new JButton("Detail");
        JButton btnStatistik = new JButton("Lihat Statistik");
        JButton btnRefresh = new JButton("Refresh Semua");
        JButton btnByVonis = new JButton("Urutkan Vonis");

        Font btnFont = new Font("Arial", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnTambah, btnHapus, btnCariNama, btnFilterJenis, btnFilterPengadilan, btnDetail, btnStatistik, btnRefresh, btnByVonis}) {
            btn.setFont(btnFont);
            buttonPanel.add(btn);
        }

        statusLabel = new JLabel("Status: Siap");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(statusLabel, BorderLayout.SOUTH);
        add(southContainer, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> actionTambah());
        btnHapus.addActionListener(e -> actionHapus());
        btnCariNama.addActionListener(e -> actionCariNama());
        btnFilterJenis.addActionListener(e -> actionFilterJenis());
        btnFilterPengadilan.addActionListener(e -> actionFilterPengadilan());
        btnDetail.addActionListener(e -> actionDetailByNomor());
        btnStatistik.addActionListener(e -> actionStatistik());
        btnRefresh.addActionListener(e -> refreshTable());
        btnByVonis.addActionListener(e -> actionUrutVonis());
    }


    private void actionTambah() {
        Putusan p = showAddForm();
        if (p != null) {
            controller.tambahPutusan(p);
            refreshTable();
            statusLabel.setText("Status: Putusan berhasil ditambahkan.");
        }
    }
    private void actionUrutVonis() {
        ArrayList<Putusan> hasil = controller.urutkanByVonis();
        updateTable(hasil, "Status: Menampilkan data diurutkan berdasarkan Vonis Hukuman.");
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
        String nama = inputHandler.getMandatoryString("Masukkan Nama Terdakwa: ");
        ArrayList<Putusan> hasil = controller.cariByNama(nama);
        updateTable(hasil, "Status: Menampilkan hasil pencarian nama '" + nama + "' (" + hasil.size() + " data)");
    }

    private void actionFilterJenis() {
        String jenis = inputHandler.getMandatoryString("Masukkan Jenis Narkotika: ");
        ArrayList<Putusan> hasil = controller.filterByJenisNarkotika(jenis);
        updateTable(hasil, "Status: Menampilkan filter jenis '" + jenis + "' (" + hasil.size() + " data)");
    }

    private void actionFilterPengadilan() {
        String pengadilan = inputHandler.getMandatoryString("Masukkan Jenis Pengadilan: ");
        ArrayList<Putusan> hasil = controller.filterByPengadilan(pengadilan);
        updateTable(hasil, "Status: Menampilkan filter pengadilan '" + pengadilan + "' (" + hasil.size() + " data)");
    }

    private void actionDetailByNomor() {
        String nomor = inputHandler.getMandatoryString("Masukkan Nomor Perkara:");

        Putusan hasil = controller.cariDetailByNomor(nomor);

        System.out.println(hasil.toString());

        if (hasil != null) {
            String detail = String.format("""
                Nomor Perkara   : %s
                Pengadilan      : %s
                Tanggal Putusan : %s

                Nama Terdakwa   : %s
                Umur            : %d tahun

                Jenis Narkotika : %s
                Berat Barang Bukti : %.2f gram

                Pasal Dilanggar : %s
                Peran Terdakwa  : %s

                Vonis Penjara   : %d tahun
                Vonis Denda     : Rp%,.0f

                Nama Hakim      : %s
                """,
                    hasil.getNomorPerkara(),
                    hasil.getPengadilan(),
                    hasil.getTanggalPutusan(),
                    hasil.getNamaTerdakwa(),
                    hasil.getUmurTerdakwa(),
                    hasil.getJenisNarkotika(),
                    hasil.getBeratBarangBukti(),
                    hasil.getPasalDilanggar(),
                    hasil.getPeranTerdakwa(),
                    hasil.getVonisHukuman(),
                    hasil.getVonisDenda(),
                    hasil.getNamaHakim()
            );
            JOptionPane.showMessageDialog(
                    this,
                    detail,
                    "Detail Putusan",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Putusan dengan Nomor Perkara '" + nomor + "' tidak ditemukan.",
                    "Pencarian",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    private void actionStatistik() {
        ArrayList<Putusan> semuaData = controller.tampilkanLaporanStatistik();
        if (semuaData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada data untuk ditampilkan statistiknya.");
            return;
        }

        StatistikPutusan stat = new StatistikPutusan(semuaData);

        showStatistikDialog(stat);
    }

    private void showStatistikDialog(StatistikPutusan stat) {
        JDialog dialog = new JDialog(this, "Laporan Statistik Putusan", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        String msg = String.format(
                "Total Putusan          : %d\n" +
                        "Rata-rata Vonis        : %.2f bulan\n" +
                        "Rata-rata Denda        : Rp %.2f\n" +
                        "Jenis Narkotika Terbanyak : %s\n\n" +
                        "Distribusi Peran:\n%s",
                stat.getTotalPutusan(),
                stat.getRataRataVonis(),
                stat.getRataRataDenda(),
                stat.getJenisNarkotikaTerbanyak(),
                String.join("\n", stat.getDistribusiPeran())
        );

        JTextArea textArea = new JTextArea(msg);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnEkspor = new JButton("Ekspor");
        JButton btnTutup = new JButton("Tutup");

        btnEkspor.setFont(new Font("Arial", Font.PLAIN, 13));
        btnTutup.setFont(new Font("Arial", Font.PLAIN, 13));
        btnEkspor.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTutup.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEkspor.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Fitur Ekspor akan segera hadir!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnTutup.addActionListener(e -> dialog.dispose());

        rightPanel.add(btnEkspor);
        rightPanel.add(Box.createVerticalStrut(10)); // Jarak antar tombol
        rightPanel.add(btnTutup);

        dialog.add(rightPanel, BorderLayout.EAST);


        dialog.setVisible(true);
    }


    private void refreshTable() {
        ArrayList<Putusan> data = controller.tampilkanSemuaPutusan();
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
    private Putusan showAddForm() {
        JTextField nomorField      = new JTextField();
        JTextField pengadilanField = new JTextField();
        JTextField tanggalField    = new JTextField();
        JTextField namaField       = new JTextField();
        JTextField umurField       = new JTextField();
        JTextField jenisField      = new JTextField();
        JTextField beratField      = new JTextField();
        JTextField pasalField      = new JTextField();
        JTextField peranField      = new JTextField();
        JTextField vonisField      = new JTextField();
        JTextField dendaField      = new JTextField();
        JTextField hakimField      = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Nomor Perkara (*):"));   panel.add(nomorField);
        panel.add(new JLabel("Pengadilan:"));           panel.add(pengadilanField);
        panel.add(new JLabel("Tanggal Putusan:"));      panel.add(tanggalField);
        panel.add(new JLabel("Nama Terdakwa (*):"));    panel.add(namaField);
        panel.add(new JLabel("Umur Terdakwa:"));        panel.add(umurField);
        panel.add(new JLabel("Jenis Narkotika:"));      panel.add(jenisField);
        panel.add(new JLabel("Berat Barang Bukti:"));   panel.add(beratField);
        panel.add(new JLabel("Pasal Dilanggar:"));      panel.add(pasalField);
        panel.add(new JLabel("Peran Terdakwa:"));       panel.add(peranField);
        panel.add(new JLabel("Vonis Hukuman (bln):"));  panel.add(vonisField);
        panel.add(new JLabel("Vonis Denda (Rp):"));     panel.add(dendaField);
        panel.add(new JLabel("Nama Hakim:"));           panel.add(hakimField);


        while (true) {
            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Form Input Putusan Baru", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            try {
                String nomor      = inputHandler.validateMandatoryString("Nomor Perkara", nomorField.getText());
                String pengadilan = inputHandler.validateOptionalString(pengadilanField.getText());
                String tanggal    = inputHandler.validateOptionalString(tanggalField.getText());
                String nama       = inputHandler.validateMandatoryString("Nama Terdakwa", namaField.getText());
                int umur          = inputHandler.validateInt("Umur Terdakwa", umurField.getText());
                String jenis      = inputHandler.validateOptionalString(jenisField.getText());
                double berat      = inputHandler.validateDouble("Berat Barang Bukti", beratField.getText());
                String pasal      = inputHandler.validateOptionalString(pasalField.getText());
                String peran      = inputHandler.validateOptionalString(peranField.getText());
                int vonis         = inputHandler.validateInt("Vonis Hukuman", vonisField.getText());
                double denda      = inputHandler.validateDouble("Vonis Denda", dendaField.getText());
                String hakim      = inputHandler.validateOptionalString(hakimField.getText());

                return new Putusan(nomor, pengadilan, tanggal, nama, umur, jenis, berat, pasal, peran, vonis, denda, hakim);

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    }