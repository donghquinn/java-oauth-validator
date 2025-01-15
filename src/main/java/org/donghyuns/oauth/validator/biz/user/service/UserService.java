package org.donghyuns.oauth.validator.biz.user.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.donghyuns.oauth.validator.biz.user.entity.OauthEntity;
import org.donghyuns.oauth.validator.biz.user.entity.UserEntity;
import org.donghyuns.oauth.validator.biz.user.entity.UserOauthEntity;
import org.donghyuns.oauth.validator.biz.user.model.request.SignonRequest;
import org.donghyuns.oauth.validator.biz.user.model.response.SignonDto;
import org.donghyuns.oauth.validator.biz.user.model.response.TelegramUserDto;
import org.donghyuns.oauth.validator.biz.user.repository.OauthRepository;
import org.donghyuns.oauth.validator.biz.user.repository.UserOauthRepository;
import org.donghyuns.oauth.validator.biz.user.repository.UserRepository;
import org.donghyuns.oauth.validator.exception.CustomException;
import org.donghyuns.oauth.validator.exception.ErrorCode;
import org.donghyuns.oauth.validator.util.AuthDataValidator;
import org.donghyuns.oauth.validator.util.CommonUtil;
import org.donghyuns.oauth.validator.util.GenerateCode;
import org.donghyuns.oauth.validator.util.JwtUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OauthRepository oauthRepository;
    private final UserOauthRepository userOauthRepository;

    private final AuthDataValidator authDataValidator;

    private final JwtUtil jwtUil;
    private final GenerateCode generateCode;
    private final CommonUtil commonUtil;

    @Transactional
    public SignonDto signonUser(SignonRequest signonRequest, HttpServletRequest request) {
        String oauthName = signonRequest.getOauthName();

        String ip = CommonUtil.getCurrentIp(request);

        Optional<OauthEntity> oauth = oauthRepository.findFirstByOauthNameAndOauthStatus(oauthName, 1);

        if (oauth.isEmpty()) {
            throw new CustomException(ErrorCode.OAUTH_NOT_FOUND);
        }

        OauthEntity oauthData = oauth.get();

        TelegramUserDto telegramData;

        // Telegram Validate, whether if the user already signed up or not
        try {
            telegramData = authDataValidator.validate(signonRequest.getAuthData());
            log.debug("Telegram Authorization Finished: {}", telegramData);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.AUTH_FAILURE);
        }

        String encryptOauthId = CommonUtil.getBase64EncodeString(telegramData.getId());

        Optional<UserOauthEntity> userOauth = userOauthRepository.findFirstByOauthAuthIdAndOauthSeq(encryptOauthId, oauthData.getOauthSeq());

        // Create New User if Oauth User does not exist
        if (userOauth.isEmpty()){
            String encryptName = CommonUtil.getBase64EncodeString(telegramData.getUsername());

            UserEntity newUser = createNewUser(encryptName, ip);

            createNewUserOauth(newUser.getUserId(), oauthData.getOauthSeq(), oauthName);

            String accessToken = jwtUil.createAccessToken(newUser.getUserId());

            return SignonDto.builder().accessToken(accessToken).build();
        }

        UserOauthEntity userOauthData = userOauth.get();

        Optional<UserEntity> user = userRepository.findFirstByUserId(userOauthData.getUserId());

        // memberOauth 는 존재하는데 member은 존재하지 않을 때
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UserEntity userInfo = user.get();

        String accessToken = jwtUil.createAccessToken(userInfo.getUserId());

        return SignonDto.builder().accessToken(accessToken).build();
    }


    protected  UserEntity createNewUser(String userName, String ip){
       return UserEntity.builder()
               .userName(userName)
               .userIp(ip)
               .build();
    }

    @Async
    @Transactional
    // memberOauth 테이블에 생성 - oauthName == oauth == TELEGRAM
    protected void createNewUserOauth(String userId, Long oauthSeq, String oauthAuthId){
        try {
            userOauthRepository.save(UserOauthEntity.builder()
                    .userId(userId)
                    .oauthSeq(oauthSeq)
                    .oauthAuthId(oauthAuthId)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.USER_SIGNON_ERROR);
        }
    }
 }
