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
    private String uploadWebImage;
    private String uploadAudio;
    private String uploadUserAvatar;
    private String uploadVocabularyPageImage;
    private String uploadCategoryImage;


    public String getUploadUserAvatar() {
        return uploadUserAvatar;
    }
    public String getUploadWebImage() {
        return uploadWebImage;
    }
    public String getUploadAudio() {
        return uploadAudio;
    }
    public void setUploadUserAvatar(String uploadUserAvatar) {
        this.uploadUserAvatar = uploadUserAvatar;
    }

    public void setUploadWebImage(String uploadWebImage) {
        this.uploadWebImage = uploadWebImage;
    }
    public void setUploadAudio(String uploadAudio) {
        this.uploadAudio = uploadAudio;
    }

    public String getUploadVocabularyPageImage() {
        return uploadVocabularyPageImage;
    }

    public void setUploadVocabularyPageImage(String uploadVocabularyPageImage) {
        this.uploadVocabularyPageImage = uploadVocabularyPageImage;
    }

    public String getUploadCategoryImage() {
        return uploadCategoryImage;
    }

    public void setUploadCategoryImage(String uploadCategoryImage) {
        this.uploadCategoryImage = uploadCategoryImage;
    }
}
