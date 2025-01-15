package org.donghyuns.oauth.validator.biz.user.repository;

import org.donghyuns.oauth.validator.biz.user.entity.OauthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthRepository extends JpaRepository<OauthEntity, Long> {
    Optional<OauthEntity> findFirstByOauthNameAndOauthStatus(String oauthName, Integer oauthStatus);
}
