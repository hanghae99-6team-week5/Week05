package com.example.intermediate.domain;

import com.example.intermediate.controller.request.CommentRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Builder
@Getter
//@NoArgsConstructor //기본 생성자를 만들어줘야 한다.
//@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

  public Comment() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "post_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @Column(nullable = false)
  private String content;


  public void update(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  //댓글을 수정한다거나 삭제할 때 댓글의 작성자와 로그인한 멤버가 일치하는지 비교 boolean return
  public boolean validateMember(Member member) {
    //일치하는지
    return !this.member.equals(member);
  }

  public Comment(Member member, Post post, String content){
    this.member = member;
    this.post = post;
    this.content = content;
  }
}
