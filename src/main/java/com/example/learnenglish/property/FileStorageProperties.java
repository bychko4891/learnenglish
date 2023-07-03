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

    public String getUploadDir() {
        return uploadDir;
    }
    public String getUploadAudio() {
        return uploadAudio;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    public void setUploadAudio(String uploadAudio) {
        this.uploadAudio = uploadAudio;
    }
}
