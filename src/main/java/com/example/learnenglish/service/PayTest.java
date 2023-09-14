package com.example.learnenglish.service;

import org.apache.commons.codec.digest.HmacUtils;

public class PayTest {

    public static String hmacWithApacheCommons(String algorithm, String data, String key) {
        String hmac = new HmacUtils(algorithm, key).hmacHex(data);
        return hmac;
    }

    public static void main(String[] args) {

        String data = "test_merchant;www.market.ua;DH783023;1415379863;1547.36;UAH;Процессор Intel Core i5-4670 3.4GHz;Память Kingston DDR3-1600 4096MB PC3-12800;1;1;1000;547.36";
        String algorithm = "HmacMD5";
        String key = "dhkq3vUi94{Z!5frxs(02ML";

        System.out.println(PayTest.hmacWithApacheCommons(algorithm, data, key));

    }
}
