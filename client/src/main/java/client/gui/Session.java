package client.gui;

/**
 * Хранит данные текущей пользовательской сессии.
 */
public class Session {

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return login != null && !login.isEmpty();
    }

    public void clear() {
        this.login = null;
        this.password = null;
    }
}