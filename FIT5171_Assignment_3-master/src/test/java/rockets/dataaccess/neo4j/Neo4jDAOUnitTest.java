package rockets.dataaccess.neo4j;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import rockets.dataaccess.DAO;
import rockets.model.*;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

import java.io.File;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jDAOUnitTest {
    private static final String TEST_DB = "target/test-data/test-db";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    private static LaunchServiceProvider esa;
    private static LaunchServiceProvider spacex;
    private Rocket rocket;
    private User user;
    private Launch launch;

    @BeforeAll
    public void initializeNeo4j() {
        EmbeddedDriver driver = createEmbeddedDriver(TEST_DB);
//        ServerControls embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder().newServer();
//        GraphDatabaseService dbService = embeddedDatabaseServer.graph();
//        EmbeddedDriver driver = new EmbeddedDriver(dbService);
        sessionFactory = new SessionFactory(driver, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(sessionFactory);
    }

    @BeforeEach
    public void setup() {
        esa = new LaunchServiceProvider("ESA", 1970, "Europe");
        spacex = new LaunchServiceProvider("SpaceX", 2002, "USA");
        rocket = new Rocket("F9", "USA", spacex);
        Rocket rocket1 = new Rocket("F9", "USA", spacex);
        user = new User("Tony", "Stark", "tony.stark@starkindustries.com");
        launch = new Launch(LocalDate.parse("1977-08-20"), null, spacex, "Pluto");

        Set launches = new HashSet();
        launches.add(launch);
        rocket.setLaunches(launches);

    }

    private static EmbeddedDriver createEmbeddedDriver(String fileDir) {
        File file = new File(fileDir);
        Configuration configuration = new Configuration.Builder()
                .uri(file.toURI().toString()) // For Embedded
                .build();
        EmbeddedDriver driver = new EmbeddedDriver();
        driver.configure(configuration);
        return driver;
    }

    @Test
    public void shouldCreateNeo4jDAOSuccessfully() {

        assertNotNull(dao);
    }
    
    @Test
    public void shouldCreateARocketSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");
        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);
        LaunchServiceProvider manufacturer = graphRocket.getManufacturer();
        assertNotNull(manufacturer.getId());
        assertEquals(rocket.getWikilink(), graphRocket.getWikilink());
        assertEquals(spacex, manufacturer);
    }

    @Test
    public void shouldUpdateRocketAttributeSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");

        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);

        String newLink = "http://adifferentlink.com";
        rocket.setWikilink(newLink);
        dao.createOrUpdate(rocket);
        graphRocket = dao.load(Rocket.class, rocket.getId());
        assertEquals(newLink, graphRocket.getWikilink());

    }

    @Test
    public void shouldNotSaveTwoSameRockets() {
        assertNull(spacex.getId());

        Collection<LaunchServiceProvider> manufacturers123 = dao.loadAll(LaunchServiceProvider.class);

        Rocket rocket1 = new Rocket("F9", "USA", spacex);
        Set launches = new HashSet();
        launches.add(launch);
        rocket1.setLaunches(launches);
        Rocket rocket2 = new Rocket("F9", "USA", spacex);
        rocket2.setLaunches(launches);
        assertEquals(rocket1, rocket2);
        dao.createOrUpdate(rocket1);
        assertNotNull(spacex.getId());
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
        Collection<LaunchServiceProvider> manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        dao.createOrUpdate(rocket2);
        manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());

    }

    @Test
    public void shouldLoadAllRockets() {
        Set<Rocket> rockets = Sets.newHashSet(
                new Rocket("Ariane4", "France", esa),
                new Rocket("F5", "USA", spacex),
                new Rocket("BFR", "USA", spacex)
        );

        for (Rocket r : rockets) {

            Set launches = new HashSet();
            launches.add(launch);
            r.setLaunches(launches);

            dao.createOrUpdate(r);
        }

        Collection<Rocket> loadedRockets = dao.loadAll(Rocket.class);
        assertEquals(rockets.size(), loadedRockets.size());
        for (Rocket r : rockets) {
            assertTrue(rockets.contains(r));
        }

    }

    @Test
    public void shouldCreateALaunchSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));

    }


    @Test
    public void shouldUpdateLaunchAttributesSuccessfully() {

        Collection<Launch> launchhk = dao.loadAll(Launch.class);

        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2017, 1, 1));
        //launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);

        Launch loadedLaunch = launches.iterator().next();
        assertNull(loadedLaunch.getFunction());

        launch.setFunction("experimental");
        dao.createOrUpdate(launch);
        launches = dao.loadAll(Launch.class);
        assertEquals(1, launches.size());
        loadedLaunch = launches.iterator().next();
        assertEquals("experimental", loadedLaunch.getFunction());

    }

    @Test
    public void shouldDeleteRocketWithoutDeleteLSP() {
        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertNotNull(rocket.getManufacturer().getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @Test
    public void shouldSaveARocketBeforeALSPDoesAcrossSessionsNotCreateDuplicateRockets() {
        assertEquals(spacex, rocket.getManufacturer());
        spacex.getRockets().add(rocket);
        dao.createOrUpdate(spacex);
        assertEquals(1, dao.loadAll(Rocket.class).size());

        dao.close();

        initializeNeo4j();

        rocket.setId(null);
        spacex.setId(null);
        dao.createOrUpdate(spacex);
        assertEquals(1, dao.loadAll(Rocket.class).size());

    }






    // Added by Zeeshan
    @Test
    public void shouldCreateAUserSuccessfully()
    {
        User userGraph = dao.createOrUpdate(user);
        assertNotNull(userGraph.getEmail());
        assertEquals(user, userGraph);
        assertEquals(user.getEmail(), userGraph.getEmail());

    }

    // Added by Zeeshan
    @Test
    public void shouldCreateLaunchServiceProviderSuccessfully()
    {
        LaunchServiceProvider graphlsp = dao.createOrUpdate(esa);
        assertNotNull(graphlsp.getId());
        assertEquals(esa, graphlsp);

    }



    // Added by Zeeshan
    @Test
    public void shouldLoadAllUsers()
    {
        Set<User> users = Sets.newHashSet(new User("Zeeshan", "Arif", "adsfer@eywtr.com"), new User("Brad", "Pitt", "uryetrw@yuetr.com"));
        for (User u : users)
        {
            dao.createOrUpdate(u);
        }

        Collection<User> usersFromDB = dao.loadAll(User.class);
        assertEquals(users.size(), usersFromDB.size());

        for (User u : users)
        {
            assertTrue(usersFromDB.contains(u));
        }
    }

    // Added by Zeeshan
    @Test
    public void shouldLoadAllLaunches()
    {
        Rocket rocket1 = new Rocket("F9", "USA", spacex);
        Set<Launch> launches = Sets.newHashSet(new Launch(LocalDate.parse("1975-11-02"), rocket, spacex, "Earth"), new Launch(LocalDate.parse("1969-04-10"), rocket1, spacex, "Pluto"));

        for (Launch l : launches)
        {
            dao.createOrUpdate(l);
        }
        Collection<Launch> launchesFromDB = dao.loadAll(Launch.class);
        assertEquals(launches.size() + 1, launchesFromDB.size());
        for (Launch l : launches)
        {
            assertTrue(launchesFromDB.contains(l));
        }

    }

    // Added by Zeeshan
    @Test
    public void shouldLoadAllLaunchServiceProviders()
    {
        Rocket rocket2 = new Rocket("F9", "USA", spacex);
        Set<LaunchServiceProvider> launchServiceProviders = Sets.newHashSet(new LaunchServiceProvider("NASA", 1958, "USA"), new
                LaunchServiceProvider("SpaceX", 2002, "USA"));
        for (LaunchServiceProvider lsp : launchServiceProviders)
        {
            dao.createOrUpdate(lsp);
        }

        Collection<LaunchServiceProvider> lspsFromDB = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(launchServiceProviders.size(), lspsFromDB.size());

        for (LaunchServiceProvider lsp : launchServiceProviders)
        {
            assertTrue(lspsFromDB.contains(lsp)); //check that this line is correct
        }

    }

    // Added by Luke
    @Test
    public void shouldLoadAllPayloads() {
        Set<Payload> payloads = new HashSet<>();
        payloads.add(new Payload("Mars Rover", "Mars", 80, "rover", true));
        payloads.add(new Payload("Alan the Researcher", "International Space Station", 85, "passenger", false));
        payloads.add(new Payload("Billy the Researcher's dog", "International Space Station", 15, "passenger", true));
        payloads.add(new Payload("Voyager", "Outer solar system", 50, "satellite", true));

        for (Payload payload : payloads) {
            dao.createOrUpdate(payload);
        }
        Collection<Payload> payloadsFromDB = dao.loadAll(Payload.class);
        assertEquals(payloadsFromDB.size(), payloads.size());

        for (Payload payload : payloads) {
            assertTrue(payloadsFromDB.contains(payload));
        }

    }


    // Added by Zeeshan
    @Test
    public void shouldUpdateUserSuccessfully()
    {
        User user1 = new User("Peter", "Parker", "peter.parker@monash.edu");
        dao.createOrUpdate(user1);

        String newEmail = "peter.parker@yahoo.com";
        user1.setEmail(newEmail);
        dao.createOrUpdate(user1);
        User graphUser = dao.load(User.class, user1.getId());
        assertEquals(newEmail, graphUser.getEmail());

    }

//    @Test
//    public void test(){
//        Collection<Rocket> a = dao.loadAll(Rocket.class);
//        dao.createOrUpdate(rocket);
//        a = dao.loadAll(Rocket.class);
//
//        Rocket rocket1 = new Rocket("F9", "USA", spacex);
//        Set launches = new HashSet();
//        launches.add(launch);
//        rocket1.setLaunches(launches);
//        dao.createOrUpdate(rocket1);
//        a = dao.loadAll(Rocket.class);
//        int b = 1;
//    }

    // Added by Zeeshan
    @Test
    public void shouldDeleteARocketSuccessfully()
    {

        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertNotNull(rocket.getManufacturer().getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());

        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(rocket);

        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());

        Collection<Launch> launches = dao.loadAll(Launch.class);
        launches = dao.loadAll(Launch.class);
        assertEquals(1, launches.size());
        dao.delete(launch);
        launches = dao.loadAll(Launch.class);
        assertEquals(0, launches.size());
    }

    // Added by Zeeshan
    @Test
    public void shouldDeleteAllLaunchesSuccessfully()
    {
        dao.createOrUpdate(launch);
        assertNotNull(launch.getId());
        assertFalse(dao.loadAll(Launch.class).isEmpty());
        dao.delete(launch);
        assertTrue(dao.loadAll(Launch.class).isEmpty());
    }

    // Added by Zeeshan
    @Test
    public void shouldDeleteALaunchServiceProviderSuccessfully()
    {
        dao.createOrUpdate(esa);
        assertNotNull(esa.getId());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(esa);
        assertTrue(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    // Added by Zeeshan
    @Test
    public void shouldDeleteAUserSuccessfully()
    {
        dao.createOrUpdate(user);
        assertNotNull(user.getId());
        assertFalse(dao.loadAll(User.class).isEmpty());
        dao.delete(user);
        assertTrue(dao.loadAll(User.class).isEmpty());
    }










    @AfterEach
    public void tearDown() {

        session.purgeDatabase();
    }

    @AfterAll
    public void closeNeo4jSession() {
        session.clear();
        sessionFactory.close();
    }
}