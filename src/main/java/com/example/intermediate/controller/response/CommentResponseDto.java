package com.example.intermediate.controller.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Builder
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class CommentResponseDto {
  private Long id;
  private String author;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public CommentResponseDto(Long id, String author, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
  this.id = id;
  this.author = author;
  this.content = content;
  this.createdAt = createdAt;
  this.modifiedAt = modifiedAt;
  }
}
