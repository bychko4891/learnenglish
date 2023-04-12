package com.example.learnenglish.service;
import java.util.UUID;
public class FileNameUtils {

    /**
     * Получить суффикс файла
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * Создать новое имя файла
     * @param  fileOriginName имя исходного файла
     * @return
     */
    public static String getFileName(String fileOriginName){
        return UUIDUtils.getUUID() + FileNameUtils.getSuffix(fileOriginName);
    }
}
