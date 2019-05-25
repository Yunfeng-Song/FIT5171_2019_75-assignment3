package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PayloadUnitTest {
    Payload target;

    @BeforeEach
    public void setUp() {
        target = new Payload("Voyager 2", "Outside Heliosphere", 826, "Satellite", true);
    }

    private Payload initAltPayload() {
        return new Payload("Centurion", "Asteroid Belt", 100, "Satellite", false);
    }

    // Name tests
    @DisplayName("should throw exception when pass empty name to setName method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetNameToEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setName(name));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null value to setName method")
    @Test
    public void shouldThrowExceptionWhenSetNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setName(null));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    // Destination tests
    @DisplayName("should throw exception when pass empty destination to setDestination method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetDestinationToEmpty(String destination) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setDestination(destination));
        assertEquals("destination cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null value to setDestination method")
    @Test
    public void shouldThrowExceptionWhenSetDestinationToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setDestination(null));
        assertEquals("destination cannot be null or empty", exception.getMessage());
    }

    // Type tests
    @DisplayName("should throw exception when pass empty type to setType method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetTypeToEmpty(String type) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setType(type));
        assertEquals("type cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null value to setType method")
    @Test
    public void shouldThrowExceptionWhenSetTypeToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setType(null));
        assertEquals("type cannot be null or empty", exception.getMessage());
    }

    // MassKG tests
    @DisplayName("should throw exception when pass negative value to setMassKG method")
    @ParameterizedTest
    @ValueSource(ints = {-1})
    public void shouldThrowExceptionWhenSetMassKGToNegative(int mass) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setMassKG(mass));
        assertEquals("massKG cannot be negative", exception.getMessage());
    }

    @DisplayName("should be able to set massKG to 0 or positive number")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void shouldBeAbleToSetMassKGToGreaterThanZero(int mass) {
        target.setMassKG(mass);
        //could throw IllegalArgumentException
    }

    // equals() tests
    @DisplayName("should return true when two of the same payloads are equal")
    @Test
    public void shouldReturnTrueWhenPayloadIsEqual() {
        Payload payload1 = target;
        setUp();
        Payload payload2 = target;
        assertTrue(payload1.equals(payload2));
    }

    @DisplayName("should return false when two different payloads are compared")
    @Test
    public void shouldReturnFalseWhenPayloadIsNotEqual() {
        Payload payloadAlpha = target;
        Payload payloadBeta = initAltPayload();
        assertFalse(payloadAlpha.equals(payloadBeta));
    }

    @DisplayName("should return false when non payload object is compared")
    @Test
    public void shouldReturnFalseWhenPayloadComparedToNonPayload() {
        assertFalse(target.equals("Payload"));
    }

    @DisplayName("should return false when null compared to payload object")
    @Test
    public void shouldReturnFalseWhenPayloadComparedToNull() {
        assertFalse(target.equals(null));
    }

    //hashCode() tests
    @DisplayName("hashCode of two of the same payload objects should be equal")
    @Test
    public void hashCodeOfTheSamePayloadShouldBeEqual() {
        Payload payload1 = target;
        setUp();
        Payload payload2 = target;
        assertEquals(payload1.hashCode(), payload2.hashCode());
    }

    @DisplayName("hashCode of two different launch objects should not be equal")
    @Test
    public void hashCodeOfDifferentLaunchShouldNotBeEqual() {
        Payload payloadAlpha = target;
        Payload payloadBeta = initAltPayload();
        assertNotEquals(payloadAlpha.hashCode(), payloadBeta.hashCode());
    }
}