package com.example.team6.repository;

import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findAllByOrderByModifiedAtDesc();
  List<Post> findAllByMember(Member member);

}
