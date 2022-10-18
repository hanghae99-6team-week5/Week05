package com.example.team6.controller.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String author;
  private List<CommentResponseDto> commentResponseDtoList;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public PostResponseDto(Long id, String title, String content, String author,
         List<CommentResponseDto> commentResponseDtoList, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this. commentResponseDtoList = commentResponseDtoList;
    this. createdAt = createdAt;
    this. modifiedAt = modifiedAt;
  }
}

