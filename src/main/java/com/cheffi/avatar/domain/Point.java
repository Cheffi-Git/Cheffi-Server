package com.cheffi.avatar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;
    // private String createdBy;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private Point(Avatar avatar, int value) {
        this.avatar = avatar;
        this.value = value;
    }

    //TODO 포인트 추가 메서드

}
