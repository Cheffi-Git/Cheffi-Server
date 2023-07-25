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
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    @Builder
    public Food(String name, int price, Review review) {
        this.name = name;
        this.price = price;
        this.review = review;
    }
}
