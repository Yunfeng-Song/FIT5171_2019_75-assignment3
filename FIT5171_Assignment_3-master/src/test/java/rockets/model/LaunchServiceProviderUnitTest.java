package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class LaunchServiceProviderUnitTest
{
    private LaunchServiceProvider target;

    @BeforeEach
    public void setUp()
    {
        target = new LaunchServiceProvider("SpaceX", 2002, "USA");
    }

    // Positive test case for setName() method.
    @DisplayName("Should allow name with alphabets, numbers and special characters in setName() method.")
    @ParameterizedTest
    @ValueSource(strings = {"SpaceX1", "N.A.S.A", "Star@Gazer!"})
    public void shouldAllowAlphabetsNumbersAndSpecialCharactersInName(String name)
    {
        target.setName(name);
    }

    // Parameterized test to check for empty string value in "name" field.
    @DisplayName("Should throw exception when an empty name is passed to the setName() function.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   ", "    ", "     "})
    public void shouldThrowExceptionWhenSetNameToEmpty(String name)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setName(name));
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    // Test to check for null value in "name" field.
    @DisplayName("Should throw exception when null is passed to the setName() function.")
    @Test
    public void shouldThrowExceptionWhenSetNameToNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setName(null));
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    // Positive test case for setYearFounded() method.
    @DisplayName("Should allow positive numbers greater than 1000 and less than the current year.")
    @ParameterizedTest
    @ValueSource(ints = {1958, 2017, 2002, 2018, 2019})
    public void shouldAllowPositiveNumbersBetweenThousandAndCurrentYear(int yearFounded)
    {
        target.setYearFounded(yearFounded);
    }

    // Parameterized test should throw exception when invalid year is passed to setYearFounded().
    @DisplayName("Should throw exception if yearFounded is less than 1000.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, 999})
    public void shouldThrowExceptionIfYearFoundedIsLessThanOne(int yearFounded)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setYearFounded(yearFounded));
        assertEquals("Year founded cannot be less than 1000.", exception.getMessage());
    }

    // Parameterized test should throw exception when year founded exceeds current year in the setYearFounded() method.
    @DisplayName("Should throw exception if yearFounded is greater than current year.")
    @ParameterizedTest
    @ValueSource(ints = {2020, 3900, 6241})
    public void shouldThrowExceptionIfYearFoundedIsGreaterThanCurrentYear(int yearFounded)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setYearFounded(yearFounded));
        assertEquals("Year founded cannot be in the future.", exception.getMessage());
    }

    // Positive test case for setCountry() method.
    @DisplayName("Should allow alphabets, comma, apostrophe and full stop to pass to setCountry() method")
    @ParameterizedTest
    @ValueSource(strings = {"USA", "US,A", "U'SA", "U.S.A", "U S A"})
    public void shouldAllowAlphabetsCommaApostropheAndFullStopInSetCountry(String country)
    {
        target.setCountry(country);
        // Any other character will throw an IllegalArgumentException.
    }

    // Parameterized test to check whether blank value is set to country field.
    @DisplayName("Should throw exception when blank value is passed to setCountry() method.")
    @ValueSource(strings = {"", " ", "  ", "   "})
    public void shouldThrowExceptionWhenCountrySetToBlank(String country)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setCountry(country));
        assertEquals("Country's name cannot be null or empty.", exception.getMessage());
    }

    // Test to check whether null is passed to setCountry method.
    @DisplayName("Should throw exception when null is passed to setCountry() method.")
    @Test
    public void shouldThrowExceptionWhenSetCountryToNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setCountry(null));
        assertEquals("Country's name cannot be null or empty.", exception.getMessage());
    }

    // Parameterized test should throw exception when characters other then alphabets, comma, apostrophe and full stop are passed to setCountry() method.
    @DisplayName("Should throw exception when characters other than alphabets, comma, apostrophe and full stop are passed to setCountry() method.")
    @ParameterizedTest
    @ValueSource(strings = {"U3A", "U#A", "1ONDON_234"})
    public void shouldThrowExceptionWhenInvalidCharacterPassedToSetCountry(String country)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setCountry(country));
        assertEquals("Country's name may only contain alphabets, comma, apostrophe and full stop.", exception.getMessage());
    }

    // Positive test case for setHeadquarters() method.
    @DisplayName("Should allow all characters when passed to setHeadquarters() method.")
    @ParameterizedTest
    @ValueSource(strings = {"22, Baker Street, LDN-201923", "44, Bourke Street, Melbourne, 3125", "253@/ Tyerw82356@#%@"})
    public void shouldAllowAllCharactersToPassToSetHeadquarters(String headquarters)
    {
        target.setHeadquarters(headquarters);
    }

    // Parameterized test should throw exception when blank value is set to setHeadquarters() method.
    @DisplayName("Should throw exception when blank value passed to setHeadquarters() method.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   "})
    public void shouldThrowExceptionIfBlankValueSetToHeadquarters(String headquarters)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setHeadquarters(headquarters));
        assertEquals("Headquarter's name cannot be null or empty.", exception.getMessage());
    }

    // Test should throw exception when null is passed to setHeadquarters() method.
    @DisplayName("Should throw exception when null is passed to setHeadquarters() method.")
    @Test
    public void shouldThrowExceptionWhenNullPassedToHeadquarters()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setHeadquarters(null));
        assertEquals("Headquarter's name cannot be null or empty.", exception.getMessage());
    }

    // Parameterized test should not throw exception when alphabets, ",", "-", " ' " and "." characters are passed to setHeadquarters() method.
    @DisplayName("Should not throw exception when alphabets, comma, hyphen apostrophe and full stop characters are passed to setHeadquarters() method")
    @ParameterizedTest
    @ValueSource(strings = {"USA", "U,SA", "U'SA", "U.S.A"})
    public void shouldAllowAlphabetsAndCertainSpecialCharactersToHeadquarters(String headquarters)
    {
        target.setHeadquarters(headquarters);
        // May throw IllegalArgumentException for certain special characters.
    }

    // Positive test should allow to pass a valid rocket object in Set<rockets>.
    @DisplayName("Should allow valid rocket object to pass in Set<rockets>.")
    @Test
    public void shouldAllowValidRocketObjectInSet()
    {
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1978, "USA");
        Rocket rocket1 = new Rocket("Atlas-Agena", "USA", manufacturer);
        Set<Rocket> rockets = new HashSet<>();
        rockets.add(rocket1);
        target.setRockets(rockets);
    }

    // Test should not allow an empty Set<rockets>.
    @DisplayName("Should throw exception when Set<rocket> is empty.")
    @Test
    public void shouldThrowExceptionWhenSetRocketsEmpty()
    {
        Set<Rocket> rockets = new HashSet<>();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setRockets(rockets));
        assertEquals("Set<Rocket> cannot be empty.", exception.getMessage());
    }

    /**
     * Creates a new Launch Service Provider with values for name, yearFounded and country fields.
     *
     * @return object of LaunchServiceProvider
     */
    public LaunchServiceProvider initializeLaunchServiceProvider()
    {
        return new LaunchServiceProvider("SpaceX", 2002, "USA");
    }

    // Test should return True when two launch service provider objects are equal.
    @DisplayName("Should return True when two Launch Service Providers are equal.")
    @Test
    public void shouldReturnTrueWhenLaunchServiceProvidersAreEqual()
    {
        LaunchServiceProvider launchServiceProvider1 = initializeLaunchServiceProvider();
        LaunchServiceProvider launchServiceProvider2 = initializeLaunchServiceProvider();
        assertTrue(launchServiceProvider1.equals(launchServiceProvider2));
    }

    // Test should return False when two launch service providers are not equal.
    @DisplayName("Should return False when two Launch Service Providers are not equal.")
    @Test
    public  void shouldReturnFalseWhenLaunchServiceProvidersAreNotEqual()
    {
        LaunchServiceProvider launchServiceProvider1 = initializeLaunchServiceProvider();
        LaunchServiceProvider launchServiceProvider2 = initializeLaunchServiceProvider();
        launchServiceProvider2.setName("NASA");
        assertFalse(launchServiceProvider1.equals(launchServiceProvider2));
    }

    // Test should return False when object is not a non-launch service provider object.
    @DisplayName("Should return false when compared to a non-launch service provider object.")
    @Test
    public void shouldReturnFalseWhenComparedToNonLaunchServiceProviderObject()
    {
        assertFalse(initializeLaunchServiceProvider().equals("LaunchServiceProvider"));
    }

    // Test should return False when launch service provider object is null.
    @DisplayName("Should return false when compared to a null launch service provider.")
    @Test
    public void shouldReturnFalseWhenComparedToNulLaunchServiceProviderObject()
    {
        assertFalse(initializeLaunchServiceProvider().equals(null));
    }

    // Hash code for two of the same Launch Service Provider objects should be equal.
    @DisplayName("HashCode should be equal for the same Launch Service Provider objects.")
    @Test
    public void shouldBeEqualWhenSameLaunchServiceProviderObjects()
    {
        LaunchServiceProvider lsp1 = initializeLaunchServiceProvider();
        LaunchServiceProvider lsp2 = initializeLaunchServiceProvider();
        assertEquals(lsp1.hashCode(), lsp2.hashCode());
    }

    // Hash code for two different Launch Service Provider objects should not be equal.
    @DisplayName("Hashcode should be unequal for different Launch Service Provider objects.")
    @Test
    public void shouldBeUnEqualWhenDifferentLaunchServiceProviderObjects()
    {
        LaunchServiceProvider lsp3 = initializeLaunchServiceProvider();
        LaunchServiceProvider lsp4 = initializeLaunchServiceProvider();
        lsp4.setName("NASA");
        assertNotEquals(lsp3.hashCode(), lsp4.hashCode());
    }
}
