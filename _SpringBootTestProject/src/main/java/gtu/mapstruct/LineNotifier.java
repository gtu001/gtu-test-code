//package gtu.mapstruct;
//
//import de.codecentric.boot.admin.event.ClientApplicationEvent;
//import de.codecentric.boot.admin.event.ClientApplicationRegisteredEvent;
//import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
//import de.codecentric.boot.admin.notify.AbstractEventNotifier;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ParserContext;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//@Slf4j
//@Component
//public class LineNotifier extends AbstractEventNotifier {
//    @Autowired
//    private LineProperties lineProperties;
//    private static final String DEFAULT_MESSAGE = "#{application.name} (#{application.id}) is #{to.status}";
//    private final SpelExpressionParser parser = new SpelExpressionParser();
//
//    private Expression message;
//    private List<String> notifyStatuses = Arrays.asList("UP", "DOWN", "OFFLINE");
//
//    @Override
//    protected void doNotify(ClientApplicationEvent event) throws Exception {
//        if (lineProperties.isEnabled() == false) {
//            return;
//        }
//        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
//        if (event instanceof ClientApplicationRegisteredEvent) {
//            ClientApplicationRegisteredEvent registeredEvent = (ClientApplicationRegisteredEvent) event;
////            System.out.println(registeredEvent.getApplication());// Application [id=2a87974b, name=boot-test, managementUrl=http://SAMPC:5566, healthUrl=http://SAMPC:5566/health, serviceUrl=http://SAMPC:5566]
////            System.out.println(registeredEvent.getType());// REGISTRATION
////            System.out.println(registeredEvent.getApplication().getServiceUrl());// http://SAMPC:5566
////            System.out.println(registeredEvent.getApplication().getStatusInfo().getStatus());// UNKNOWN
//        }
//        if (event instanceof ClientApplicationStatusChangedEvent) {
//            ClientApplicationStatusChangedEvent statusChangedEvent = (ClientApplicationStatusChangedEvent) event;
//            String msg = message.getValue(event, String.class); // boot-test (2a87974b) is UP
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
//            headers.add("Authorization", String.format("%s %s", "Bearer", lineProperties.getChannelToken()));
//
//            HashMap object = new HashMap<>();
//            object.put("to", lineProperties.getTo());
//            List messages = new ArrayList();
//            HashMap message = new HashMap<>();
//            message.put("type", "text");
//            message.put("text", msg);
//            messages.add(message);
//            object.put("messages", messages);
//
//            HttpEntity<HashMap> entity = new HttpEntity<HashMap>(object, headers);
//            ResponseEntity<String> response = restTemplate.exchange(
//                    "https://api.line.me/v2/bot/message/push",
//                    HttpMethod.POST, entity, String.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                System.out.println(response.getBody());
//            }
//        }
//    }
//}