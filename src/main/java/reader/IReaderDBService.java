package reader;

import user.User;

import java.util.List;

public interface IReaderDBService {
    void addReaderInDB(int idUser);
    void deleteReaderFromDB(int idUser);
    Reader readReaderFromDB(int idUser);
    List<Reader> getAllReadersFromDB();
    List<Integer> getReadersCards();
    List<User> readReadersFromDB();
    List<User> readReadersFromDB(int idCard);
    List<User> readReadersFromDB(String name);
    List<User> readReadersFromDB(String firstName, String lastName);
    void chooseBook();
    void makeReservation();
    void seeListOfBorrows();
}
