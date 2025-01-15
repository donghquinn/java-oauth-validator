package org.donghyuns.oauth.validator.biz.user.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignonRequest {
    @NotNull
    private String oauthName;

    private String recomCode;

    @NotNull
    private Map<String, String>  authData; // Get Telegram Auth Data from Client
}
