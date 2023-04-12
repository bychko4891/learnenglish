package com.example.learnenglish.service;

import org.springframework.stereotype.Service;
import java.util.UUID;
@Service
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
