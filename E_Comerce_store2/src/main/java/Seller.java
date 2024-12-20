import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Seller(String username, String password, int userId) {
        super(username, password, userId, RoleEnum.SELLER);
    }

    public void addProduct(String name, double price, String description, int stock) {
        String sql = "INSERT INTO products (name, price, description, stock, seller_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, description);
            statement.setInt(4, stock);
            statement.setInt(5, getUserId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewProducts() {
        String sql = "SELECT id, name, price, description, stock FROM products WHERE seller_id = ?";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, getUserId());

            ResultSet rs = statement.executeQuery();
            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                int stock = rs.getInt("stock");

                products.add(new Product(id, name, price, description, stock));
            }

            if (products.isEmpty()) {
                System.out.println("No products found.");
            } else {
                for (Product product : products) {
                    System.out.println(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateProductStock(int productId, int newStock) {
        String query = "UPDATE products SET stock = ? WHERE id = ? AND seller_id = ?";
        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setInt(1, newStock);
            statement.setInt(2, productId);
            statement.setInt(3, this.id);


            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public void register(String username, String password) {

    }
}
