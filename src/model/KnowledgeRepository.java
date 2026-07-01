import java.util.ArrayList;

public class KnowledgeRepository {

    private ArrayList<Putusan> daftarPutusan;

    
    public KnowledgeRepository() {
        daftarPutusan = new ArrayList<>();
    }

    public void simpan(Putusan p) {
        daftarPutusan.add(p);
    }

    
    public Putusan cariByNomor(String nomor) {
        for (Putusan p : daftarPutusan) {
            if (p.getNomorPerkara().equalsIgnoreCase(nomor)) {
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

        for (Putusan p : daftarPutusan) {
            if (p.getPengadilan().equalsIgnoreCase(pengadilan)) {
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