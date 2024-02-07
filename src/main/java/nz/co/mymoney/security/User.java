package nz.co.mymoney.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "user")
@Table(name = "users")

public class User {

    private @Id
    @GeneratedValue Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    public User(){}


    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
