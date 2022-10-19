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
      CommentResponseDto commentResponseDto = new CommentResponseDto(comment.getId(), comment.getMember().getNickname(), comment.getContent(),
              comment.getCreatedAt(), comment.getModifiedAt());
      commentResponseDtoList.add(commentResponseDto);
      //builder 수정
//          CommentResponseDto.builder()
//              .id(comment.getId())
//              .author(comment.getMember().getNickname())
//              .content(comment.getContent())
//              .createdAt(comment.getCreatedAt())
//              .modifiedAt(comment.getModifiedAt())
//              .build()

    }
    //생서자를 return 위에 추가 //success( 여기에 값 넣어주기 )
    PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getContent(),commentResponseDtoList,
            post.getMember().getNickname(), post.getCreatedAt(), post.getModifiedAt());
    return ResponseDto.success(postResponseDto);
    //builder 수정
//        PostResponseDto.builder()
//            .id(post.getId())
//            .title(post.getTitle())
//            .content(post.getContent())
//            .commentResponseDtoList(commentResponseDtoList)
//            .author(post.getMember().getNickname())
//            .createdAt(post.getCreatedAt())
//            .modifiedAt(post.getModifiedAt())
//            .build()
//    );
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
