package com.example.team6.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class MypageResponseDto {
    private List<PostResponseDto> myPosts;
    //private List<CommentResponseDto> myComments;
    private List<PostResponseDto> heartedPosts;
    //private List<CommentResponseDto> heartedComments;

    //생성자 추가
    public MypageResponseDto(List<PostResponseDto> myPosts, List<PostResponseDto> heartedPosts) {
        this.myPosts = myPosts;
        this.heartedPosts = heartedPosts;
    }

}
