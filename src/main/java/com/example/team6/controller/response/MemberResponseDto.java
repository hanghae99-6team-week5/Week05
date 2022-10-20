package com.example.team6.controller.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class MemberResponseDto {
  private Long id;
  private String nickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  //@NoArgsConstructor
  public MemberResponseDto() {
  }

  //@AllArgsConstructor
  public MemberResponseDto(Long id, String nickname, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.id = id;
    this.nickname = nickname;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  //@Getter
  public Long getId() {
    return id;
  }
  //@Getter
  public String getNickname() {
    return nickname;
  }
  //@Getter
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  //@Getter
  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }
}
