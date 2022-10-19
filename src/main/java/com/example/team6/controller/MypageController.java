package com.example.team6.controller;


import com.example.team6.domain.Comment;
import com.example.team6.domain.Post;
import com.example.team6.domain.UserDetailsImpl;
import com.example.team6.repository.CommentRepository;
import com.example.team6.service.MypageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@AllArgsConstructor
public class MypageController {

    private final MypageService MypageService;
    private final CommentRepository commentRepository;

    //@AllArgsConstructor
    public MypageController(MypageService mypageService, CommentRepository commentRepository) {
        this.MypageService = mypageService;
        this.commentRepository = commentRepository;
    }

//    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping({"/api/mypage/comment"})
    private List<Comment> getMyComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
       // @AuthenticationPrincipal 애노테이션을 사용하면
        // UserDetailsService에서 Return한 객체 를 파라메터로 직접 받아 사용할 수 있다.
        if (userDetails != null) {
            String username = userDetails.getMember().getNickname();
            return this.MypageService.getMypageComment(username);
        }
        else{
            return null;
        }
    }

//    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping({"/api/mypage/post"})
    public List<Post> getMyPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null){
            Long userId = userDetails.getMember().getId();
            return this.MypageService.getMypagePost(userId);
        }
        else{
            return null;
        }
    }
}
