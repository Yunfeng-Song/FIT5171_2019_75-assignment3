package rockets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.INCOMING;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

@NodeEntity
@CompositeIndex(properties = {"name", "country", "manufacturer"}, unique = true)
public class Rocket extends Entity {
    @Property(name="name")
    private String name;

    @Property(name="country")
    private String country;

    @Relationship(type = "MANUFACTURES", direction = INCOMING)
    private LaunchServiceProvider manufacturer;

    @Property(name="massToLEO")
    private String massToLEO;

    @Property(name="massToGTO")
    private String massToGTO;

    @Property(name="massToOther")
    private String massToOther;

    @Property(name="firstYearFlight")
    private int firstYearFlight;

    @Property(name="lastYearFlight")
    private int latestYearFlight;

    @Relationship(type = "PROVIDES", direction = OUTGOING)
    @JsonIgnore
    private Set<Launch> launches;

    private Rocket() { super();}

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param country
     * @param manufacturer
     */
    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
        setName(name);
        setCountry(country);
        setManufacturer(manufacturer);

        launches = Sets.newLinkedHashSet();
    }

    //TODO: Compare this (newly added from assignment repo) constructor with the above and consider replacing
//    public Rocket(String name, String country, LaunchServiceProvider manufacturer) {
//        notNull(name);
//        notNull(country);
//        notNull(manufacturer);
//
//        this.name = name;
//        this.country = country;
//        this.manufacturer = manufacturer;
//    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public LaunchServiceProvider getManufacturer() {
        return manufacturer;
    }

    public String getMassToLEO() { return massToLEO; }

    public String getMassToGTO() {
        return massToGTO;
    }

    public String getMassToOther() {
        return massToOther;
    }

    public void setName(String name) {
        notNull(name,  "rocket name cannot be null");
        notBlank(name, "rocket name cannot be empty");
        this.name = name.trim();
    }

    public void setCountry(String country) {
        notNull(country, "rocket manufacture country cannot be null");
        notBlank(country, "rocket manufacture country cannot be empty");
        this.country = country.trim();
    }

    public void setManufacturer(LaunchServiceProvider manufacturer) {
        notNull(manufacturer, "rocket manufacturer cannot be null");
        //notBlank(manufacturer, "rocket manufacturer cannot be empty");
        this.manufacturer = manufacturer;
    }

    public void setMassToLEO(String massToLEO) {
        notNull(massToLEO, "massToLEO cannot be null");
        notBlank(massToLEO, "massToLEO cannot be empty");
        if (!Pattern.matches("^([0-9]*)+$", massToLEO.trim()))
            throw new IllegalArgumentException("massToLEO must only contain numeric characters");
        this.massToLEO = massToLEO.trim();
    }

    public void setMassToGTO(String massToGTO) {
        notNull(massToGTO, "massToGTO cannot be null");
        notBlank(massToGTO, "massToGTO cannot be empty");
        if (!Pattern.matches("^([0-9]*)+$", massToGTO.trim()))
            throw new IllegalArgumentException("massToGTO must only contain numeric characters");
        this.massToGTO = massToGTO.trim();
    }

    public void setMassToOther(String massToOther) {
        notNull(massToOther, "massToOther cannot be null");
        notBlank(massToOther, "massToOther cannot be empty");
        if (!Pattern.matches("^([0-9]*)+$", massToOther.trim()))
            throw new IllegalArgumentException("massToOther must only contain numeric characters");
        this.massToOther = massToOther.trim();
    }

    public int getFirstYearFlight() {
        return firstYearFlight;
    }

    public int getLatestYearFlight() {
        return latestYearFlight;
    }

    public void setFirstYearFlight(int firstYearFlight) {
        this.firstYearFlight = firstYearFlight;
    }

    public void setLatestYearFlight(int latestYearFlight) {
        this.latestYearFlight = latestYearFlight;
    }

    public Set<Launch> getLaunches() {
        return launches;
    }

    public void setLaunches(Set<Launch> launches) {
        this.launches = launches;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name) &&
                Objects.equals(country, rocket.country) &&
                Objects.equals(manufacturer, rocket.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, manufacturer);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", manufacturer='" + manufacturer.getName() + '\'' +
                ", massToLEO='" + massToLEO + '\'' +
                ", massToGTO='" + massToGTO + '\'' +
                ", massToOther='" + massToOther + '\'' +
                '}';
    }
}
