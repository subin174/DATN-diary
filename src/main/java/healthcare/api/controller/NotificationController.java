//package healthcare.api.controller;
//
//import healthcare.api.service.FirebaseMessagingService;
//import healthcare.entity.NotificationMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/notification")
//public class NotificationController {
//    @Autowired
//    FirebaseMessagingService firebaseMessagingService;
//    @PostMapping
//    public String sendNotificationByToken(@RequestBody NotificationMessage notificationMessage){
//        return firebaseMessagingService.sendNotificationBytoken(notificationMessage);
//    }
//}
