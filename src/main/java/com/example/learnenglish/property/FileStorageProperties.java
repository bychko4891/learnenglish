package com.example.learnenglish.property;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String uploadAudio;
    private String uploadUserAvatar;
    private String uploadWordImage;


    public String getUploadUserAvatar() {
        return uploadUserAvatar;
    }
    public String getUploadDir() {
        return uploadDir;
    }
    public String getUploadAudio() {
        return uploadAudio;
    }
    public void setUploadUserAvatar(String uploadUserAvatar) {
        this.uploadUserAvatar = uploadUserAvatar;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    public void setUploadAudio(String uploadAudio) {
        this.uploadAudio = uploadAudio;
    }

    public String getUploadWordImage() {
        return uploadWordImage;
    }

    public void setUploadWordImage(String uploadWordImage) {
        this.uploadWordImage = uploadWordImage;
    }
}
