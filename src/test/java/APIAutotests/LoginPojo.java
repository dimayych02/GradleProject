package APIAutotests;

public class LoginPojo {
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private String username;

    public LoginPojo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String password;
}
