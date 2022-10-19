package com.example.team6.controller.response;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class CommentResponseDto {
  private Long id;
  private String author;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public Long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
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
  public CommentResponseDto( String author, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.author = author;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public void setCommentResponseDto(Long id, String memberNickname, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    // 댓글 id 할당
    this.id = id;
    // 댓글 작성자 할당(member.getNickname)
    this.author = memberNickname;
    // 댓글 내용할당
    this.content = content;
    // 댓글 생성시간
    this.createdAt = createdAt;
    // 댓글 수정 시간
    this.modifiedAt = modifiedAt;
  }

  public CommentResponseDto(Long id, String author, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.author = author;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}
