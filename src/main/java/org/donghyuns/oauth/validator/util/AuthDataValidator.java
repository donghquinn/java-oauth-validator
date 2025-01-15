package org.donghyuns.oauth.validator.util;

import org.donghyuns.oauth.validator.biz.user.model.response.TelegramUserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class AuthDataValidator {
    @Value("${spring.oauth.secret.telegram}")
    private String botToken;

    private static final long DEFAULT_EXPIRATION_SECONDS = 86400; // 1 day
    private static final String HMAC_ALGO = "HmacSHA256";

    private long inValidateDataAfter = DEFAULT_EXPIRATION_SECONDS;


    /**
     * Validates Telegram data.
     *
     * @param authDataMap The data received from Telegram Login Widget.
     * @return Validated and parsed user data.
     * @throws Exception If the data is invalid.
     */
    public TelegramUserDto validate(Map<String, String> authDataMap) throws Exception {
        assertBotToken();
        assertDataShape(authDataMap);

        String hashFromTelegram = authDataMap.remove("hash");
        String dataStr = getFinalDataStr(authDataMap);

        boolean isDataValid = verifyHMAC(hashFromTelegram, dataStr);
        if (!isDataValid) {
            log.error("Unauthorized! Data could not be validated.");
            throw new SecurityException("Unauthorized! Data could not be validated.");
        }

        if (hasDataExpired(authDataMap)) {
            log.error("Unauthorized! The data has expired.");
            throw new SecurityException("Unauthorized! The data has expired.");
        }

        return parseTelegramUserData(authDataMap);
    }

    private void assertBotToken() {
        if (this.botToken == null || this.botToken.isEmpty()) {
            log.error("Error! Bot token is required.");
            throw new IllegalStateException("Error! Bot token is required.");
        }
    }

    private void assertDataShape(Map<String, String> authDataMap) {
        if (!authDataMap.containsKey("auth_date") ||
                (!authDataMap.containsKey("id") && !authDataMap.containsKey("user"))) {
            log.error("Invalid! Incomplete data provided.");
            throw new IllegalArgumentException("Invalid! Incomplete data provided.");
        }
    }

    private boolean verifyHMAC(String hashFromTelegram, String dataStr) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);

        SecretKey secretKey = new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(botToken.getBytes(StandardCharsets.UTF_8)), "SHA256");

        mac.init(secretKey);

        byte[] computedHash = mac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));

        byte[] telegramHash = hexStringToByteArray(hashFromTelegram.trim().toLowerCase());

        return MessageDigest.isEqual(computedHash, telegramHash);
    }

    private boolean hasDataExpired(Map<String, String> authDataMap) {
        long authDate = Long.parseLong(authDataMap.get("auth_date"));
        long currentTime = Instant.now().getEpochSecond();
        return (currentTime - authDate) > inValidateDataAfter;
    }

    private TelegramUserDto parseTelegramUserData(Map<String, String> authDataMap) {
        TelegramUserDto userData = new TelegramUserDto();
        userData.setId(authDataMap.get("id"));
        userData.setFirstName(authDataMap.get("first_name"));
        userData.setLastName(authDataMap.get("last_name"));
        userData.setUsername(authDataMap.get("username"));
        userData.setAuthDate(authDataMap.get("auth_date"));
        return userData;
    }

    private String getFinalDataStr(Map<String, String> authDataMap) {
        TreeMap<String, String> sortedMap = new TreeMap<>(authDataMap);
        StringBuilder sb = new StringBuilder();
        sortedMap.forEach((key, value) -> sb.append(key).append("=").append(value).append("\n"));
        return sb.toString().trim();
    }

    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
