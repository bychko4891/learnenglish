package com.example.learnenglish.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    /**
     *
     * @param  //файл
     * @param // путь к файлу
     * @param  fileName оригинальное имя файла
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName){

        // Создать новое имя файла
        String realPath = path + "/" + FileNameUtils.getFileName(fileName);

        // Использовать оригинальное имя файла
        // String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        // Определяем, существует ли файл родительского каталога
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            // Сохранить файл
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
