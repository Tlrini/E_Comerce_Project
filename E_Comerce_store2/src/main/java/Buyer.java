import java.sql.*;
import java.util.*;

public class Buyer extends User {
    private List<Product> cart;

    public Buyer(String username, String password, int userId) {
        super(username, password, userId, RoleEnum.BUYER);
        this.cart = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'BUYER'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Buyer logged in successfully.");
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
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'BUYER')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("Buyer registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(int productId) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM product WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("description"),
                        resultSet.getInt("stock")
                );
                cart.add(product);
                System.out.println(product.getName() + " added to cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Items in your cart:");
            for (Product product : cart) {
                System.out.println("Name: " + product.getName() + ", Price: " + product.getPrice());
            }
        }
    }

    public void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. You cannot checkout.");
            return;
        }

        double total = 0;
        for (Product product : cart) {
            total += product.getPrice();
        }

        System.out.println("Total amount: " + total);

        // Proceed with purchase: update stock and insert order into DB
        try (Connection connection = DatabaseHandler.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Update stock
            for (Product product : cart) {
                String updateStockQuery = "UPDATE product SET stock = stock - 1 WHERE id = ? AND stock > 0";
                PreparedStatement statement = connection.prepareStatement(updateStockQuery);
                statement.setInt(1, product.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Product " + product.getName() + " is out of stock.");
                    connection.rollback();
                    return;
                }
            }

            // Add order
            String insertOrderQuery = "INSERT INTO orders (buyer_id, total_amount) VALUES (?, ?)";
            PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStatement.setInt(1, getUserId());
            orderStatement.setDouble(2, total);
            orderStatement.executeUpdate();

            ResultSet rs = orderStatement.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                // Add products to the order
                for (Product product : cart) {
                    String insertOrderItemsQuery = "INSERT INTO order_items (order_id, product_id) VALUES (?, ?)";
                    PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemsQuery);
                    orderItemStatement.setInt(1, orderId);
                    orderItemStatement.setInt(2, product.getId());
                    orderItemStatement.executeUpdate();
                }
            }

            connection.commit(); // Commit transaction
            System.out.println("Order placed successfully.");
            cart.clear(); // Clear the cart after checkout
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
