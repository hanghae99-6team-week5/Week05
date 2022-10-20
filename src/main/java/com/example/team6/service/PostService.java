package com.example.team6.service;

import com.example.team6.controller.response.CommentResponseDto;
import com.example.team6.controller.response.PostResponseDto;
import com.example.team6.domain.Comment;
import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import com.example.team6.controller.request.PostRequestDto;
import com.example.team6.controller.response.ResponseDto;
import com.example.team6.jwt.TokenProvider;
import com.example.team6.repository.CommentRepository;
import com.example.team6.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@RequiredArgsConstructor
public class PostService {
  // 의존성 주입을 해서 사용하는 경우, 최초 선언 후 선언된 객체의 기능(메서드)만을 사용하고 새롭게 객체를 생성하거나 값을 할당해줄 필요가 없는 경우
  //이 부분은 계속 다른 메서드에 쓰이는 용도로 사용되기에 의존성 주입이 필요하다
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final TokenProvider tokenProvider;

  ////@RequiredArgsConstructor 대체할 생성자 추가
  public PostService(PostRepository postRepository, CommentRepository commentRepository, TokenProvider tokenProvider) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.tokenProvider = tokenProvider;
  }
  //////////////////////////

  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    ////builder 수정
    Post post = new Post(requestDto.getTitle(), requestDto.getContent(), member);
//    Post post = Post.builder()
//        .title(requestDto.getTitle())
//        .content(requestDto.getContent())
//        .member(member)
//        .build();
    postRepository.save(post);
    PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getMember().getNickname(), post.getCreatedAt(), post.getModifiedAt());
    return ResponseDto.success(postResponseDto);
    //builder 수정
//        PostResponseDto.builder()
//            .id(post.getId())
//            .title(post.getTitle())
//            .content(post.getContent())
//            .author(post.getMember().getNickname())
//            .createdAt(post.getCreatedAt())
//            .modifiedAt(post.getModifiedAt())
//            .build()
//    );

  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      //builder() 미사용시 생성자 추가 후 commentResponseDto 값 넣어주기
      CommentResponseDto commentResponseDto = new
              CommentResponseDto(
              comment.getMember().getNickname(),
              comment.getContent(),
              comment.getCreatedAt(),
              comment.getModifiedAt());

      commentResponseDtoList.add(commentResponseDto);
    }

    //생서자를 return 위에 추가 //success( 여기에 값 넣어주기 )
    PostResponseDto postResponseDto = new PostResponseDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            commentResponseDtoList,
            post.getMember().getNickname(),
            post.getCreatedAt(),
            post.getModifiedAt());


    return ResponseDto.success(postResponseDto);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
  }

  @Transactional
  public ResponseDto<Post> updatePosts(Long id, PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    post.update(requestDto);
    return ResponseDto.success(post);
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
              "로그인이 필요합니다.");
    }
//사용할 때마다 새로운 이름으로 사용하기 때문에 의존성 주입을 해주는게 아니다
    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }
}
