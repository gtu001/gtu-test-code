package gtu.webservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;



public class SendXmlTest2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080/tee-middleware/rws/receipt/import");
        WebTarget target = client.target("http://localhost:8080/tee-middleware/rws");
        String responseMsg = target.path("receipt").request().get(String.class);
        System.out.println(responseMsg);
        System.out.println("done...");
    }
}
