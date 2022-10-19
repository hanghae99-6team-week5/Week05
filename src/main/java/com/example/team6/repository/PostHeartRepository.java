package com.example.team6.repository;

import com.example.team6.domain.Member;
import com.example.team6.domain.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    Long countAllByPostId(Long postId);
    PostHeart findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostHeart> findAllByMember(Member member);
}
