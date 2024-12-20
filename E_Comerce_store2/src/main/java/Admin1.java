import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin1 extends User {

    public Admin1(String username, String password, int userId) throws SQLException {
        super(username, password,userId,RoleEnum.ADMIN);
        setRole(RoleEnum.ADMIN);
    }

    @Override
    public boolean login(String username, String password) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'ADMIN'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Admin logged in successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void register(String username, String password) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "INSERT INTO users (username, password, role, status) VALUES (?, ?, 'ADMIN', 'ACTIVE')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("Admin registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void manageUsers() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("Username: " + resultSet.getString("username") +
                        ", Role: " + resultSet.getString("role") +
                        ", Status: " + resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchUserByUsername(String username) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Username: " + resultSet.getString("username") +
                        ", Role: " + resultSet.getString("role") +
                        ", Status: " + resultSet.getString("status"));
            } else {
                System.out.println("User " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String username) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + username + " removed successfully.");
            } else {
                System.out.println("User " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void promoteUserToSeller(String username) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "UPDATE users SET role = 'SELLER' WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + username + " promoted to seller.");
            } else {
                System.out.println("User " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetUserPassword(String username, String newPassword) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password for user " + username + " has been reset.");
            } else {
                System.out.println("User " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewUsers() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("Username: " + resultSet.getString("username") + ", Role: " + resultSet.getString("role") + ", Status: " + resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllOrders() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM orders";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Order ID: " + resultSet.getInt("order_id"));
                System.out.println("Buyer: " + resultSet.getString("buyer_name"));
                System.out.println("Total: " + resultSet.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
