package rockets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    @DisplayName("All launch service provider rockets should have a manufacturer with the same value")
    public void rocketManufacturerShouldEqualLaunchServiceProviderRocket() {
        LaunchServiceProvider lsp = new LaunchServiceProvider("ULA", 1990, "USA");
        LaunchServiceProvider lsp2 = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket rocket = new Rocket("Falcon", "USA", lsp);
        HashSet<Rocket> rockets = new HashSet<>();
        rockets.add(rocket);
        lsp2.setRockets(rockets);
        assertEquals(rocket.getManufacturer(), lsp2); //In the LSP class, setRocket should set the rocket.setManufacturer(this)
    }
}
