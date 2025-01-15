package org.donghyuns.oauth.validator.util;

import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class GenerateCode {
    public  String generateRandomCode(Integer size) {
        Random random = new Random();

        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return NanoIdUtils.randomNanoId(random, alphabet, size);
    }
}
