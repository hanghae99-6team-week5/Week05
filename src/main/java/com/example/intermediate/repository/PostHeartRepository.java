package com.example.intermediate.repository;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    Long countAllByPostId(Long postId);
    PostHeart findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostHeart> findAllByMember(Member member);
}
