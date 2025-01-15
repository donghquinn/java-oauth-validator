package org.donghyuns.oauth.validator.biz.user.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignonDto {
    private String accessToken;
    private String refreshToken;
}
