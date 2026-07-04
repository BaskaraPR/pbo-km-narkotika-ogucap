package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatistikPutusan {

    private int totalPutusan;
    private double rataRataVonis;
    private double rataRataDenda;
    private String jenisNarkotikaTerbanyak;
    private String[] distribusiPeran;

    public StatistikPutusan(ArrayList<Putusan> daftar) {
        hitungSemua(daftar);
    }

    public void hitungSemua(ArrayList<Putusan> daftar) {

        totalPutusan = daftar.size();

        if (totalPutusan == 0) {
            rataRataVonis = 0;
            rataRataDenda = 0;
            jenisNarkotikaTerbanyak = "-";
            distribusiPeran = new String[0];
            return;
        }

        double totalVonis = 0;
        double totalDenda = 0;

        HashMap<String, Integer> jenisMap = new HashMap<>();
        HashMap<String, Integer> peranMap = new HashMap<>();

        for (Putusan p : daftar) {

            totalVonis += p.getVonisHukuman();
            totalDenda += p.getVonisDenda();

            String jenis = p.getJenisNarkotika();
            jenisMap.put(jenis, jenisMap.getOrDefault(jenis, 0) + 1);

            String peran = p.getPeranTerdakwa();
            peranMap.put(peran, peranMap.getOrDefault(peran, 0) + 1);
        }

        rataRataVonis = totalVonis / totalPutusan;
        rataRataDenda = totalDenda / totalPutusan;

        int max = 0;
        jenisNarkotikaTerbanyak = "-";

        for (Map.Entry<String, Integer> entry : jenisMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                jenisNarkotikaTerbanyak = entry.getKey();
            }
        }

        distribusiPeran = new String[peranMap.size()];

        int i = 0;
        for (Map.Entry<String, Integer> entry : peranMap.entrySet()) {
            distribusiPeran[i++] =
                    entry.getKey() + " : " + entry.getValue();
        }
    }


    public void tampilkanLaporan() {

        System.out.println("\n===== STATISTIK PUTUSAN =====");
        System.out.println("Total Putusan          : " + totalPutusan);
        System.out.printf("Rata-rata Vonis        : %.2f bulan%n", rataRataVonis);
        System.out.printf("Rata-rata Denda        : %.2f%n", rataRataDenda);
        System.out.println("Jenis Terbanyak        : " + jenisNarkotikaTerbanyak);

        System.out.println("\nDistribusi Peran:");

        for (String s : distribusiPeran) {
            System.out.println("- " + s);
        }
    }

    public int getTotalPutusan() {
        return totalPutusan;
    }

    public double getRataRataVonis() {
        return rataRataVonis;
    }

    public double getRataRataDenda() {
        return rataRataDenda;
    }

    public String getJenisNarkotikaTerbanyak() {
        return jenisNarkotikaTerbanyak;
    }

    public String[] getDistribusiPeran() {
        return distribusiPeran;
    }
}