package model;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class KnowledgeRepository {

    public ArrayList<Putusan> getDaftarUrutVonis() {
        ArrayList<Putusan> sortedList = new ArrayList<>(daftarPutusan);

        sortedList.sort(java.util.Comparator.comparingInt(Putusan::getVonisHukuman));

        return sortedList;
    }

    private final ArrayList<Putusan> daftarPutusan;

    
    public KnowledgeRepository() {
        daftarPutusan = new ArrayList<>();
        loadInitialData();
    }

    public void simpan(Putusan p) {
        daftarPutusan.add(p);
    }

    private void loadInitialData() {

        try (BufferedReader br = new BufferedReader(new FileReader("src/dataSample/putusan.csv"))) {

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                Putusan p = new Putusan(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        Integer.parseInt(data[4]),
                        data[5],
                        Double.parseDouble(data[6]),
                        data[7],
                        data[8],
                        Integer.parseInt(data[9]),
                        Double.parseDouble(data[10]),
                        data[11]
                );

                daftarPutusan.add(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Putusan cariByNomor(String nomor) {
        for (Putusan p : daftarPutusan) {
            if (p.getNomorPerkara().contains(nomor)) {
                return p;
            }
        }
        return null;
    }

   
    public ArrayList<Putusan> cariByNama(String nama) {
        ArrayList<Putusan> filteredData = new ArrayList<>();

        for (Putusan p : daftarPutusan) {
            if (p.getNamaTerdakwa().toLowerCase().contains(nama.toLowerCase())) {
                filteredData.add(p);
            }
        }

        return filteredData;
    }

    
    public ArrayList<Putusan> filterByJenis(String jenis) {
        ArrayList<Putusan> filteredData = new ArrayList<>();

        for (Putusan p : daftarPutusan) {
            if (p.getJenisNarkotika().equalsIgnoreCase(jenis)) {
                filteredData.add(p);
            }
        }

        return filteredData;
    }

    
    public ArrayList<Putusan> filterByPengadilan(String pengadilan) {
        ArrayList<Putusan> filteredData = new ArrayList<>();

        String searchQuery = pengadilan.toLowerCase();

        for (Putusan p : daftarPutusan) {
            String namaPengadilan = p.getPengadilan();

            if (namaPengadilan != null && namaPengadilan.toLowerCase().contains(searchQuery)) {
                filteredData.add(p);
            }
        }

        return filteredData;
    }
    
    public boolean hapus(String nomor) {
        Putusan p = cariByNomor(nomor);

        if (p != null) {
            daftarPutusan.remove(p);
            return true;
        }

        return false;
    }
    
    public ArrayList<Putusan> getDaftarSemua() {
        return new ArrayList<>(daftarPutusan);
    }
    
    public int getTotalData() {
        return daftarPutusan.size();
    }

}