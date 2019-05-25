package rockets.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;

@NodeEntity
public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public User(){
        firstName = "";
        lastName = "";
        email = "";
        password = "";
    }

    public User(String firstName, String lastName, String email)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    //disallows numbers, symbols, spaces and -' combinations, but allows ONE - or '
    private final static String NAME_REGEX = "[a-zA-Z]+['-][a-zA-Z]+$|^[A-Za-z]+$";
    //email Regex: ^(?!\.)[\w.!#$%&'*+\-/=?^_`{|}~]+@[\w.]+\.[a-zA-Z]{2,}$
    private final static String EMAIL_REGEX = "^(?!\\.)[\\w.!#$%&'*+\\-/=?^_`{|}~]+@[\\w.]+\\.[a-zA-Z]{2,}$";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws IllegalArgumentException {
        if (!Pattern.matches(NAME_REGEX, firstName)) {
            throw new IllegalArgumentException("first name can't have numbers, symbols or spaces except a single - or '");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!Pattern.matches(NAME_REGEX, lastName)) {
            throw new IllegalArgumentException("surname can't have numbers, symbols or spaces except a single - or '");
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new IllegalArgumentException("email is invalid");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        if (password.length() < 8) {
            throw new IllegalArgumentException("password must be 8 or more characters long");
        }
        // If password doesn't include non alphabetic characters
        if (!Pattern.matches("^(?:[a-zA-Z]*[^a-zA-Z]+[a-zA-Z]*)+$", password))
            throw new IllegalArgumentException("password must contain symbols or numbers");
        // If password doesn't include upper and lowercase characters
        if (!Pattern.matches("^.*(?=[a-z]).*(?=[A-Z]).*$|^.*(?=[A-Z]).*(?=[a-z]).*$", password))
            throw new IllegalArgumentException(("password must include upper and lowercase letters"));
        this.password = password;
    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
