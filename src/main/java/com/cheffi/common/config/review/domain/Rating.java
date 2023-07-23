package com.cheffi.common.config.review.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
}
