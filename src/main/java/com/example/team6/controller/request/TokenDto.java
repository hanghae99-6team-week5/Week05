package com.example.team6.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiresIn;

  //////////////////추가
//  public TokenDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn) {
//    this.grantType = grantType;
//    this.accessToken = accessToken;
//    this.refreshToken = refreshToken;
//    this.accessTokenExpiresIn = accessTokenExpiresIn;
//  }
  //////////////////
}
