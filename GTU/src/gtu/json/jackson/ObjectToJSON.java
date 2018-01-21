package gtu.json.jackson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJSON {

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
        /**
         * Mapper can be used to convert object to JSON
         */
        ObjectMapper mapper = new ObjectMapper();

        Customer customer = new Customer();
        customer.setAge(29);
        customer.setFirstName("Hamidul");
        customer.setMiddleName("");
        customer.setLastName("Islam");
        customer.setContacts(Arrays.asList("8095185442", "9998887654", "1234567890"));

        /**
         * Now we can create JSON from customer object Into different forms We
         * can write in Console or we can create JSON as string Or we can write
         * JSON in file also See all the examples below
         */

        mapper.writeValue(System.out, customer);

        String jsonString = mapper.writeValueAsString(customer);

        mapper.writeValue(new File("customer.json"), customer);

        /**
         * To pretty print the above JSON use the below code. Uncomment the
         * below code to see the result
         */

        /**
         * mapper.writerWithDefaultPrettyPrinter().writeValue(System.out,
         * customer);
         * 
         * String prettyJson =
         * mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customer);
         * 
         * mapper.writerWithDefaultPrettyPrinter().writeValue(new
         * File("customer.json"), customer);
         */
    }

}