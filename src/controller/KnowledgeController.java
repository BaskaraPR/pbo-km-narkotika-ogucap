package controller;

import model.KnowledgeRepository;
import model.Putusan;
import model.StatistikPutusan;

import java.util.ArrayList;

public class KnowledgeController {

    private final KnowledgeRepository repository;

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

    public ArrayList<Putusan> tampilkanSemuaPutusan() {
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
        return data;
    }

    public Putusan cariDetailByNomor(String nomor) {
        return repository.cariByNomor(nomor);
    }

    public ArrayList<Putusan> cariByNama(String nama) {
        ArrayList<Putusan> hasil = repository.cariByNama(nama);
        tampilkanHasilList(hasil, "PENCARIAN NAMA TERDAKWA: " + nama);
        return hasil;
    }

    public ArrayList<Putusan> filterByJenisNarkotika(String jenis) {
        ArrayList<Putusan> hasil = repository.filterByJenis(jenis);
        tampilkanHasilList(hasil, "FILTER JENIS NARKOTIKA: " + jenis);
        return hasil;
    }

    public ArrayList<Putusan> filterByPengadilan(String pengadilan) {
        ArrayList<Putusan> hasil = repository.filterByPengadilan(pengadilan);
        tampilkanHasilList(hasil, "FILTER PENGADILAN: " + pengadilan);
        return hasil;
    }

    public void hapusPutusan(String nomor) {
        boolean isDeleted = repository.hapus(nomor);

        if (isDeleted) {
            System.out.println("Sukses: Putusan dengan Nomor Perkara '" + nomor + "' berhasil dihapus dari sistem.");
        } else {
            System.out.println("Gagal: Putusan dengan Nomor Perkara '" + nomor + "' tidak ditemukan sehingga tidak dapat dihapus.");
        }
    }

    public ArrayList<Putusan> tampilkanLaporanStatistik() {
        ArrayList<Putusan> semuaData = repository.getDaftarSemua();
        StatistikPutusan statistik = new StatistikPutusan(semuaData);

        statistik.tampilkanLaporan();
        return semuaData;
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

}