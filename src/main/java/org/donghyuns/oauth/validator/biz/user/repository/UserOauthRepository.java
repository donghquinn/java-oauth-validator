package org.donghyuns.oauth.validator.biz.user.repository;

import org.donghyuns.oauth.validator.biz.user.entity.UserOauthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOauthRepository extends JpaRepository<UserOauthEntity, Long> {
    Optional<UserOauthEntity> findFirstByOauthAuthIdAndOauthSeq(String oauthAuthId, Long oauthSeq);
}
