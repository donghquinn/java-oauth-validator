package org.donghyuns.oauth.validator.biz.user.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "user_oauth_table")
public class UserOauthEntity {
    @Id
    @Column(name = "user_oauth_seq", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userOauthSeq;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "oauthSeq", length = 1, nullable = false)
    private Long oauthSeq;

    @Column(name = "oauth_auth_id") // Telegram Auth Id
    private String oauthAuthId;

    @Comment("1 - Active, 0 - Deactive, 2 - Delete")
    @ColumnDefault("1")
    @Column(name = "user_oauth_status", length = 1) // 1 - Active, 0 - deactivated, 2 - deleted
    private Integer userOauthStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
