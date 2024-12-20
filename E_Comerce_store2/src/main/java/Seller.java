import java.sql.*;

public class Seller extends User {

    public Seller(String username, String password, int userId) throws SQLException {
        super(username, password);
        setRole(RoleEnum.SELLER);
    }

    @Override
    public boolean login(String username, String password) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'SELLER'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Seller logged in successfully.");
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
            String query = "INSERT INTO users (username, password, role, status) VALUES (?, ?, 'SELLER', 'ACTIVE')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("Seller registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(String name, double price, String description, int stock) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "INSERT INTO products (name, price, description, stock, seller_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, description);
            statement.setInt(4, stock);
            statement.setInt(5, getUserId());
            statement.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewProducts() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM products WHERE seller_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, getUserId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Product Name: " + resultSet.getString("name"));
                System.out.println("Price: " + resultSet.getDouble("price"));
                System.out.println("Stock: " + resultSet.getInt("stock"));
                System.out.println("Description: " + resultSet.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductStock(int productId, int newStock) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "UPDATE products SET stock = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, newStock);
            statement.setInt(2, productId);
            statement.executeUpdate();
            System.out.println("Product stock updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
