package reader;

import user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static config.DBConfig.closeDBResources;
import static config.DBConfig.initializeDataBaseConnection;

public class ReaderDBServiceImpl implements IReaderDBService {

    @Override
    public void addReaderInDB(int idUser) {

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryInsertReader = "INSERT INTO reader (userid) " + "VALUES (?) ";
            preparedStatement = connection.prepareStatement(queryInsertReader);
            preparedStatement.setInt(1, idUser);
            preparedStatement.executeUpdate();
            System.out.println("New reader was added to database");

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke SQL query");
        } finally {
            closeDBResources(connection, preparedStatement);
        }

    }

    @Override
    public void deleteReaderFromDB(int idUser) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryDeleteReader = "DELETE FROM reader WHERE userid = ? ";
            preparedStatement = connection.prepareStatement(queryDeleteReader);
            preparedStatement.setInt(1, idUser);
            preparedStatement.execute();
            System.out.println("Reader was deleted");
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke SQL query");
        } finally {
            closeDBResources(connection, preparedStatement);
        }
    }

    @Override
    public Reader readReaderFromDB(int idUser) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadReader = "SELECT * FROM reader WHERE (userid) = (?) ";
            preparedStatement = connection.prepareStatement(queryReadReader);
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            Reader reader = new Reader();
            while (resultSet.next()) {
                reader.setIdReader(resultSet.getInt("idreader"));
                reader.setIdUser(resultSet.getInt("userid"));
            }
            return reader;
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke SQL query");
        } finally {
            closeDBResources(connection, preparedStatement);
        }
    }

    @Override
    public List<Integer> getReadersCards() {

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        List<Integer> listOfCards = new ArrayList<>();

        String SQL = "select distinct cardid from public.user where iduser in (select userid from reader);";

        try {
            preparedStatement = connection.prepareStatement(SQL);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int card = rs.getInt("cardid");
                listOfCards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke SQL query");
        } finally {
            closeDBResources(connection, preparedStatement);
        }

        return listOfCards;
    }

    @Override
    public List<User> readReadersFromDB(int idCard) {

        List<User> readers = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadReader = "SELECT * FROM public.user WHERE (cardid) = (?) and iduser in (select userid from reader)";
            preparedStatement = connection.prepareStatement(queryReadReader);
            preparedStatement.setInt(1,idCard);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));

                readers.add(user);
            }
            return readers;

        }
        catch (SQLException e){
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public List<User> readReadersFromDB() {

        List<User> readers = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadReader = "SELECT * FROM public.user WHERE iduser in (select userid from reader) order by lastname;";
            preparedStatement = connection.prepareStatement(queryReadReader);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));

                readers.add(user);
            }
            return readers;

        }
        catch (SQLException e){
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public List<User> readReadersFromDB(String name) {

        List<User> readers = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.user WHERE firstname = ? or lastname = ? and iduser in (select userid from reader)";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));

                readers.add(user);
            }
            return readers;

        }
        catch (SQLException e){
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public List<User> readReadersFromDB(String firstName, String lastName) {

        List<User> readers = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE firstname = ? and lastname = ? and iduser in (select userid from reader)";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));

                readers.add(user);
            }
            return readers;

        }
        catch (SQLException e){
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public int getUserId(int readerId) {

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        int userId = 0;

        try {
            String queryReadReader = "SELECT * FROM reader WHERE (idreader) = (?) ";
            preparedStatement = connection.prepareStatement(queryReadReader);
            preparedStatement.setInt(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userId = resultSet.getInt("userid");
            }
            return userId;
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke SQL query");
        } finally {
            closeDBResources(connection, preparedStatement);
        }
    }
}
