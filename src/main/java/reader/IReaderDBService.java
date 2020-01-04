package reader;

import java.util.List;

public interface IReaderDBService {
    void addReaderInDB(int idUser);
    void deleteReaderFromDB(int idUser);
    Reader readReaderFromDB(int idUser);
    List<Reader> getAllReadersFromDB();
    List<Integer> getReadersCards();
    void chooseBook();
    void makeReservation();
    void seeListOfBorrows();
}
