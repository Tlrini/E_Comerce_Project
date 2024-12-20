import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            User loggedInUser = null;

            System.out.println("Welcome to the E-Commerce Platform!");

            while (loggedInUser == null) {
                System.out.println("Do you want to log in or register? (Type 'login' or 'register')");
                String action = scanner.nextLine().toLowerCase();

                if (action.equals("login")) {
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    loggedInUser = loginUser(username, password);

                    if (loggedInUser != null) {
                        System.out.println("Logged in successfully as " + loggedInUser.getRole());
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }

                } else if (action.equals("register")) {
                    System.out.print("Enter a username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter a password: ");
                    String password = scanner.nextLine();

                    System.out.println("Select role: 'buyer', 'seller', or 'admin'");
                    String role = scanner.nextLine().toUpperCase();

                    loggedInUser = registerUser(username, password, role);
                    if (loggedInUser != null) {
                        System.out.println("Registered successfully as " + role);
                    }
                } else {
                    System.out.println("Invalid input. Please type 'login' or 'register'.");
                }
            }

            while (loggedInUser != null) {
                if (loggedInUser instanceof Admin1) {
                    Admin1 admin1 = (Admin1) loggedInUser;
                    adminActions(scanner, admin1);
                } else if (loggedInUser instanceof Seller) {
                    Seller seller = (Seller) loggedInUser;
                    sellerActions(scanner, seller);
                } else if (loggedInUser instanceof Buyer) {
                    Buyer buyer = (Buyer) loggedInUser;
                    buyerActions(scanner, buyer);
                }

                System.out.println("\nDo you want to log out? (yes/no)");
                String logoutChoice = scanner.nextLine().toLowerCase();
                if (logoutChoice.equals("yes")) {
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static User loginUser(String username, String password) throws SQLException {
        User user = null;

        String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("id");

                switch (role.toUpperCase()) {
                    case "ADMIN":
                        user = new Admin1(username, password, userId);
                        break;
                    case "SELLER":
                        user = new Seller(username, password, userId);
                        break;
                    case "BUYER":
                        user = new Buyer(username, password, userId);
                        break;
                    default:
                        System.out.println("Invalid role.");
                }
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private static User registerUser(String username, String password, String role) throws SQLException {
        User user = null;
        RoleEnum userRole;

        try {
            userRole = RoleEnum.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role.");
            return null;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, userRole.name());
            statement.setString(4, "ACTIVE");
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                switch (userRole) {
                    case BUYER:
                        user = new Buyer(username, password, userId);
                        break;
                    case SELLER:
                        user = new Seller(username, password, userId);
                        break;
                    case ADMIN:
                        user = new Admin1(username, password, userId);
                        break;
                    default:
                        System.out.println("Error: Unknown role.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    private static void adminActions(Scanner scanner, Admin1 admin1) {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Manage Users");
        System.out.println("2. View All Orders");
        System.out.println("3. Search User by Username");
        System.out.println("4. Remove User");
        System.out.println("5. Promote User to Seller");
        System.out.println("6. Reset User Password");
        System.out.println("7. Logout");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                admin1.manageUsers();
                break;
            case 2:
                admin1.viewAllOrders();
                break;
            case 3:
                System.out.print("Enter username to search: ");
                String searchUsername = scanner.nextLine();
                admin1.searchUserByUsername(searchUsername);
                break;
            case 4:
                System.out.print("Enter username to remove: ");
                String removeUsername = scanner.nextLine();
                admin1.removeUser(removeUsername);
                break;
            case 5:
                System.out.print("Enter username to promote to seller: ");
                String promoteUsername = scanner.nextLine();
                admin1.promoteUserToSeller(promoteUsername);
                break;
            case 6:
                System.out.print("Enter username to reset password: ");
                String resetUsername = scanner.nextLine();
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                admin1.resetUserPassword(resetUsername, newPassword);
                break;
            case 7:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void sellerActions(Scanner scanner, Seller seller) {
        System.out.println("\nSeller Menu:");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Update Product Stock");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter product name: ");
                String productName = scanner.nextLine();
                System.out.print("Enter product price: ");
                double productPrice = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter product stock: ");
                int productStock = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter product description: ");
                String description = scanner.nextLine();
                seller.addProduct(productName, productPrice, description, productStock);
                break;
            case 2:
                seller.viewProducts();
                break;
            case 3:
                System.out.print("Enter product ID to update stock: ");
                int productId = scanner.nextInt();
                System.out.print("Enter new stock quantity: ");
                int newStock = scanner.nextInt();
                scanner.nextLine();
                seller.updateProductStock(productId, newStock);
                break;
            case 4:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void buyerActions(Scanner scanner, Buyer buyer) {
        System.out.println("\nAvailable Products:");
        List<Product> availableProducts = Product.getAllProducts();
        for (Product product : availableProducts) {
            System.out.println("ID: " + product.getId() + " | Name: " + product.getName() +
                    " | Price: " + product.getPrice() + " | Stock: " + product.getStock() +
                    " | Description: " + product.getDescription());
        }
        System.out.println("\nBuyer Menu:");
        System.out.println("1. Add to Cart");
        System.out.println("2. View Cart");
        System.out.println("3. Checkout");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter product ID to add to cart: ");
                int productId = scanner.nextInt();
                buyer.addToCart(productId);
                break;
            case 2:
                buyer.viewCart();
                break;
            case 3:
                buyer.checkout();
                break;
            case 4:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
}

