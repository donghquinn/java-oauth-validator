package org.donghyuns.oauth.validator.biz.user.controller;

import org.donghyuns.oauth.validator.biz.user.model.request.SignonRequest;
import org.donghyuns.oauth.validator.biz.user.model.response.SignonDto;
import org.donghyuns.oauth.validator.biz.user.service.UserService;
import org.donghyuns.oauth.validator.common.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/telegram")
@Tag(name = "OAUTH-Controller", description = "Oauth Validating Controller")
public class MemberController {
    private final UserService userService;

    @PostMapping("/validator")
    @Operation(summary = "Signon", description = "Oauth Sign-up and Sign-in")
    public Response<SignonDto> memberSignon(@RequestBody @Valid SignonRequest signonRequest, HttpServletRequest request){
        return new Response<>(userService.signonUser(signonRequest, request));
    }
}
