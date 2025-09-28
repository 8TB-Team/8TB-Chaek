package com.example.chackchack.domain.user.entity;

import com.example.chackchack.common.entity.SoftDeleteEntity;
import com.example.chackchack.domain.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString(exclude = "password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "users",
        indexes = {
            @Index(name = "idx_users_email", columnList = "email")
        }
)
public class User extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 254, nullable = false, unique = true, updatable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "nickname", length = 30, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, columnDefinition = "varchar(20) default 'ROLE_USER'")
    private UserRole userRole;

    @Builder
    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = normalizeEmail(email);
        this.password = password;
        this.nickname = nickname;
        this.userRole = Objects.requireNonNullElse(userRole, UserRole.ROLE_USER);
    }

    // 이메일 소문자 정규화
    private static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    @PrePersist @PreUpdate
    private void normalize() {
        this.email = normalizeEmail(this.email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
