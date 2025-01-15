package org.donghyuns.oauth.validator.biz.user.repository;

import org.donghyuns.oauth.validator.biz.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByUserId(String userId);
}
