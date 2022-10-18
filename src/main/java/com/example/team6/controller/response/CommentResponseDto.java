package com.example.team6.controller.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

  @Autowired
  public CommentResponseDto(Long id, String author, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.author = author;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}
