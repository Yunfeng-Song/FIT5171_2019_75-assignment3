package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class LaunchUnitTest {
    private Launch target;

    @BeforeEach
    public void SetUp() {
        target = new Launch();
    }

    @DisplayName("should throw exception when pass an empty set to setPayload() method")
    @Test
    public void shouldThrowExceptionWhenPassEmptyPayloadSetToSetPayload() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPayload(new HashSet<Payload>()));
        assertEquals("payload set cannot be set to empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null launchSite to setLaunchSite() method")
    @Test
    public void shouldThrowExceptionWhenSetLaunchSiteToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setLaunchSite(null));
        assertEquals("launchSite can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty launchSite to setLaunchSite() method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLaunchSiteToEmpty(String launchSite) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLaunchSite(launchSite));
        assertEquals("launchSite can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null orbit to setOrbit() method")
    @Test
    public void shouldThrowExceptionWhenSetOrbitToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setOrbit(null));
        assertEquals("orbit can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty orbit to setOrbit() method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetOrbitToEmpty(String orbit) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setOrbit(orbit));
        assertEquals("orbit can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a null function to setFunction() method")
    @Test
    public void shouldThrowExceptionWhenSetFunctionToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setFunction(null));
        assertEquals("function can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty function to setFunction() method")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFunctionToEmpty(String function) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFunction(function));
        assertEquals("function can't be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when set price to negative value")
    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    public void shouldThrowExceptionWhenSetPriceToNegative(int price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> target.setPrice(new BigDecimal(price)));
        assertEquals("price can't be negative", exception.getMessage());
    }

    /**
     * creates a Launch object with values for launchDate, launchVehicle, launchServiceProvider and orbit
     * @return
     */
    public Launch initLaunch() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("Martin Marietta", 1958, "USA");
        Rocket titanRocket = new Rocket("Titan IIIE", "United States", manufacturer);
        LocalDate launchDate = LocalDate.parse("1977-08-20");
        LaunchServiceProvider serviceProvider = new LaunchServiceProvider("NASA", 1958, "United States");

        return new Launch(launchDate, titanRocket, serviceProvider, "Pluto");
    }

    // equals() tests
    @DisplayName("should return true when two of the same launches are equal")
    @Test
    public void shouldReturnTrueWhenLaunchIsEqual() {
        Launch launchAlpha = initLaunch();
        Launch launchAlpha2 = initLaunch();
        assertTrue(launchAlpha.equals(launchAlpha2));
    }

    @DisplayName("should return false when two different launches are compared")
    @Test
    public void shouldReturnFalseWhenLaunchIsNotEqual() {
        Launch launchAlpha = initLaunch();
        Launch launchBeta = initLaunch();
        launchBeta.setOrbit("Mars");
        assertFalse(launchAlpha.equals(launchBeta));
    }

    @DisplayName("should return false when non launch object is compared")
    @Test
    public void shouldReturnFalseWhenLaunchComparedToNonLaunch() {
        assertFalse(initLaunch().equals("Launch"));
    }

    @DisplayName("should return false when null compared to launch object")
    @Test
    public void shouldReturnFalseWhenLaunchComparedToNull() {
        assertFalse(initLaunch().equals(null));
    }

    //hashCode() tests
    @DisplayName("hashCode of two of the same launch objects should be equal")
    @Test
    public void hashCodeOfTheSameLaunchShouldBeEqual() {
        Launch launchAlpha = initLaunch();
        Launch launchAlpha2 = initLaunch();
        assertEquals(launchAlpha.hashCode(), launchAlpha2.hashCode());
    }

    @DisplayName("hashCode of two different launch objects should not be equal")
    @Test
    public void hashCodeOfDifferentLaunchShouldNotBeEqual() {
        Launch launchAlpha = initLaunch();
        Launch launchBeta = initLaunch();
        launchBeta.setOrbit("Mars");
        assertNotEquals(launchAlpha.hashCode(), launchBeta.hashCode());
    }
}