package rockets.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RocketUnitTest {
    private Rocket testRocket;

    @BeforeEach
    @DisplayName("should throw an exception when passed a null value to name ")
    @Test
    public void shouldThrowExceptionWhenNameIsNull() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new Rocket(null, "USA", manufacturer));
        assertEquals("rocket name cannot be null", exception.getMessage());
    }

    @BeforeEach
    @DisplayName("should throw an exception when passed a null value to country ")
    @Test
    public void shouldThrowExceptionWhenCountryIsNull() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new Rocket("Navjot1", null, manufacturer));
        assertEquals("rocket manufacture country cannot be null", exception.getMessage());
    }

    @BeforeEach
    @DisplayName("should throw an exception when passed a null value to manufacturer ")
    @Test
    public void shouldThrowExceptionWhenManufacturerIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new Rocket("Navjot1", "Australia", null));
        assertEquals("rocket manufacturer cannot be null", exception.getMessage());
    }

    @BeforeEach
    public void SetUp() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("ISRO", 1978, "India");
        testRocket = new Rocket("Navjot2", "India", manufacturer);
    }

    @DisplayName("should throw an exception when passed an empty value to name ")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenNameIsEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setName(name));
        assertEquals("rocket name cannot be empty", exception.getMessage());
    }

    @DisplayName("getName() should return a string of shorter length leading/trailing/both passed along with string to setName()")
    @ParameterizedTest
    @ValueSource(strings = {"  Navjot", "Navjot  ", "  Navjot  "})
    public void shouldReturnNameOfSmallerLengthWhenPassedWithLeadingOrTrailingSpaces(String name) {
        testRocket.setName(name);
        assertTrue(name.length() > testRocket.getName().length());
    }

    @DisplayName("should throw an exception when passed an empty value to country ")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "\n", "\t"})
    public void shouldThrowExceptionWhenCountryIsEmpty(String country) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setCountry(country));
        assertEquals("rocket manufacture country cannot be empty", exception.getMessage());
    }

    @DisplayName("getCountry() should return a string of shorter length leading/trailing/both passed along with string to setCountry()")
    @ParameterizedTest
    @ValueSource(strings = {"  Australia", "Australia  ", "  Australia  "})
    public void shouldReturnCountryOfSmallerLengthWhenPassedWithLeadingOrTrailingSpaces(String country) {
        testRocket.setCountry(country);
        assertTrue(country.length() > testRocket.getCountry().length());
    }

