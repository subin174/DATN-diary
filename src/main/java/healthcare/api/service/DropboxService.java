//package healthcare.api.service;
//
//import com.dropbox.core.*;
//import com.dropbox.core.v2.DbxClientV2;
//import com.dropbox.core.v2.files.FileMetadata;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Service
//public class DropboxService {
//    @Value("${dropbox.appKey}")
//    private String dropboxAppKey; // Inject your Dropbox app key from application.properties or application.yml
//
//    @Value("${dropbox.appSecret}")
//    private String dropboxAppSecret; // Inject your Dropbox app secret from application.properties or application.yml
//
//    private final String DROPBOX_ACCESS_TOKEN_FILE = "dropboxAccessToken.txt";
//
//    public String uploadAudioToDropbox(MultipartFile audioFile) throws IOException, DbxException {
//        // Initialize Dropbox API
//        DbxRequestConfig config = new DbxRequestConfig("your-app-name");
//        DbxAppInfo appInfo = new DbxAppInfo(dropboxAppKey, dropboxAppSecret);
//        DbxWebAuth auth = new DbxWebAuth(config, appInfo);
//
//        // Load or authenticate Dropbox access token
//        String accessToken = loadAccessToken();
//        if (accessToken == null) {
//            accessToken = authenticateDropbox(auth);
//            saveAccessToken(accessToken);
//        }
//        DbxClientV2 client = new DbxClientV2(config, accessToken);
//        try  {
//            String fileName = audioFile.getOriginalFilename();
//            Path tempFile = Files.createTempFile("audio_", fileName.substring(fileName.lastIndexOf(".")));
//            Files.copy(audioFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
//
//            try (InputStream inputStream = new FileInputStream(tempFile.toFile())) {
//                FileMetadata uploadedFile = client.files().uploadBuilder("/" + fileName)
//                        .uploadAndFinish(inputStream);
//                return uploadedFile.getId();
//            }
//        }finally {
//            if (client != null) {
//                client.close();
//            }
//        }
//    }
//
//    private String authenticateDropbox(DbxWebAuth auth) throws IOException, DbxException {
//        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().build();
//        String authorizeUrl = auth.authorize(webAuthRequest);
//        System.out.println("1. Go to: " + authorizeUrl);
//        System.out.println("2. Click \"Allow\" (you might have to log in first)");
//        System.out.println("3. Copy the authorization code.");
//
//        // Get authorization code
//        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
//
//        // Exchange authorization code for access token
//        DbxAuthFinish authFinish = auth.finishFromCode(code);
//        return authFinish.getAccessToken();
//    }
//
//    private void saveAccessToken(String accessToken) throws IOException {
//        Files.write(Paths.get(DROPBOX_ACCESS_TOKEN_FILE), accessToken.getBytes());
//    }
//
//    private String loadAccessToken() throws IOException {
//        if (Files.exists(Paths.get(DROPBOX_ACCESS_TOKEN_FILE))) {
//            return new String(Files.readAllBytes(Paths.get(DROPBOX_ACCESS_TOKEN_FILE)));
//        }
//        return null;
//    }
//}
