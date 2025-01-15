package org.donghyuns.oauth.validator.biz.user.model.response;

import lombok.Data;

@Data
public class TelegramUserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String authDate;
}
