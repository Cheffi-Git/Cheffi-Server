package com.cheffi.avatar.domain;

import com.cheffi.common.constant.Address;
import com.cheffi.user.domain.User;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;
    private String pictureUrl;
    private String introduction;
    @Embedded
    private Address address;
    private int cheffiCoinCnt;
    private int pointCnt;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Avatar(String nickname, String pictureUrl, User user) {
        this.nickname = nickname;
        this.pictureUrl = pictureUrl;
        this.user = user;
        this.cheffiCoinCnt = 0;
        this.pointCnt = 0;
    }

    public void changeAddress(Address address) {
        if(!address.isSimpleAddress())
            throw new IllegalArgumentException("Address 클래스의 서브 클래스는 사용할 수 없습니다.");
        this.address = address;
    }

    public void changeIntroduction(String introduction) {
        //TODO 불변식
        this.introduction = introduction;
    }


}
