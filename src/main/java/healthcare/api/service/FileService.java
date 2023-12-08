package healthcare.api.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service
public class FileService {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    private final String CLIENT_ID = "9145e6effdd64dd";
    private final RestTemplate restTemplate;

    public FileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${dropbox.appKey}")
    private String dropboxAppKey; // Inject your Dropbox app key from application.properties or application.yml

    @Value("${dropbox.appSecret}")
    private String dropboxAppSecret; // Inject your Dropbox app secret from application.properties or application.yml

    private final String DROPBOX_ACCESS_TOKEN_FILE = "dropboxAccessToken.txt";
    //upload
    public String uploadImageToImgur(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID " + CLIENT_ID);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                IMGUR_UPLOAD_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode dataNode = rootNode.path("data");

        String imgLink = dataNode.path("link").asText();
        return imgLink;
    }

    //download
    public byte[] downloadImageFromImgur(String imageId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + CLIENT_ID);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                IMGUR_UPLOAD_URL + "/" + imageId,
                HttpMethod.GET,
                entity,
                String.class
        );

        String responseBody = responseEntity.getBody();

        // Process the Imgur API response to get the image URL
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        String imageUrl = jsonNode.path("data").path("link").asText();

        // Download the image
        ResponseEntity<byte[]> imageResponse = restTemplate.exchange(
                imageUrl,
                HttpMethod.GET,
                entity,
                byte[].class
        );
        return imageResponse.getBody();}

}
