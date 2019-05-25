package rockets.model;

import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

public class Payload extends Entity {
    // Name refers to the name of the payload, e.g. Voyager 2
    private String name;
    // Destination indicates the target destination of the payload, e.g. "outside heliosphere"
    private String destination;
    // massKG indicates the mass of the payload, in kilograms
    private int massKG;
    // Type refers to the type of payload, e.g. satellites, space probes, sentient beings (e.g. humans/animals), cargo
    private String type;
    // isActive indicates the status of the payload, e.g. false for retired astronauts
    private boolean isActive;

    public Payload(String name, String destination, int massKG, String type, boolean isActive) {
        this.name = name;
        this.destination = destination;
        this.massKG = massKG;
        this.type = type;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notBlank(name, "name cannot be null or empty");
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        notBlank(destination, "destination cannot be null or empty");
        this.destination = destination;
    }

    public int getMassKG() {
        return massKG;
    }

    public void setMassKG(int massKG) throws IllegalArgumentException {
        if (massKG < 0)
            throw new IllegalArgumentException("massKG cannot be negative");
        this.massKG = massKG;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        notBlank(type, "type cannot be null or empty");
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Payload payload = (Payload) o;
//        return Objects.equals(name, payload.name) &&
//                Objects.equals(destination, payload.destination) &&
//                Objects.equals(type, payload.type) &&
//                Objects.equals(isActive, payload.isActive) &&
//                Objects.equals(massKG, payload.massKG);
        return Objects.equals(name, payload.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, destination, type, isActive, massKG);
    }
}