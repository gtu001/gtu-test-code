package gtu.jersey;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
 
import org.glassfish.grizzly.http.server.HttpServer;
 
public class MyResourceTest {
 
    private HttpServer server;
    private WebTarget target;
 
    @Before
    public void setUp() throws Exception {
        server = ExtensionPointProxyFactory_Main.startServer();
 
        Client c = ClientBuilder.newClient();
        target = c.target(ExtensionPointProxyFactory_Main.BASE_URI);
    }
 
    @After
    public void tearDown() throws Exception {
        server.stop();
    }
 
    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @URLToImageTest
    public void testGetIt() {
        String responseMsg = target.path("myresource").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }
}