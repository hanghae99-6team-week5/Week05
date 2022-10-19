package com.example.team6.repository;

import com.example.team6.domain.Comment;
import com.example.team6.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
  List<Comment> findAllByUsernameOrderByCreatedAtDesc(String username);

}
