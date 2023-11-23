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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class HealthCareApplication {


    public HealthCareApplication() throws IOException {
    }

    @Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("diary-3b91a-firebase-adminsdk-9pn87-dd4d3281b5.json").getInputStream());
		FirebaseOptions firebaseOptions =FirebaseOptions.builder().setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "diary-app");
		return FirebaseMessaging.getInstance(app);
	}

	public static void main(String[] args)throws IOException {

		ClassLoader classLoader = HealthCareApplication.class.getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource("ServiceAccountKey.json")).getFile());
		FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://diary-3b91a-default-rtdb.asia-southeast1.firebasedatabase.app")
				.build();
		FirebaseApp.initializeApp(options);
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
