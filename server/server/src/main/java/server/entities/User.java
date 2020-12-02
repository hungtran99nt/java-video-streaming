package server.entities;

import java.io.Serializable;

import server.entities.value.Role;

/**
 * User
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int idCount = 0;
    private int id;
    private String username;
    private String password;
    private Role role;

    /**
     * Create new user with generated id, default role member
     */
    public User() {
        id = ++idCount;
        role = Role.MEMBER;
    }

    /**
     * Create new user with given username and password
     * 
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
}