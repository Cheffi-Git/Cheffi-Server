package com.cheffi.avatar.domain;

import com.cheffi.common.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class CheffiCoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cfcValue;

    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private CheffiCoin(Avatar avatar, int cfcValue, String description) {
        this.cfcValue = cfcValue;
        this.avatar = avatar;
        this.description = description;
    }

    public static CheffiCoin of(Avatar avatar, int value, String description) {
        avatar.applyCheffiCoinBy(value);
        return new CheffiCoin(avatar, value, description);
    }

}
