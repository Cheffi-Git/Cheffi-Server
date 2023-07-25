package com.cheffi.review.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Enum ratingType;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public Rating(Enum ratingType, Review review) {
        this.ratingType = ratingType;
        this.review = review;
    }
}
