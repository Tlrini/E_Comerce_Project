import java.sql.SQLException;

public abstract class User {
    private String username;
    private String password;
    private int userId;
    private RoleEnum role;




    public User(String username, String password, int userId, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.role = role;
    }


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


    public abstract boolean login(String username, String password);
    public abstract void register(String username, String password);
}
