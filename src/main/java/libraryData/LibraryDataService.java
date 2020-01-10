package libraryData;

import book.Book;
import book.Bookshelf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.DBConfig.closeDBResources;
import static config.DBConfig.initializeDataBaseConnection;

public class LibraryDataService implements ILibraryData {

    @Override
    public LibraryData getLibraryDatas() {

        LibraryData libraryData = new LibraryData();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        String SQL = "select * from library_data where library_id = 1;";

        try {
            preparedStatement = connection.prepareStatement(SQL);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                libraryData.setName(rs.getString(1));
                libraryData.setAddress(rs.getString(2));
                libraryData.setPostalCode(rs.getString(3));
                libraryData.setCity(rs.getString(4));
                libraryData.setOpenHours(rs.getString(5));
                libraryData.setOpenDays(rs.getString(6));
            }
            return libraryData;

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void updateLibraryDatas(String name, String address, String postalCode, String city, String openDays, String openHours) {

        String SQL = "update library_data set library_name = ?, address = ?, postal_code = ?, city = ?, open_days = ?, open_hours = ?;";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, openDays);
            preparedStatement.setString(6,openHours);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }
}
