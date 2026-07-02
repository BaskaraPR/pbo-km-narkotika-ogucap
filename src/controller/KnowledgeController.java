package controller;

import model.KnowledgeRepository;
import model.Putusan;
import model.StatistikPutusan;

import java.util.ArrayList;

public class KnowledgeController {

    private KnowledgeRepository repository;


    public KnowledgeController() {
        this.repository = new KnowledgeRepository();
    }

    public KnowledgeController(KnowledgeRepository repository) {
        this.repository = repository;
    }

    public void tambahPutusan(Putusan putusan) {
        if (putusan != null) {
            repository.simpan(putusan);
            System.out.println("Sukses: Putusan dengan nomor perkara '" + putusan.getNomorPerkara() + "' berhasil disimpan.");
        } else {
            System.out.println("Error: Gagal menyimpan. Data putusan tidak valid (null).");
        }
    }

    public void tampilkanSemuaPutusan() {
        ArrayList<Putusan> data = repository.getDaftarSemua();

        if (data.isEmpty()) {
            System.out.println("\nInformasi: Belum ada data putusan di dalam sistem.");
        } else {
            System.out.println("\n--- DAFTAR SELURUH PUTUSAN ---");
            for (Putusan p : data) {
                p.tampilkan(false); // Menggunakan tampilan ringkas
            }
            System.out.println("Total Data Tersimpan: " + repository.getTotalData());
        }
    }

    public void cariDetailByNomor(String nomor) {
        Putusan p = repository.cariByNomor(nomor);

        if (p != null) {
            System.out.println("\n--- DETAIL PENCARIAN PUTUSAN ---");
            p.tampilkan(true); // Memanggil method tampilkan(true) untuk output detail/toString
        } else {
            System.out.println("Peringatan: Putusan dengan Nomor Perkara '" + nomor + "' tidak ditemukan.");
        }
    }

    public void cariByNama(String nama) {
        ArrayList<Putusan> hasil = repository.cariByNama(nama);
        tampilkanHasilList(hasil, "PENCARIAN NAMA TERDAKWA: " + nama);
    }

    public void filterByJenisNarkotika(String jenis) {
        ArrayList<Putusan> hasil = repository.filterByJenis(jenis);
        tampilkanHasilList(hasil, "FILTER JENIS NARKOTIKA: " + jenis);
    }

    public void filterByPengadilan(String pengadilan) {
        ArrayList<Putusan> hasil = repository.filterByPengadilan(pengadilan);
        tampilkanHasilList(hasil, "FILTER PENGADILAN: " + pengadilan);
    }

    public void hapusPutusan(String nomor) {
        boolean isDeleted = repository.hapus(nomor);

        if (isDeleted) {
            System.out.println("Sukses: Putusan dengan Nomor Perkara '" + nomor + "' berhasil dihapus dari sistem.");
        } else {
            System.out.println("Gagal: Putusan dengan Nomor Perkara '" + nomor + "' tidak ditemukan sehingga tidak dapat dihapus.");
        }
    }

    public void tampilkanLaporanStatistik() {
        ArrayList<Putusan> semuaData = repository.getDaftarSemua();
        StatistikPutusan statistik = new StatistikPutusan(semuaData);

        statistik.tampilkanLaporan();
    }

    private void tampilkanHasilList(ArrayList<Putusan> daftar, String konteks) {
        System.out.println("\n--- " + konteks.toUpperCase() + " ---");

        if (daftar.isEmpty()) {
            System.out.println("Tidak ada putusan yang sesuai kriteria pencarian/filter.");
        } else {
            for (Putusan p : daftar) {
                p.tampilkan(false);
            }
            System.out.println("Jumlah hasil ditemukan: " + daftar.size());
        }
    }

    public KnowledgeRepository getRepository() {
        return repository;
    }
}