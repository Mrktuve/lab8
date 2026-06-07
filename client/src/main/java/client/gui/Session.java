package client.gui;

/**
 * Хранит данные текущей пользовательской сессии.
 */
public class Session {

    private String login;
    private String password;

    /**
     * Возвращает логин текущего пользователя.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Устанавливает логин текущего пользователя.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Возвращает пароль текущего пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль текущего пользователя.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Проверяет, авторизован ли пользователь.
     */
    public boolean isLoggedIn() {
        return login != null && !login.isEmpty();
    }

    /**
     * Очищает данные сессии.
     */
    public void clear() {
        this.login = null;
        this.password = null;
    }
}