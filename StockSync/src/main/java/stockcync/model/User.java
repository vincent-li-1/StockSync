package stockcync.model;

public class User {
    private Long userId; // Corresponds to the users_PK column in the database
    private String userName; // Corresponds to the users_Name column in the database

    // Getters and setters for userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getters and setters for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}