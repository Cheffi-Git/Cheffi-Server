package com.cheffi.user.domain;

import java.time.LocalDateTime;

import com.cheffi.avatar.domain.Avatar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private boolean locked;

    @NotNull
    private boolean expired;
    @NotNull
    private boolean withdrawn;
    private LocalDateTime withdrawnDate;
    @NotNull
    private boolean activated;
    @NotNull
    private LocalDateTime lastPwChangedDate;
    @NotNull
    private String name;

    @NotNull
    private String provider;
    @NotNull
    private boolean adAgreed;
    @NotNull
    private boolean analysisAgreed;
    private String pwHash;
    private String pwSalt;
    private String fcmToken;


    @OneToOne(mappedBy = "user")
    private Avatar avatar;

    @Builder
    private User(String email, boolean locked, boolean expired, boolean withdrawn, boolean activated, String name,
        String provider) {
        this.email = email;
        this.locked = locked;
        this.expired = expired;
        this.withdrawn = withdrawn;
        this.activated = activated;
        this.name = name;
        this.provider = provider;
    }

    //TODO User 객체 생성 메서드
}
