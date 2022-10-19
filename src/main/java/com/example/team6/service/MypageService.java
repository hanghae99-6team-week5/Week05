package com.example.team6.service;


import com.example.team6.domain.Comment;
import com.example.team6.domain.Post;
import com.example.team6.repository.CommentRepository;
import com.example.team6.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@AllArgsConstructor
public class MypageService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //@AllArgsConstructor
    public MypageService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<Comment> getMypageComment(String username) {
        return this.commentRepository.findAllByUsernameOrderByCreatedAtDesc(username);
    }

    public List<Post> getMypagePost(Long userId) {
        return this.postRepository.findAllByuserIdOrderByCreatedAtDesc(userId);
    }

}