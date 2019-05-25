package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Payload;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;
    private static final String noNegativeK = "k cannot be negative";


    public RocketMiner(DAO dao) {
        this.dao = dao;
    }


    /**
     * TODO: to be implemented & tested!
     * Returns the top-k active rocket, as measured by number of launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {

        if (k < 0)
            throw new IllegalArgumentException(noNegativeK);
        ArrayList<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        HashMap<Rocket, Integer> countRockets = new HashMap<>();
        for (Launch launch : launches) {
            boolean inMap = countRockets.containsKey(launch.getLaunchVehicle());
            if (inMap) {
                // Get counter
                int count = countRockets.get(launch.getLaunchVehicle());
                countRockets.replace(launch.getLaunchVehicle(), ++count);
                // Add 1 to counter
            } else {
                // Add to map with 1 in counter
                countRockets.put(launch.getLaunchVehicle(), 1);
            }
        }
        // Demonstrated in https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
        Map<Rocket, Integer> sortedCountRockets = countRockets.entrySet().stream().sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        ArrayList<Rocket> sortedRocketList = new ArrayList<>();
        sortedCountRockets.forEach((key, value) -> sortedRocketList.add(key));
        return (k > sortedRocketList.size()) ? sortedRocketList : sortedRocketList.subList(0, k);
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {

        logger.info("find most reliable " + k + " launch service providers");
        if (k < 0)
            throw new IllegalArgumentException(noNegativeK);
        ArrayList<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        HashMap<LaunchServiceProvider, Integer[]> totalLaunches = new HashMap<>();

        // Counting the total number of launches a launch service provider has done
        for (Launch launch : launches) {
            boolean inMap = totalLaunches.containsKey(launch.getLaunchServiceProvider());
            if (inMap) { // If already in the map totalLaunches, add 1
                int currentSuccessful = totalLaunches.get(launch.getLaunchServiceProvider())[0];
                int currentTotal = totalLaunches.get(launch.getLaunchServiceProvider())[1];
                if (launch.getLaunchOutcome() == Launch.LaunchOutcome.SUCCESSFUL) {
                    currentSuccessful++;
                }
                totalLaunches.replace(launch.getLaunchServiceProvider(), new Integer[] {currentSuccessful, ++currentTotal});
            } else { // If not in the map totalLaunches (i.e. it's the first one found)
                int successful = 0;
                if (launch.getLaunchOutcome() == Launch.LaunchOutcome.SUCCESSFUL) {
                    successful++;
                }
                totalLaunches.put(launch.getLaunchServiceProvider(), new Integer[] {successful, 1});
            }
        }

        // Calculating ratio
        HashMap<LaunchServiceProvider, Double> reliabilityRatio = new HashMap<>();
        totalLaunches.forEach((key, value) -> reliabilityRatio.put(key, (value[0] * 1.0) / value[1]));

        //TODO: discuss this line of code
        // Demonstrated in https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
        Map<LaunchServiceProvider, Double> sortedReliabilityRatio = reliabilityRatio.entrySet().stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        List<LaunchServiceProvider> reliableProviders = new ArrayList<>();
        sortedReliabilityRatio.forEach((key, value) -> reliableProviders.add(key));
        return (k > reliableProviders.size()) ? reliableProviders : reliableProviders.subList(0, k);
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        if (k < 0)
            throw new IllegalArgumentException(noNegativeK);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the successful launch rate in <code>year</code> measured by the
     * number of successful launches and total number of launches
     *
     * @param year the year
     * @return the successful launch rate in BigDecimal with scale 2.
     */
    public BigDecimal successfulLaunchRateInYear(int year) {
        return BigDecimal.valueOf(0);
    }

    public String dominantCountry(String orbit) {
        logger.info("find most dominant country in orbit " + orbit);
        ArrayList<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        Map<String, ArrayList<Launch>> allLaunchesByCountry = new HashMap<>();

        for(Launch launch : launches) {
            if (launch.getOrbit().equals(orbit)) {
                Set<String> countries = allLaunchesByCountry.keySet();
                String launchCountry = launch.getLaunchServiceProvider().getCountry();
                if (countries.contains(launchCountry)) {
                    ArrayList<Launch> countryLaunches = allLaunchesByCountry.get(launchCountry);
                    countryLaunches.add(launch);
                    allLaunchesByCountry.put(launchCountry, countryLaunches);
                } else {
                    ArrayList<Launch> countryLaunches = new ArrayList<>();
                    countryLaunches.add(launch);
                    allLaunchesByCountry.put(launchCountry, countryLaunches);
                }
            }
        }

        Map<String, ArrayList<Launch>> sortedAllLaunches = allLaunchesByCountry
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getValue().size(), Comparator.reverseOrder()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        List<String> sortedCountries = new ArrayList<>(sortedAllLaunches.keySet());

        if (sortedCountries.size() > 1) {
            if (allLaunchesByCountry.get(sortedCountries.get(0)).size() == allLaunchesByCountry.get(sortedCountries.get(1)).size()) {
                //if top two countries have same number of rockets in orbit
                //return the one with more payload mass

                ArrayList<Launch> countryLaunches1 = allLaunchesByCountry.get(sortedCountries.get(0));
                ArrayList<Launch> countryLaunches2 = allLaunchesByCountry.get(sortedCountries.get(1));
                if (CalculatePayloadMass(countryLaunches1) > CalculatePayloadMass(countryLaunches2)) {
                    return sortedCountries.get(0);
                } else {
                    return sortedCountries.get(1);
                }
            } else {
                //return the top country on the list
                return sortedCountries.get(0);
            }
        } else {
            return sortedCountries.get(0);
        }
    }

    private int CalculatePayloadMass(ArrayList<Launch> countryLaunches) {
        int countryPayloadMass = 0;
        for(Launch launch : countryLaunches) {
            ArrayList<Payload> payloads = new ArrayList<Payload>();
            payloads.addAll(launch.getPayload());
            int launchPayloadMass = 0;
            for (Payload payload : payloads) {
                launchPayloadMass += payload.getMassKG();
            }
            countryPayloadMass += launchPayloadMass;
        }
        return countryPayloadMass;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {

        logger.info("find top " + k + "most expensive launches");
        ArrayList<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        //launches.sort((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
        Comparator<Launch> expensiveLaunchesComparator = (o1, o2) -> -o1.getPrice().compareTo(o2.getPrice());
        return launches.stream().sorted(expensiveLaunchesComparator).limit(k).collect(Collectors.toList());
    }

    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year)
    {
        // Throw IllegalArguementException if 'k' is -ve or zero.
        if (k < 0)
        {
            throw new IllegalArgumentException("The value of k cannot be negative.");
        }

        logger.info("Find the top " + k + " launch service providers that have the highest sales revenue in the year " + year + ".");

        // The launch price is being considered as the launch service provider's sales revenue.

        Collection<Launch> launches = dao.loadAll(Launch.class);
        Map<LaunchServiceProvider, Double> salesRevenue = new HashMap<>();

        for (Launch l : launches)
        {
            if (l.getLaunchDate().getYear() == year)
            {
                LaunchServiceProvider lsp = l.getLaunchVehicle().getManufacturer();
                double value = l.getPrice().doubleValue();

                if (salesRevenue.containsKey(lsp))
                {
                    double currentValue = salesRevenue.get(lsp).doubleValue();
                    salesRevenue.put(lsp, (currentValue + value));
                }
                else
                {
                    salesRevenue.put(lsp, value);
                }
            }
        }

        Map<LaunchServiceProvider, Double> sales = salesRevenue.entrySet().stream().sorted(Collections.reverseOrder(comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        List<LaunchServiceProvider> sortedRevenue = new ArrayList<>();
        int x = 0;
        for (LaunchServiceProvider key: sales.keySet())
        {
            if (x < k)
            {
                sortedRevenue.add(key);
                x++;
            }
            else
            {
                break;
            }
        }

        return sortedRevenue;
    }

    public Rocket rocketWithMostActivePayloads(String country) {
        ArrayList<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        int highestSoFar = 0;
        Rocket currentHighestRocket = null;
        for (Launch launch : launches) {
            int currentTotalActivePayloads = 0;
            if (launch.getPayload() != null && launch.getLaunchVehicle().getCountry().equals(country)) {
                for (Payload payload : launch.getPayload()) {
                    currentTotalActivePayloads += (payload.isActive()) ? 1 : 0;
                }
                if (currentTotalActivePayloads > highestSoFar)
                    currentHighestRocket = launch.getLaunchVehicle();
            }
        }
        return currentHighestRocket;
    }
}
