package com.example.team6.controller.request;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class CommentRequestDto {
  private Long postId;
  private String content;

  public Long getPostId() {
    return postId;
  }

  public String getContent() {
    return content;
  }


  public CommentRequestDto(Long postId, String content) {
    this.postId = postId;
    this.content = content;
  }


}
