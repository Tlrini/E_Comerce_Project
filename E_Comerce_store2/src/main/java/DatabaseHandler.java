import java.sql.*;

class DatabaseHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/e_comerce_store2";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default MySQL password

    // Establish database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add user to the database
    public void addUser(String username, String password, RoleEnum role, UserStatusEnum status) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role.name());
            statement.setString(4, status.name());
            statement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validate user login
    public boolean validateLogin(String username, String password, RoleEnum role) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role.name());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve user role
    public RoleEnum getUserRole(String username) {
        try (Connection connection = getConnection()) {
            String query = "SELECT role FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return RoleEnum.valueOf(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public void getAllUsers() {
        try (Connection connection = getConnection()) {
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
}