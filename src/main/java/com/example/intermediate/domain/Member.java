package com.example.intermediate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
  private Long id;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Member member = (Member) o;
    return id != null && Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }

  /////////특정게시글 좋아요///Member가 삭제될 때 연관된 도메인인 Heart도 같이 삭제되도록 설정
//  @OneToMany(
//          mappedBy = "member",
//          cascade = CascadeType.ALL,  //같이삭제되도록
//          orphanRemoval = true,
//          fetch = FetchType.LAZY)
//  private List<Heart> hearts;
  //////////////////////////////////////////////
}
