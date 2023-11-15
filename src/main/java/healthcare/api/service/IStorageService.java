package healthcare.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IStorageService {
    String getStorageFileName(MultipartFile file, String id);

    void store(MultipartFile file, String storeFileName);

    Resource loadAsResource(String filename);

    Path load(String filename);

    void delete(String storeFilename) throws Exception;

    void init();
}
