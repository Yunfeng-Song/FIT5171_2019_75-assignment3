package rockets.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;

public class Launch extends Entity {
    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }

    private LocalDate launchDate;

    private Rocket launchVehicle;

    private LaunchServiceProvider launchServiceProvider;

    private Set<Payload> payload;

    private String launchSite;

    private String orbit;

    private String function;

    private BigDecimal price;

    private LaunchOutcome launchOutcome;

    public Launch() {}

    public Launch(LocalDate launchDate, Rocket rocket, LaunchServiceProvider serviceProvider, String orbit) {
        this.launchDate = launchDate;
        this.launchVehicle = rocket;
        this.launchServiceProvider = serviceProvider;
        this.orbit = orbit;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {
        this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        this.launchServiceProvider = launchServiceProvider;
    }

    public Set<Payload> getPayload() {
        return payload;
    }

    public void setPayload(Set<Payload> payload) {
        if (payload != null) {
            if (payload.isEmpty())
                throw new IllegalArgumentException("payload set cannot be set to empty");
        }
        this.payload = payload;
    }

    public String getLaunchSite() {
        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        notBlank(launchSite, "launchSite can't be null or empty");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {
        notBlank(orbit, "orbit can't be null or empty");
        this.orbit = orbit;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        notBlank(function, "function can't be null or empty");
        this.function = function;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) throws IllegalArgumentException {
        if (price.doubleValue() < 0)
            throw new IllegalArgumentException("price can't be negative");
        this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        this.launchOutcome = launchOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
