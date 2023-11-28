package healthcare.api.service;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DropboxService {
    //Appkey
    @Value("${dropbox.appKey}")
    private String dropboxAppKey;
    //AppSecret
    @Value("${dropbox.appSecret}")
    private String dropboxAppSecret;
    //accessToken
    @Value("${dropbox.accessToken}")
    private String accessToken;
    private final String DROPBOX_ACCESS_TOKEN_FILE = "token.txt";

    // using access token
    public String uploadAudioFileToDropboxV2(MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            return "redirect:/uploadFailure";
        }

        try {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            DbxClientV2 client = new DbxClientV2(config, accessToken);

            try (InputStream in = audioFile.getInputStream()) {
                FileMetadata metadata = client.files().uploadBuilder("/" + audioFile.getOriginalFilename())
                        .uploadAndFinish(in);


                SharedLinkMetadata sharedLinkMetadata = null;
                try {
                    sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(metadata.getPathDisplay());
                } catch (CreateSharedLinkWithSettingsErrorException ex) {
                    // Handle the case where the shared link already exists
                    if (ex.errorValue.isSharedLinkAlreadyExists()) {
                        // The shared link already exists, you can use the existing link
                        sharedLinkMetadata = ex.errorValue.getSharedLinkAlreadyExistsValue().getMetadataValue();
                    } else {
                        // Handle other errors
                        throw new RuntimeException(ex);
                    }
                }

                // Now you can use sharedLinkMetadata.getUrl() or handle it as needed
                String sharedLinkUrl = sharedLinkMetadata.getUrl();
                System.out.println("Shared Link URL: " + sharedLinkUrl);
                return sharedLinkUrl;
            } catch (UploadErrorException e) {
                throw new RuntimeException(e);
            } catch (DbxException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/uploadFailure";
        }
    }
    // using key
    public String uploadAudioToDropbox(MultipartFile audioFile) throws IOException, DbxException {
        // Initialize Dropbox API
        DbxRequestConfig config = new DbxRequestConfig("diary-app");
        DbxAppInfo appInfo = new DbxAppInfo(dropboxAppKey, dropboxAppSecret);
        DbxWebAuth auth = new DbxWebAuth(config, appInfo);

        // Load or authenticate Dropbox access token
        String accessToken = loadAccessToken();
        if (accessToken == null) {
            accessToken = authenticateDropbox(auth);
            saveAccessToken(accessToken);
        }
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            String fileName = audioFile.getOriginalFilename();
            Path tempFile = Files.createTempFile("audio_", fileName.substring(fileName.lastIndexOf(".")));
            Files.copy(audioFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            try (InputStream inputStream = new FileInputStream(tempFile.toFile())) {
                FileMetadata uploadedFile = client.files().uploadBuilder("/" + fileName)
                        .uploadAndFinish(inputStream);
                SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings("/" + fileName);
                // Extract the shared URL
                String sharedUrl = sharedLinkMetadata.getUrl();
                // Return the modified shared URL
                return sharedUrl;
            }
        }
        catch (UploadErrorException e){
            throw new RuntimeException("Error uploading file to Dropbox", e);
        }

    }

    private String authenticateDropbox(DbxWebAuth auth) throws IOException, DbxException {
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().build();
        String authorizeUrl = auth.authorize(webAuthRequest);
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        System.out.print("Enter the authorization code here: ");

        // Get authorization code
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        // Exchange authorization code for access token
        DbxAuthFinish authFinish = auth.finishFromCode(code);
        return authFinish.getAccessToken();
    }

    private void saveAccessToken(String accessToken) throws IOException {
        Files.write(Paths.get(DROPBOX_ACCESS_TOKEN_FILE), accessToken.getBytes());
    }

    private String loadAccessToken() throws IOException {
        if (Files.exists(Paths.get(DROPBOX_ACCESS_TOKEN_FILE))) {
            return new String(Files.readAllBytes(Paths.get(DROPBOX_ACCESS_TOKEN_FILE)));
        }
        return null;
    }
}
