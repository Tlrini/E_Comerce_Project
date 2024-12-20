import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private int stock;

    // Constructor that accepts 5 parameters
    public Product(int id, String name, double price, String description, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Static method to fetch all products from the database
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>(); // Use java.util.ArrayList

        // Establish connection to the database
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM products"; // SQL query to fetch all products
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and create Product objects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");
                int stock = resultSet.getInt("stock");

                // Create a Product object and add it to the list
                Product product = new Product(id, name, price, description, stock);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products; // Return the list of products
    }
}
