package healthcare.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DropboxService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadAudioCloud(MultipartFile audioFile) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(audioFile.getBytes(),
                ObjectUtils.asMap("resource_type", "auto", "transformation", new Transformation().fetchFormat("mp3")));

        // Get the public URL of the uploaded audio file
        return (String) uploadResult.get("url");
    }

}
