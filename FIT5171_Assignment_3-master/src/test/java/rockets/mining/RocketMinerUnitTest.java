package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.mining.RocketMiner;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Payload;
import rockets.model.Rocket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Map.Entry;

import static java.util.Map.Entry.comparingByValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList( // lsps means Launch Service Providers
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe ")
        );

        Set<Payload> payloads1 = new HashSet<>();
        payloads1.add(new Payload("Mars Rover", "Mars", 80, "rover", true));
        payloads1.add(new Payload("Alan the Researcher", "International Space Station", 85, "passenger", false));
        payloads1.add(new Payload("Billy the Researcher's dog", "International Space Station", 15, "passenger", true));
        Set<Payload> payloads2 = new HashSet<>();
        payloads2.add(new Payload("Voyager", "Outer solar system", 50, "satellite", true));
        Set<Payload> payloads3 = new HashSet<>();
        payloads3.add(new Payload("Voyager 2", "Outer solar system", 50, "satellite", false));
        List<Set<Payload>> payloadList = new ArrayList<>();
        payloadList.add(payloads1);
        payloadList.add(payloads2);
        payloadList.add(payloads3);
        while (payloadList.size() <= 10) {
            payloadList.add(null);
        }

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};

        //countries
        String[] countries = new String[] {"USA", "Australia", "India"};

        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, countries[i%3], lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};

        // index of launch outcome
        Launch.LaunchOutcome[] launchOutcomes = new Launch.LaunchOutcome[] {Launch.LaunchOutcome.FAILED, Launch.LaunchOutcome.SUCCESSFUL};
        int[] outcomeIndex = new int[] {1, 1, 1, 1, 0, 1, 0, 1, 0, 1};

        //orbits
        String[] orbits = new String[] {"LEO", "GTO", "MLE"};

        //price
        List<BigDecimal> prices = new ArrayList<>();
        prices.add(new BigDecimal(83491.1234354));
        prices.add(new BigDecimal(68943.23454));
        prices.add(new BigDecimal(65442.2345));
        prices.add(new BigDecimal(54334.234542));
        prices.add(new BigDecimal(32405.5432));
        prices.add(new BigDecimal(10000.3456423));
        prices.add(new BigDecimal(19423.12));
        prices.add(new BigDecimal(24901.1234312));
        prices.add(new BigDecimal(29384.123453));
        prices.add(new BigDecimal(24345.1234));

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch launch = new Launch();
            launch.setLaunchDate(LocalDate.of(2017, months[i], 1));
            launch.setLaunchVehicle(rockets.get(rocketIndex[i]));
            launch.setLaunchSite("VAFB");
            launch.setOrbit(orbits[i%3]);
            launch.setLaunchServiceProvider(launch.getLaunchVehicle().getManufacturer()); // Review this line (multiple method calls)
            launch.setLaunchOutcome(launchOutcomes[outcomeIndex[i]]);
            launch.setPrice(prices.get(i));
            launch.setPayload(payloadList.get(i));
            spy(launch);
            return launch;
        }).collect(Collectors.toList());
    }

    @ParameterizedTest
    @ValueSource (ints = {-1})
    public void shouldThrowExceptionWhenKIsNegativeForMostRecentLaunches(int k) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> miner.mostRecentLaunches(k));
        assertEquals("k cannot be negative", exception.getMessage());
    }

    // Example test
    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10, 11}) // min, nom, max, max+
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        //assertEquals(k, loadedLaunches.size()); //what if there are less than k launches in the db?
        assertFalse(k < loadedLaunches.size());
        //assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
        assertEquals(sortedLaunches.subList(0, loadedLaunches.size()), loadedLaunches); // Review this modification of example code
    }

    @ParameterizedTest
    @ValueSource (ints = {-1}) // min-
    public void shouldThrowExceptionWhenKIsNegativeForMostActiveRockets(int k) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> miner.mostLaunchedRockets(k));
        assertEquals("k cannot be negative", exception.getMessage());
    }

    //Written by Luke: Workhorse test
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 4, 5, 6}) //min, min+, nom, max-, max, max+
    public void shouldReturnTopMostActiveRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> launchList = new ArrayList<>(launches);
        // count rockets
        Map<Rocket, Long> countedRockets = launchList.stream().collect(
                Collectors.groupingBy(Launch::getLaunchVehicle,
                        Collectors.mapping(Launch::getLaunchVehicle, Collectors.counting())));
        // checking that returned results are > results not returned
        List<Rocket> loadedRockets = miner.mostLaunchedRockets(k);
        for (Rocket rocket : loadedRockets) {
            Long numberOfLaunches = countedRockets.get(rocket);
            // for each rocket in countedRockets, ensure numberOfLaunches is > non returned results (if not in loadedRockets)
            logger.info("Rocket name: " + rocket.getName());
            countedRockets.forEach((key, value) -> {
                if (!loadedRockets.contains(key)) {
                    assertTrue(numberOfLaunches > value, "The rocket: " + rocket.getName() +
                            " is not a top most active rocket"); // Should this be >= ?
                    logger.info("K = " + k + " From master list (test): " + key.getName() + ": " + value + " AND From method: " + rocket.getName() + ": " + numberOfLaunches);
                } else {
                    logger.info("Didn't compare " + rocket.getName() + " with " + key.getName());
                }
            });
        }
        //assertEquals(k, loadedRockets.size(), "The returned list is not the correct size"); //Can't assertEquals because DB size may be less than k
        assertFalse(k < loadedRockets.size(), "The returned list is not the correct size");
    }

    @ParameterizedTest
    @ValueSource (ints = {-1})
    public void shouldThrowExceptionWhenKIsNegativeForMostReliableLsps(int k) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> miner.mostReliableLaunchServiceProviders(k));
        assertEquals("k cannot be negative", exception.getMessage());
    }

    // Written by Luke; BestPerformed test
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4}) //min, min+, nom, max-, max, max+
    public void shouldReturnTopMostReliableLaunchServiceProviders(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> launchList = new ArrayList<>(launches);
        // Calculate reliability ratios
        Map<LaunchServiceProvider, Double> lspRatioMasterMap = launchList.stream().collect(
                Collectors.groupingBy(Launch::getLaunchServiceProvider,
                        Collectors.mapping(Launch::getLaunchOutcome, Collectors.averagingDouble(outcome -> {
                            return (outcome == Launch.LaunchOutcome.SUCCESSFUL) ? 1 : 0;
                        }))));
        // Check that returned results are > results not returned
        List<LaunchServiceProvider> mostReliableLoadedLsps = miner.mostReliableLaunchServiceProviders(k);
        for (LaunchServiceProvider lsp : mostReliableLoadedLsps) {
            Double reliabilityRatio = lspRatioMasterMap.get(lsp);
            logger.info("LSP: " + lsp.getName());
            lspRatioMasterMap.forEach((key, value) -> {
                if (!mostReliableLoadedLsps.contains(key)) {
                    assertTrue(reliabilityRatio > value, "The launch service provider: " + lsp.getName() +
                            " is not a most reliable launch service provider");
                }
            });
        }
        //assertEquals(k, mostReliableLoadedLsps.size(), "The returned list is not the correct size"); // Can't assertEquals because DB size may be less than k
        assertFalse(k < mostReliableLoadedLsps.size(), "The returned list is not the correct size");
    }


    // Added by Zeeshan
    @DisplayName("Should throw exception when 'k' is negative.")
    @ParameterizedTest
    @ValueSource(ints = {-1})
    public void shouldThrowExceptionWhenKIsNegative(int k)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.highestRevenueLaunchServiceProviders(k, 2017));
        assertEquals("The value of k cannot be negative.", exception.getMessage());
    }

    // Added by Zeeshan
    @DisplayName("Should return the top most launch service providers with the highest sales revenue.")
    @ParameterizedTest
    @ValueSource(ints = {0, 3, 4, 5})
    public void shouldReturnTopKLaunchServiceProvidersWithHighestRevenue(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> launchList = new ArrayList<>(launches);
        List<LaunchServiceProvider> highestSalesRevenue = miner.highestRevenueLaunchServiceProviders(k, 2017);

        assertTrue(highestSalesRevenue.size() <= k, "The top " + k + " launch service providers with the highest revenue.");
    }

    //Written By: Navjot; DominantCountry
    @ParameterizedTest
    @ValueSource(strings = {"LEO", "GTO", "MLE"})
    public void shouldReturnDominantCountryWhenPassedAnOrbit(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> allLaunches = new ArrayList<>(launches);

        Map<String, Integer> countryLaunchCount = new HashMap<>();
        for (Launch launch: launches) {
            LaunchServiceProvider lsp = launch.getLaunchServiceProvider();
            Set<String> allCountries = countryLaunchCount.keySet();
            if (!allCountries.contains(lsp.getCountry())) {
                countryLaunchCount.put(lsp.getCountry(), 1);
            } else {
                Integer launchCount = countryLaunchCount.get(lsp.getCountry());
                countryLaunchCount.put(lsp.getCountry(), launchCount + 1);
            }
        }

        Set<Entry<String, Integer>> entries = countryLaunchCount.entrySet();
        TreeMap<String, Integer> sorted = new TreeMap<>(countryLaunchCount);
        Set<Entry<String, Integer>> mappings = sorted.entrySet();

        Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String,Integer>>()
        {
            @Override
            public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(entries);
        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());
        for(Entry<String, Integer> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        String dominantCountry = miner.dominantCountry(orbit);

        assertEquals(sortedByValue.keySet().iterator().next(), dominantCountry);
    }

    //Written By Navjot; Most Expensive Launches
    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void shouldReturnMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> allLaunches = new ArrayList<>(launches);
        Comparator<Launch> expensiveLaunchesComparator = (o1, o2) -> -o1.getPrice().compareTo(o2.getPrice());
        List<Launch> sortedLaunches = allLaunches
                .stream()
                .sorted(expensiveLaunchesComparator)
                .limit(k)
                .collect(Collectors.toList());

        List<Launch> mostExpensiveMinerLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, mostExpensiveMinerLaunches.size());
        assertEquals(sortedLaunches, mostExpensiveMinerLaunches);
    }


    @ParameterizedTest
    @ValueSource (strings = {"USA"})
    public void shouldReturnRocketWithTopMostActivePayloads (String country) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        Rocket topRocketWithPayload = miner.rocketWithMostActivePayloads(country);
        assertEquals(topRocketWithPayload, rockets.get(0));
    }

    @ParameterizedTest
    @ValueSource (strings = {"Australia"})
    public void shouldReturnNullIfNoMatchingCountry (String country) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        Rocket topRocketWithPayload = miner.rocketWithMostActivePayloads(country);
        assertEquals(topRocketWithPayload, null);
    }
}