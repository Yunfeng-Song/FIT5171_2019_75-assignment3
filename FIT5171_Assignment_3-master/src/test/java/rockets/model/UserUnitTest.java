package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {
    private User target;

    @BeforeEach
    public void setUp() {
        target = new User("Tony", "Stark", "tony.stark@starkindustries.com");
    }


    @DisplayName("should throw exception when pass a empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setEmail function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exceptions when pass a null password to setPassword function")
    @Test
    public void shouldThrowExceptionWhenSetPasswordToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPassword(null));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when two users have the same email")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameEmail() {
        String email = "tony.stark@starkindustries.com";
        target.setEmail(email);
        User anotherUser = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        anotherUser.setEmail(email);
        assertTrue(target.equals(anotherUser));
    }


    @DisplayName("should return false when two users have different emails")
    @Test
    public void shouldReturnFalseWhenUsersHaveDifferentEmails() {
        target.setEmail("abc@example.com");
        User anotherUser = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        // anotherUser.setEmail("def@example.com");
        assertFalse(target.equals(anotherUser));
    }

    // Added tests
    // First Name tests
    @DisplayName("should throw exception if first name has numbers")
    @ParameterizedTest
    @ValueSource(strings = {"luke1", "1luke", "7", "4.6"})
    public void shouldThrowExceptionWhenSetNumericFirstName(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setFirstName(name));
        assertEquals("first name can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("should throw exception if first name has spaces")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void shouldThrowExceptionWhenSetFirstNameWithSpace(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setFirstName(name));
        assertEquals("first name can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("should throw exception if first name has symbols")
    @ParameterizedTest
    @ValueSource(strings = {"luk@", "lu!e", "l&ke", "l()ke", "lu+k=", "l[]ke", "l<>ke", "l#ke", "|uke", "l*ke", "l^ke",
            "l%ke", "lu.ke", "lu,ke", "luke?", "{_}\\/\"", "Mc-Mari-Ann"})
    public void shouldThrowExceptionWhenFirstNameSetWithSymbols(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setFirstName(name));
        assertEquals("first name can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("no exception thrown if first name has a single - or '")
    @ParameterizedTest
    @ValueSource(strings = {"Mari-Anne", "O'Keif"})
    public void shouldAllowNamesWithSingleHyphenOrApostrophe(String name) {
        target.setFirstName(name);
        // No error should be thrown, however it is possible that an IllegalArgumentException is thrown
    }

    // Surname tests
    @DisplayName("should throw exception if surname has numbers")
    @ParameterizedTest
    @ValueSource(strings = {"smith1", "1smith", "7", "4.6"})
    public void shouldThrowExceptionWhenSetNumericSurname(String surname) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setLastName(surname));
        assertEquals("surname can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("should throw exception if surname has spaces")
    @ParameterizedTest
    @ValueSource(strings = {"Sm ith", " Smith ", " Sm ith "})
    public void shouldThrowExceptionWhenSetSurnameWithSpace(String surname) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setLastName(surname));
        assertEquals("surname can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("should throw exception if surname has symbols")
    @ParameterizedTest
    @ValueSource(strings = {"luk@", "lu!e", "l&ke", "l()ke", "lu+k=", "l[]ke", "l<>ke", "l#ke", "|uke", "l*ke", "l^ke",
            "l%ke", "lu.ke", "lu,ke", "luke?", "{_}\\/\"", "Mc-Mari-Ann"})
    public void shouldThrowExceptionWhenSurnameSetWithSymbols(String surname) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setLastName(surname));
        assertEquals("surname can't have numbers, symbols or spaces except a single - or '", exception.getMessage());
    }

    @DisplayName("no exception thrown if surname has a single - or '")
    @ParameterizedTest
    @ValueSource(strings = {"Mari-Anne", "O'Keefe"})
    public void shouldAllowSurnamesWithSingleHyphenOrApostrophe(String surname) {
        target.setLastName(surname);
        // No error should be thrown, however it is possible that an IllegalArgumentException is thrown
    }

    @DisplayName("should throw exception if email is invalid")
    @ParameterizedTest
    @ValueSource(strings = {"user@email@.com", "useremail.com", ".user@email.com", "user @email.com"})
    public void shouldThrowExceptionIfInvalidEmail(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setEmail(email));
        assertEquals("email is invalid", exception.getMessage());
    }

    @DisplayName("should throw exception if password is less than 8 characters")
    @ParameterizedTest
    @ValueSource(strings = {"P@sswor", "Pa$$"})
    public void shouldThrowExceptionIfPasswordLessThan8Characters(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPassword(password));
        assertEquals("password must be 8 or more characters long", exception.getMessage());
    }

    @DisplayName("should throw exception if password only contains alphabetic characters")
    @ParameterizedTest
    @ValueSource(strings = {"Passwords"})
    public void shouldThrowExceptionIfPasswordContainsNoSymbols(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPassword(password));
        assertEquals("password must contain symbols or numbers", exception.getMessage());
    }

    @DisplayName("should throw exception if password doesn't contain a mix of upper and lowercase letters")
    @ParameterizedTest
    @ValueSource(strings = {"P@SSWORDS", "p@sswords"})
    public void shouldThrowExceptionIfPasswordDoesntContainUpperAndLowercaseLetters(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPassword(password));
        assertEquals("password must include upper and lowercase letters", exception.getMessage());
    }

    @DisplayName("should successfully set valid password")
    @ParameterizedTest
    @ValueSource(strings = {"P@ssword", "p@ssworD", "p@$$W0rd", "123Passw@rd", "@passworD"})
    public void shouldSuccessfullySetValidPassword(String password) {
        target.setPassword(password);
        assertEquals(target.getPassword(), password);
    }

    @DisplayName("should return true when passwords match")
    @ParameterizedTest
    @ValueSource(strings = {"P@$$w0rd"})
    public void shouldReturnTrueWhenPasswordsMatch(String password) {
        target.setPassword(password);
        assertTrue(target.isPasswordMatch(password));
    }

    @DisplayName("should return false when passwords don't match")
    @ParameterizedTest
    @ValueSource(strings = {"P@$$w0rd"})
    public void shouldReturnFalseWhenPasswordsMismatch(String password) {
        target.setPassword(password);
        assertFalse(target.isPasswordMatch("wrongPassword"));
    }

    // equals() tests
    @DisplayName("should return true when two of the same users are equal")
    @Test
    public void shouldReturnTrueWhenUserEmailIsEqual() {
        String email = "allen@email.com";
        User allen = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        User allen2 = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        allen.setEmail(email);
        allen2.setEmail(email);

        assertTrue(allen.equals(allen2));
    }

    @DisplayName("should return false when two different users are compared")
    @Test
    public void shouldReturnFalseWhenUserEmailIsNotEqual() {
        User allen = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        User brian = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        allen.setEmail("allen@email.com");
        brian.setEmail("brian@email.com");
        assertFalse(allen.equals(brian));
    }

    @DisplayName("should return false when non user object is compared")
    @Test
    public void shouldReturnFalseWhenUserComparedToNonUser() {
        assertFalse(new User("Tony", "Stark", "tony.stark@starkindustries.com").equals("Launch"));
    }

    @DisplayName("should return false when null compared to user object")
    @Test
    public void shouldReturnFalseWhenUserComparedToNull() {
        assertFalse(new User("Tony", "Stark", "tony.stark@starkindustries.com").equals(null));
    }

    //hashCode() tests
    @DisplayName("hashCode of two of the same user objects should be equal")
    @Test
    public void hashCodeOfTheSameUserShouldBeEqual() {
        String email = "allen@email.com";
        User allen = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        User allen2 = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        allen.setEmail(email);
        allen2.setEmail(email);
        assertEquals(allen.hashCode(), allen2.hashCode());
    }

    @DisplayName("hashCode of two different user objects should not be equal")
    @Test
    public void hashCodeOfDifferentLaunchShouldNotBeEqual() {
        User allen = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        User brian = new User("Steve", "Rogers", "tony.stark@starkindustries.com");
        allen.setEmail("allen@email.com");
        brian.setEmail("brian@email.com");
        assertNotEquals(allen.hashCode(), brian.hashCode());
    }
}