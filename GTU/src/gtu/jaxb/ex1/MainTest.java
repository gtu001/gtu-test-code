package gtu.jaxb.ex1;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class MainTest {
    public static void main(String args[]) {
        try {
            // -- get context instance
            JAXBContext ctx = JAXBContext.newInstance("gtu.jaxb.ex1");
            ObjectFactory of = new ObjectFactory();

            // -- create Suger object
            Suger ws = of.createSuger();

            // -- set values
            ws.setColor("Cream color");
            ws.setSweetness((byte) 20);

            // -- convert ws to JAXBElement
            JAXBElement<Suger> je = of.createSugers(ws);
            // -- get Marshaller
            Marshaller m = ctx.createMarshaller();

            // -- marshal JAXBElement into StringWriter
            StringWriter xml = new StringWriter();
            m.marshal(je, xml);

            // -- get xml string
            String xmlString = xml.toString();
            System.err.println(xmlString);

            // -- separator
            System.err.println("\n********\n********\n");

            // -- clear old content in StringWriter
            StringBuffer sb = xml.getBuffer();
            sb.setLength(0);

            // -- marshal JAXBElement into StringWriter with format
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(je, xml);
            System.err.println(xml.toString());

            // -- get context instance
            JAXBContext context = JAXBContext.newInstance(Suger.class);
            // -- get unmarshaller
            Unmarshaller um = context.createUnmarshaller();
            // -- unmarshal xml string to JAXB element
            JAXBElement<Suger> root = um.unmarshal(new StreamSource(new StringReader(xmlString)), Suger.class);

            // -- get object from JAXB element
            Suger ws2 = root.getValue();

            // -- separator
            System.err.println("\n********\n********\n");

            // -- output object value
            System.err.println(" ws2 color = " + ws2.getColor());
            System.err.println(" ws2 sweetness = " + ws2.getSweetness());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
