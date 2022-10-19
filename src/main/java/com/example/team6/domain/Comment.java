package com.example.team6.domain;

import com.example.team6.controller.request.CommentRequestDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

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

  //getter
  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public Post getPost() {
    return post;
  }

  public String getContent() {
    return content;
  }

  // setter
  public void setMember(Member member) {
    this.member = member;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void setContent(String content) {
    this.content = content;
  }

  // 모든 파라미터가 필요한 생성자
  public Comment(Member member, Post post, String content) {
//    this.id = id; FIXME : 확인 요함
    this.member = member;
    this.post = post;
    this.content = content;
  }

  // 모든 파라미터가 필요 없는 생성자
  public Comment() {
  }

  // Comment 객체에 필드 값 할당
  public void saveComment(CommentRequestDto commentRequestDto , Member member , Post post) {
    this.member = member;
    this.post = post;
    this.content = commentRequestDto.getContent();
  }

  public void updateComment(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  // 댓글을 수정한다거나 삭제할 때에 댓글의 작성자와 로그인한 멤버가 일치하는지 비교 boolean return
  public boolean validateMember(Member member) {
    // 일치하면 true를 리턴 : 일치하지 않으면 false를 리턴
    return this.member.equals(member) ? true : false ;
  }
}
