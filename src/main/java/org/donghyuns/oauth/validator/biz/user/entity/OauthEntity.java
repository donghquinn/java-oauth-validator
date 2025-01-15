package org.donghyuns.oauth.validator.biz.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "oauth_table")
public class OauthEntity {
    @Id
    @Column(name = "oauth_seq", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oauthSeq;

    @Column(name = "oauth_name", unique = true, length = 10)
    private String oauthName;

    @Comment("OAuth Auth Data Id")
    @Column(name = "oauth_id")
    private String oauthId;

    @Comment("1 - Active, 0 - Deactive, 2 - Delete")
    @ColumnDefault("1")
    @Column(name = "oauth_status", length = 1)
    private Integer oauthStatus;
}
