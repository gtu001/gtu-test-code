package gtu.json.jackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
 
public class CreateJSON2 {
 
    public static void main(String[] args) throws JsonProcessingException {
 
        ObjectMapper mapper = new ObjectMapper();
 
        ArrayNode arrayNode = mapper.createArrayNode();
 
        /**
         * Create three JSON Objects objectNode1, objectNode2, objectNode3, objectNode4
         * Add all these three objects in the array
         */
 
        ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("bookName", "Java");
        objectNode1.put("price", "100");
 
        ObjectNode objectNode2 = mapper.createObjectNode();
        objectNode2.put("bookName", "Spring");
        objectNode2.put("price", "200");
 
        ObjectNode objectNode3 = mapper.createObjectNode();
        objectNode3.put("bookName", "Liferay");
        objectNode3.put("price", "500");
 
        ArrayNode authorsArray = mapper.createArrayNode();
        ObjectNode author1 = mapper.createObjectNode();
        author1.put("firstName","Hamidul");
        author1.put("middleName","");
        author1.put("lastName","Islam");
 
        ObjectNode author2 = mapper.createObjectNode();
        author2.put("firstName","Richard");
        author2.put("middleName","");
        author2.put("lastName","Sezove");
 
        authorsArray.add(author1);
        authorsArray.add(author2);
 
        ObjectNode objectNode4 = mapper.createObjectNode();
        objectNode4.putPOJO("authors", authorsArray);
 
        /**
         * Array contains JSON Objects
         */
        arrayNode.add(objectNode1);
        arrayNode.add(objectNode2);
        arrayNode.add(objectNode3);
        arrayNode.add(objectNode4);
 
        /**
         * We can directly write the JSON in the console.
         * But it wont be pretty JSON String
         */
        //System.out.println(arrayNode.toString());
 
        /**
         * To make the JSON String pretty use the below code
         */
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode));
 
    }
 
}