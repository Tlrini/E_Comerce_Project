import java.sql.SQLException;

public abstract class User {
    private String username;
    private String password;
    private int userId;  // User ID that uniquely identifies the user
    private RoleEnum role;

    // Constructor
    public User(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    // Abstract methods to be implemented by subclasses
    public abstract boolean login(String username, String password);
    public abstract void register(String username, String password);
}
