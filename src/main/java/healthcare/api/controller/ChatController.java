//package healthcare.api.controller;
//
//import healthcare.entity.NotifiMesWs;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    @Autowired
//    SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public NotifiMesWs send(final NotifiMesWs message) throws Exception {
//        return message;
//    }
//
//    @MessageMapping("/private")
//    public void sendToSpecificUser(@Payload NotifiMesWs message) {
//        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
//    }
//}
