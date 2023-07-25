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
public class ReviewPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Integer order;

    @Builder
    public ReviewPhoto(String url, Integer order) {
        this.url = url;
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
