package libraryData;

public interface ILibraryData {

    LibraryData getLibraryDatas();
    void updateLibraryDatas(String name, String address, String postalCode, String city, String openDays, String openHours);
}
