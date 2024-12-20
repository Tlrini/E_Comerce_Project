class AuthenticationService {
    private final DatabaseHandler databaseHandler;

    // Constructor
    public AuthenticationService() {
        this.databaseHandler = new DatabaseHandler();
    }

    // Register user
    public void registerUser(String username, String password, RoleEnum role, UserStatusEnum status) {
        databaseHandler.addUser(username, password, role, status);
    }

    // Authenticate user login
    public boolean authenticate(String username, String password, RoleEnum role) {
        return databaseHandler.validateLogin(username, password, role);
    }
}