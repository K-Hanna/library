package libraryData;

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
                libraryData.setName(rs.getString("library_name"));
                libraryData.setAddress(rs.getString("address"));
                libraryData.setPostalCode(rs.getString("postal_code"));
                libraryData.setCity(rs.getString("city"));
                libraryData.setOpenHours(rs.getString("open_hours"));
                libraryData.setOpenDays(rs.getString("open_days"));
                libraryData.seteMail(rs.getString("e_mail"));
                libraryData.setTelephone(rs.getInt("telephone"));
                libraryData.setInfo(rs.getString("info"));
                libraryData.setImage(rs.getInt("image"));
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
    public void updateLibraryDatas(String name, String address, String postalCode, String city, String openHours,
                                   String openDays, String eMail, int telephone, String info) {

        String SQL = "update library_data set library_name = ?, address = ?, postal_code = ?, city = ?, " +
                "open_hours = ?, open_days = ?, e_mail = ?, telephone = ?, info = ?;";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, openHours);
            preparedStatement.setString(6,openDays);
            preparedStatement.setString(7, eMail);
            preparedStatement.setInt(8, telephone);
            preparedStatement.setString(9, info);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void setLibraryImage(int image) {
        String SQL = "update library_data set image = ?;";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, image);

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
