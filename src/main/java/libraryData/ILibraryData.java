package libraryData;

public interface ILibraryData {

    LibraryData getLibraryDatas();
    void updateLibraryDatas(String name, String address, String postalCode, String city, String openDays,
                            String openHours, String eMail, int telephone, String info);
    void setLibraryImage(int image);
}
