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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    private int ratingCnt;

    @ManyToOne
    @Column(name = "restuarnt_id")
    private Restuarant restuarant;

    @Builder
    public Review(String title, String text, int ratingCnt) {
        this.title = title;
        this.text = text;
        this.ratingCnt = ratingCnt;
    }
}
