public class Putusan {

    private String nomorPerkara;
    private String pengadilan;
    private String tanggalPutusan;
    private String namaTerdakwa;
    private int umurTerdakwa;
    private String jenisNarkotika;
    private double beratBarangBukti;
    private String pasalDilanggar;
    private String peranTerdakwa;
    private int vonisHukuman;
    private double vonisDenda;
    private String namaHakim;

    private static int jumlahDibuat;

    public Putusan() {
        jumlahDibuat++;
    }

    public Putusan(
            String nomorPerkara,
            String pengadilan,
            String tanggalPutusan,
            String namaTerdakwa,
            int umurTerdakwa,
            String jenisNarkotika,
            double beratBarangBukti,
            String pasalDilanggar,
            String peranTerdakwa,
            int vonisHukuman,
            double vonisDenda,
            String namaHakim
    ) {
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.namaTerdakwa = namaTerdakwa;
        this.umurTerdakwa = umurTerdakwa;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBukti = beratBarangBukti;
        this.pasalDilanggar = pasalDilanggar;
        this.peranTerdakwa = peranTerdakwa;
        this.vonisHukuman = vonisHukuman;
        this.vonisDenda = vonisDenda;
        this.namaHakim = namaHakim;

        jumlahDibuat++;
    }

    public String getNomorPerkara() {
        return nomorPerkara;
    }

    public void setNomorPerkara(String nomorPerkara) {
        this.nomorPerkara = nomorPerkara;
    }

    public String getPengadilan() {
        return pengadilan;
    }

    public void setPengadilan(String pengadilan) {
        this.pengadilan = pengadilan;
    }

    public String getTanggalPutusan() {
        return tanggalPutusan;
    }

    public void setTanggalPutusan(String tanggalPutusan) {
        this.tanggalPutusan = tanggalPutusan;
    }

    public String getNamaTerdakwa() {
        return namaTerdakwa;
    }

    public void setNamaTerdakwa(String namaTerdakwa) {
        this.namaTerdakwa = namaTerdakwa;
    }

    public int getUmurTerdakwa() {
        return umurTerdakwa;
    }

    public void setUmurTerdakwa(int umurTerdakwa) {
        this.umurTerdakwa = umurTerdakwa;
    }

    public String getJenisNarkotika() {
        return jenisNarkotika;
    }

    public void setJenisNarkotika(String jenisNarkotika) {
        this.jenisNarkotika = jenisNarkotika;
    }

    public double getBeratBarangBukti() {
        return beratBarangBukti;
    }

    public void setBeratBarangBukti(double beratBarangBukti) {
        this.beratBarangBukti = beratBarangBukti;
    }

    public String getPasalDilanggar() {
        return pasalDilanggar;
    }

    public void setPasalDilanggar(String pasalDilanggar) {
        this.pasalDilanggar = pasalDilanggar;
    }

    public String getPeranTerdakwa() {
        return peranTerdakwa;
    }

    public void setPeranTerdakwa(String peranTerdakwa) {
        this.peranTerdakwa = peranTerdakwa;
    }

    public int getVonisHukuman() {
        return vonisHukuman;
    }

    public void setVonisHukuman(int vonisHukuman) {
        this.vonisHukuman = vonisHukuman;
    }

    public double getVonisDenda() {
        return vonisDenda;
    }

    public void setVonisDenda(double vonisDenda) {
        this.vonisDenda = vonisDenda;
    }

    public String getNamaHakim() {
        return namaHakim;
    }

    public void setNamaHakim(String namaHakim) {
        this.namaHakim = namaHakim;
    }

    public static int getJumlahDibuat() {
        return jumlahDibuat;
    }

    public static void setJumlahDibuat(int jumlahDibuat) {
        Putusan.jumlahDibuat = jumlahDibuat;
    }

    public void tampilkan() {
        System.out.println(
                nomorPerkara + " | "
                        + namaTerdakwa + " | "
                        + jenisNarkotika
        );
    }

    public void tampilkan(boolean detail) {

        if (!detail) {
            tampilkan();
            return;
        }

        System.out.println(this);
    }

    public String getKategoriHukuman() {
        return "Placehold";
    }

    @Override
    public String toString() {
        return "Putusan{" +
                "nomorPerkara='" + nomorPerkara + '\'' +
                ", pengadilan='" + pengadilan + '\'' +
                ", tanggalPutusan='" + tanggalPutusan + '\'' +
                ", namaTerdakwa='" + namaTerdakwa + '\'' +
                ", umurTerdakwa=" + umurTerdakwa +
                ", jenisNarkotika='" + jenisNarkotika + '\'' +
                ", beratBarangBukti=" + beratBarangBukti +
                ", pasalDilanggar='" + pasalDilanggar + '\'' +
                ", peranTerdakwa='" + peranTerdakwa + '\'' +
                ", vonisHukuman=" + vonisHukuman +
                ", vonisDenda=" + vonisDenda +
                ", namaHakim='" + namaHakim + '\'' +
                '}';
    }
}