//    @DisplayName("should throw an exception when passed an empty value to manufacturer ")
//    @ParameterizedTest
//    @ValueSource(strings = {"", " ", "  "})
//    public void shouldThrowExceptionWhenManufacturerIsEmpty(String manufacturer) {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> testRocket.setManufacturer(manufacturer));
//        assertEquals("rocket manufacturer cannot be empty", exception.getMessage());
//    }

    @DisplayName("getManufacturer() ")
    @Test
    public void getManufacturerMustReturnTheExpectedLaunchServiceProviderObject() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        testRocket.setManufacturer(manufacturer);
        assertTrue(manufacturer == testRocket.getManufacturer());
    }

    //equals tests
    @DisplayName("should return true when two instances of Rocket are equal")
    @Test
    public void shouldReturnTrueWhenTwoRocketInstancesAreEqual() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("ISRO", 1978, "India");
        Rocket testRocket2 = new Rocket("Navjot2", "India", manufacturer);
        assertTrue(testRocket.equals(testRocket2));
    }

    @DisplayName("should throw an exception when two instances of Rocket are not equal")
    @Test
    public void shouldThrowExceptionWhenTwoRocketInstancesAreNotEqual() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        Rocket testRocket2 = new Rocket("Navjot3", "USA", manufacturer);
        assertFalse(testRocket.equals(testRocket2));
    }

    @DisplayName("should return an exception when non Rocket object is passed to equal method")
    @Test
    public void shouldReturnExceptionWhenPassedInvalidRocketInstance() {
        assertFalse(testRocket.equals("1.2.3...go"));
    }

    @DisplayName("should return an exception when null is passed to equal method")
    @Test
    public void shouldReturnExceptionWhenPassedNull() {
        assertFalse(testRocket.equals(null));
    }

    //hashCode() tests
    @DisplayName("hashCode of two of the same Rocket instances should be equal")
    @Test
    public void hashCodeOfTwoInstancesOfSameRocketShouldBeEqual() {
        Rocket r1 = testRocket;
        SetUp();
        Rocket r2 = testRocket;
        assertEquals(r1, r2);
    }

    @DisplayName("hashCode of two different Rocket instances should not be equal")
    @Test
    public void shouldThrowExceptionIfhashCodeOfTwoInstancesOfRocketAreNotEqual() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        Rocket testRocket2 = new Rocket("Navjot4", "USA", manufacturer);
        assertNotEquals(testRocket.hashCode(), testRocket2.hashCode());
    }

    //massToLEO tests

    @DisplayName("should set massToLEO upon passing a valid numerical string value")
    @Test
    public void shouldUpdateMassToLEOWhenPassedValidValue() {
        String massToLEO = "1200";
        testRocket.setMassToLEO(massToLEO);
        assertEquals(testRocket.getMassToLEO(), massToLEO);
    }

    @DisplayName("getMassToLEO() should return a string of shorter length leading/trailing/both passed along with string to setMassToLEO()")
    @ParameterizedTest
    @ValueSource(strings = {"  1200", "1200  ", "  1200  "})
    public void shouldReturnMassToLEOOfSmallerLengthWhenPassedWithLeadingOrTrailingSpaces(String massToLEO) {
        testRocket.setMassToLEO(massToLEO);
        assertTrue(massToLEO.length() > testRocket.getMassToLEO().length());
    }

    @DisplayName("should throw an exception on updating massToLEO upon passing an empty string value")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenPassedEmptyStringToMassToLEO(String massToLEO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToLEO(massToLEO));
        assertEquals("massToLEO cannot be empty", exception.getMessage());
    }

    @DisplayName("should throw an exception on updating massToLEO upon passing a null value")
    @Test
    public void shouldThrowExceptionWhenPassedNullToMassToLEO() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> testRocket.setMassToLEO(null));
        assertEquals("massToLEO cannot be null", exception.getMessage());
    }

    //assumption: sinceMassToLEO is a ratio, the value entered must be a number (passed and stored as a string)
    @DisplayName("should throw an exception on updating massToLEO upon passing non numeric characters")
    @ParameterizedTest
    @ValueSource(strings = {"123k", "oneKg", "1200lbs", "$124lb", "@2\\!*@!#@"})
    public void shouldThrowExceptionIfNonNumericCharactersPassedToMassToLEO(String massToLEO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToLEO(massToLEO));
        assertEquals("massToLEO must only contain numeric characters", exception.getMessage());
    }

    //massToGTO
    @DisplayName("@DisplayName(\"should set massToGTO upon passing a valid numerical string value\")")
    @Test
    public void shouldUpdateMassToGTOWhenPassedValidValue() {
        String massToGTO = "1800";
        testRocket.setMassToGTO(massToGTO);
        assertEquals(testRocket.getMassToGTO(), massToGTO);
    }

    @DisplayName("getMassToGTO() should return a string of shorter length leading/trailing/both passed along with string to setMassToGTO()")
    @ParameterizedTest
    @ValueSource(strings = {"  1800", "1800  ", "  1800  "})
    public void shouldReturnMassToGTOOfSmallerLengthWhenPassedWithLeadingOrTrailingSpaces(String massToGTO) {
        testRocket.setMassToGTO(massToGTO);
        assertTrue(massToGTO.length() > testRocket.getMassToGTO().length());
    }


    @DisplayName("should throw an exception on updating massToGTO upon passing a null value")
    @Test
    public void shouldThrowExceptionWhenPassedNullToMassToGTO() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> testRocket.setMassToGTO(null));
        assertEquals("massToGTO cannot be null", exception.getMessage());
    }

    @DisplayName("should throw an exception on updating massToLEO upon passing an empty string value")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenPassedEmptyStringToMassToGTO(String massToGTO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToGTO(massToGTO));
        assertEquals("massToGTO cannot be empty", exception.getMessage());
    }

    //assumption: sinceMassToLEO is a ratio, the value entered must be a number (passed and stored as a string)
    @DisplayName("should throw an exception on updating massToLEO upon passing non numeric characters")
    @ParameterizedTest
    @ValueSource(strings = {"123k", "oneKg", "1200lbs", "$124lb", "@2\\!*@!#@"})
    public void shouldThrowExceptionIfNonNumericCharactersPassedToMassToGTO(String massToGTO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToGTO(massToGTO));
        assertEquals("massToGTO must only contain numeric characters", exception.getMessage());
    }

    //massToOther
    @DisplayName("@DisplayName(\"should set massToOther upon passing a valid numerical string value\")")
    @Test
    public void shouldUpdateMassToOtherWhenPassedValidValue() {
        String massToOther = "2400";
        testRocket.setMassToOther(massToOther);
        assertEquals(testRocket.getMassToOther(), massToOther);
    }

    @DisplayName("getMassToOther() should return a string of shorter length leading/trailing/both passed along with string to setMassToOther()")
    @ParameterizedTest
    @ValueSource(strings = {"  2400", "2400  ", "  2400  "})
    public void shouldReturnMassToOtherOfSmallerLengthWhenPassedWithLeadingOrTrailingSpaces(String massToOther) {
        testRocket.setMassToOther(massToOther);
        assertTrue(massToOther.length() > testRocket.getMassToOther().length());
    }

    @DisplayName("should throw an exception on updating massToOther upon passing a null value")
    @Test
    public void shouldThrowExceptionWhenPassedNullToMassToOther() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> testRocket.setMassToOther(null));
        assertEquals("massToOther cannot be null", exception.getMessage());
    }

    @DisplayName("should throw an exception on updating massToOther upon passing an empty string value")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenPassedEmptyStringToMassToOther(String massToOther) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToOther(massToOther));
        assertEquals("massToOther cannot be empty", exception.getMessage());
    }

    //assumption: sinceMassToLEO is a ratio, the value entered must be a number (passed and stored as a string)
    @DisplayName("should throw an exception on updating massToOther upon passing non numeric characters")
    @ParameterizedTest
    @ValueSource(strings = {"123k", "oneKg", "1200lbs", "$124lb", "@2\\!*@!#@"})
    public void shouldThrowExceptionIfNonNumericCharactersPassedToMassToOther(String massToOther) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testRocket.setMassToOther(massToOther));
        assertEquals("massToOther must only contain numeric characters", exception.getMessage());
    }

    //Testing toString
    @DisplayName("should return a string representation of the values passed to object")
    @Test
    public void shouldReturnStringRepresentationOfValuesPassedToObject() {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("ISRO", 1978, "India" );
        Rocket rocket2 = new Rocket("GSLV Mk II", "India", manufacturer);
        rocket2.setMassToLEO("5000");
        rocket2.setMassToGTO("2700");
        rocket2.setMassToOther("1000");

        assertEquals("Rocket{name='GSLV Mk II', country='India', manufacturer='ISRO', massToLEO='5000', massToGTO='2700', massToOther='1000'}", rocket2.toString());
    }

    //TODO: Review added assignment methods
    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }
}