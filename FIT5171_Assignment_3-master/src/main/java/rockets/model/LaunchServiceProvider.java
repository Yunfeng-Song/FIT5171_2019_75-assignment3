package rockets.model;

import com.google.common.collect.Sets;
import static org.apache.commons.lang3.Validate.notBlank;


import java.util.HashSet;
import java.util.regex.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Set;

public class LaunchServiceProvider extends Entity
{
    private String name;

    private int yearFounded;

    private String country;

    private String headquarters;

    private Set<Rocket> rockets;


    public LaunchServiceProvider(){
        super();
    }

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param yearFounded
     * @param country
     */
    public LaunchServiceProvider(String name, int yearFounded, String country)
    {
        setName(name);
        setYearFounded(yearFounded);
        setCountry(country);

        rockets = Sets.newLinkedHashSet();
    }

    /**
     * Accessor method for Name field.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor method for yearFounded field.
     *
     * @return yearFounded
     */
    public int getYearFounded() {
        return yearFounded;
    }

    /**
     * Accessor method for country field.
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Accessor method for headquarters field.
     *
     * @return headquarters
     */
    public String getHeadquarters() {
        return headquarters;
    }

    /**
     * Accessor method for Set of rockets objects.
     *
     * @return object rocket
     */
    public Set<Rocket> getRockets()
    {
        return rockets;
    }

    // setName() was missing. Included by Zeeshan.
    /**
     * Mutator method for name field.
     *
     * @param name
     */
    public void setName(String name)
    {
        notBlank(name, "Name cannot be null or empty.");
        name = name.trim();
        this.name = name;
    }

    // setYearFounded() was missing. Included by Zeeshan.

    /**
     * Mutator method for yearFounded field.
     *
     * @param yearFounded
     */
    public void setYearFounded(int yearFounded)
    {
        if (yearFounded < 1000)
        {
            throw new IllegalArgumentException("Year founded cannot be less than 1000.");
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");

        String str = dateFormat.format(date);
        int currentYear = Integer.parseInt(str);
        if (yearFounded > currentYear)
            throw new IllegalArgumentException("Year founded cannot be in the future.");
        this.yearFounded = yearFounded;
    }

    // setCountry() was missing. Included by Zeeshan.

    /**
     * Mutator method for country field.
     *
     * @param country
     */
    public void setCountry(String country)
    {
        notBlank(country, "Country's name cannot be null or empty.");
        country = country.trim();
        if (!Pattern.matches("^[a-zA-Z,'. ]*$", country))
            throw new IllegalArgumentException("Country's name may only contain alphabets, comma, apostrophe and full stop.");

        this.country = country;
    }

    /**
     * Mutator method for headquarters field.
     *
     * @param headquarters
     */
    public void setHeadquarters(String headquarters)
    {
        notBlank(headquarters, "Headquarter's name cannot be null or empty.");
        headquarters = headquarters.trim();
        this.headquarters = headquarters;
    }

    /**
     * Mutator method for Set of rockets.
     *
     * @param rockets
     */
    public void setRockets(Set<Rocket> rockets)
    {
        Set<Rocket> rockets2 = new HashSet<>();
        if(rockets.isEmpty())
            throw new IllegalArgumentException("Set<Rocket> cannot be empty.");
        for (Rocket rocket : rockets) {
            rocket.setManufacturer(this);
        }
        this.rockets = rockets;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider launchServiceProvider = (LaunchServiceProvider) o;
        // LaunchServiceProvider that = (LaunchServiceProvider) o;
        return Objects.equals(name, launchServiceProvider.name) &&
                Objects.equals(yearFounded, launchServiceProvider.yearFounded) &&
                Objects.equals(country, launchServiceProvider.country) &&
                Objects.equals(headquarters, launchServiceProvider.headquarters) &&
                Objects.equals(rockets, launchServiceProvider.rockets);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, yearFounded, country);
    }
}
