package com.example.team6.domain;

import com.example.team6.controller.request.CommentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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

  public Comment() {

  }

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

  public Comment(Member member, Post post, String content) {
    this.id = id;
    this.member = member;
    this.post = post;
    this.content = content;
  }

  public void update(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
