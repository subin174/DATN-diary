package healthcare;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class HealthCareApplication {
	@Bean
	FirebaseMessaging firebaseMessaging()throws IOException{
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("diary-3b91a-firebase-adminsdk-9pn87-dd4d3281b5.json").getInputStream());
		FirebaseOptions firebaseOptions =FirebaseOptions.builder().setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "diary-app");
		return FirebaseMessaging.getInstance(app);
	}

	public static void main(String[] args) {
		SpringApplication.run(HealthCareApplication.class, args);
	}
	/*@Bean
	public Cloudinary cloudinary(){
		Cloudinary c =new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "dst4cbcr7",
				"api_key", "369687286919363",
				"api_secret", "InAtDuCHmBaeENUVqPYlhU3KyWo",
				"secure", true
		));
		return c;
	}*/
}
