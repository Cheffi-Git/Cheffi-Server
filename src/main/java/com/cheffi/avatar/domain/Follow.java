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
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Avatar subject;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Avatar target;

    private Follow(Avatar subject, Avatar target) {
        this.subject = subject;
        this.target = target;
    }

    //TODO 팔로우 메서드 추가

}
