package com.example.team6.domain;

import javax.persistence.*;

//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class PostHeart {

    //@NoArgsConstructor 추가 부분
    public PostHeart() {
    }
    //////////////////

    //////@AllArgsConstructor 추가부분
    public PostHeart(Long id, Member member, Post post) {
        this.id = id;
        this.member = member;
        this.post = post;
    }
    ////////////////////////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    ////////@Getter 추가부분
    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Post getPost() {
        return post;
    }
    /////////////////

    //PostHeartService builder 사용 안하기 위한 생성자 추가
    public PostHeart(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
    ////////////////////
}
