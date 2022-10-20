package com.example.team6.controller.response;

import java.time.LocalDateTime;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class CommentResponseDto {

  private String nickname;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;


  public String getNickname() {
    return nickname;
  }

  public String getContent() {
    return content;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  //@AllArgsConsturctor
  public CommentResponseDto( String nickname, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.nickname = nickname;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public void setCommentResponseDto(String memberNickname, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {

    // 댓글 작성자 할당(member.getNickname)
    this.nickname = memberNickname;
    // 댓글 내용할당
    this.content = content;
    // 댓글 생성시간
    this.createdAt = createdAt;
    // 댓글 수정 시간
    this.modifiedAt = modifiedAt;
  }

}
