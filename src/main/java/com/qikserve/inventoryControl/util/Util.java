package com.qikserve.inventoryControl.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Util {

    public static ModelMapper modelMapper = new ModelMapper();

    public static String createID() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
