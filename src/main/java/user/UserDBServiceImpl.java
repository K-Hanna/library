package user;


import card.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static config.DBConfig.closeDBResources;
import static config.DBConfig.initializeDataBaseConnection;

public class UserDBServiceImpl implements IUserDBService {
    @Override
    public void addUserInDB(User user) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryInsertUser = "INSERT INTO public.\"user\" (firstname, lastname, cardid,email,pass,streetbuilding,postalcode) " + "VALUES (?,?,?,?,?,?,?) ";
            preparedStatement = connection.prepareStatement(queryInsertUser);
            preparedStatement.setString(1,user.getFirstName());
            preparedStatement.setString(2,user.getLastName());
            preparedStatement.setInt(3,user.getCardNumber());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5,user.getPassword());
            preparedStatement.setString(6,user.getStreetBuilding());
            preparedStatement.setString(7,user.getPostalCode());
            preparedStatement.executeUpdate();
            System.out.println("New user was added to database");

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
    public void deleteUserFromDB(int idCard) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryDeleteUser = "DELETE FROM public.\"user\" WHERE cardid = ? ";
            preparedStatement = connection.prepareStatement(queryDeleteUser);
            preparedStatement.setInt(1,idCard);
            preparedStatement.execute();
            System.out.println("User was deleted");
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
    public List<User> readUsersFromDB(int idCard) {

        List<User> users = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE (cardid) = (?) ";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setInt(1,idCard);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));
                users.add(user);
            }
            return users;

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
    public User readUserFromDB(int idCard) {

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE (cardid) = (?) ";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setInt(1,idCard);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));
            }
            return user;

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
    public List<User> readUsersFromDB(String name) {

        List<User> users = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE firstname = ? or lastname = ? ";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));
                users.add(user);
            }
            return users;

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
    public List<User> readUsersFromDB(String firstName, String lastName) {

        List<User> users = new ArrayList<>();
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE firstname = ? and lastname = ? ";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));
                users.add(user);
            }
            return users;

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
    public User readUserFromDBById(int idUser) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryReadUser = "SELECT * FROM public.\"user\" WHERE (iduser) = (?) ";
            preparedStatement = connection.prepareStatement(queryReadUser);
            preparedStatement.setInt(1,idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setCardNumber(resultSet.getInt("cardid"));
                user.setPassword(resultSet.getString("pass"));
                user.setIdUser(resultSet.getInt("iduser"));
                user.setPostalCode(resultSet.getString("postalcode"));
                user.setStreetBuilding(resultSet.getString("streetbuilding"));
            }
            return user;

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
    public List<User> getAllUsersFromDB() {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        List<User> userList = new ArrayList<>();
        try {
            String queryReadUsers = "SELECT * FROM public.\"user\" ORDER BY lastname ASC;" ;
            preparedStatement = connection.prepareStatement(queryReadUsers);
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

                userList.add(user);
            }
            return userList;
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
    public void updateUserInDB(int idUser, String firstName, String lastName, String email, String password, String streetBuilding, String postalCode, int cardNumber) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;
        try {
            String queryUpdateUser = "UPDATE public.\"user\" SET (firstname,lastname,email,pass,postalcode,streetbuilding) = (?,?,?,?,?,?) " + "where cardid = ?";
            preparedStatement = connection.prepareStatement(queryUpdateUser);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,password);
            preparedStatement.setString(5,postalCode);
            preparedStatement.setString(6,streetBuilding);
            preparedStatement.setInt(7,cardNumber);
            preparedStatement.executeUpdate();
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
    public int readLastUserIdFromDB() {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            String queryReadUser = "Select * From public.\"user\" order by iduser desc limit 1;";
            int lastUserId = 0;
            preparedStatement = connection.prepareStatement(queryReadUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                lastUserId = resultSet.getInt("iduser");
            }
            return lastUserId;
        }
        catch (SQLException e){
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }


    }
}
