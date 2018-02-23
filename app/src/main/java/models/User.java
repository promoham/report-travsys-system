package models;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;

/**
 * Created by eagle on 11/6/2017.
 */
@Entity
public class User {
    public String name;
    public String password;
    public boolean admin;
    @Id
    public long id;

    public User() {
    }

    public User(String name, String password, boolean admin, long id) {
        this.name = name;
        this.password = password;
        this.admin = admin;
        this.id = id;
    }

    public static String getNameById(long id) {
        Box<User> userBox = dbBox.boxFor(User.class);
        User user = userBox.get(id);
        return user.getName();
    }

    public static boolean getIsAdminById(long id) {
        Box<User> userBox = dbBox.boxFor(User.class);
        User user = userBox.get(id);
        return user.isAdmin();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
