//package healthcare.api.service;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import healthcare.entity.NotificationMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FirebaseMessagingService {
//    @Autowired
//    private FirebaseMessaging firebaseMessaging;
//
//    public String sendNotificationBytoken(NotificationMessage notificationMessage) {
//        Notification notification = Notification
//                .builder().setTitle(notificationMessage.getTitle())
//                .setBody(notificationMessage.getBody())
//                .setImage(notificationMessage.getImage())
//                .build();
//        Message message = com.google.firebase.messaging.Message.builder()
//                .setToken(notificationMessage.getRecipientToken())
//                .setNotification(notification)
//                .putAllData(notificationMessage.getData())
//                .build();
//        try{
//            firebaseMessaging.send(message);
//            return "Suscess Sending Notification";
//        }
//        catch(FirebaseMessagingException e){
//            e.printStackTrace();
//            return "Error Sending Notification";
//        }
//
//    }
//}
