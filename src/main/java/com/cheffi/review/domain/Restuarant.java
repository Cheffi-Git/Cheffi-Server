package com.cheffi.review.domain;

import com.cheffi.common.constant.Address;
import com.cheffi.common.constant.DetailedAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Restuarant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Embedded
    private DetailedAddress detailedAddress;

    @Builder
    public Restuarant(String name, DetailedAddress detailedAddress) {
        this.name = name;
        this.detailedAddress = detailedAddress;
    }
}
