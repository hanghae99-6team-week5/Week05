package com.example.team6.service;

import com.example.team6.controller.response.ResponseDto;
import com.example.team6.controller.response.CommentResponseDto;
import com.example.team6.domain.Comment;
import com.example.team6.domain.Member;
import com.example.team6.domain.Post;
import com.example.team6.controller.request.CommentRequestDto;
import com.example.team6.jwt.TokenProvider;
import com.example.team6.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final CommentResponseDto commentResponseDto;
  private final TokenProvider tokenProvider;
  private final PostService postService;

  // TODO : 왜 이렇게 해줘야하는지 작성 용문님 숙제 ( 의존성 주입의 3가지 방법 )
  public CommentService(CommentRepository commentRepository, CommentResponseDto commentResponseDto, TokenProvider tokenProvider, PostService postService) {
    this.commentRepository = commentRepository;
    this.commentResponseDto = commentResponseDto;
    this.tokenProvider = tokenProvider;
    this.postService = postService;
  }

  //게시글 생성
  @Transactional
  public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {

    ResponseDto<Object> MEMBER_NOT_FOUND =
            validateUser(
                    null == request.getHeader("Refresh-Token"),
                    "MEMBER_NOT_FOUND",
                    "MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.",
                    "로그인이 필요합니다.",
                    null == request.getHeader("Authorization"));

    if (MEMBER_NOT_FOUND != null) return MEMBER_NOT_FOUND;

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = postService.isPresentPost(requestDto.getPostId());
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = new Comment(member, post, requestDto.getContent());
//    Comment comment = Comment.builder()
//            .member(member)
//            .post(post)
//            .content(requestDto.getContent())
//            .build();
    Comment savedcomment = commentRepository.save(comment);

    CommentResponseDto setCommet = new CommentResponseDto(
            commentResponseDto.getAuthor(),
            commentResponseDto.getContent(),
            commentResponseDto.getCreatedAt(),
            commentResponseDto.getModifiedAt());


    return ResponseDto.success(setCommet);
//            CommentResponseDto.builder()
//                    .id(comment.getId())
//                    .author(comment.getMember().getNickname())
//                    .content(comment.getContent())
//                    .createdAt(comment.getCreatedAt())
//                    .modifiedAt(comment.getModifiedAt())
//                    .build()
//    );
  }


  // 댓글 조회
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllCommentsByPost(Long postId) {
    Post post = postService.isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      CommentResponseDto commentResponseDto1 = new CommentResponseDto(
              comment.getId(),
              comment.getMember().getNickname(),
              comment.getContent(),
              comment.getCreatedAt(),
              comment.getModifiedAt());
      commentResponseDtoList.add(commentResponseDto1);
//          CommentResponseDto.builder()
//              .id(comment.getId())
//              .author(comment.getMember().getNickname())
//              .content(comment.getContent())
//              .createdAt(comment.getCreatedAt())
//              .modifiedAt(comment.getModifiedAt())
//              .build()
//      );
    };
    return ResponseDto.success(commentResponseDtoList);
  }

  //댓글 업데이트
  @Transactional
  public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
    ResponseDto<Object> MEMBER_NOT_FOUND = validateUser(null == request.getHeader("Refresh-Token"), "MEMBER_NOT_FOUND", "MEMBER_NOT_FOUND", "로그인이 필요합니다.", "로그인이 필요합니다.", null == request.getHeader("Authorization"));
    if (MEMBER_NOT_FOUND != null) return MEMBER_NOT_FOUND;

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = postService.isPresentPost(requestDto.getPostId());
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = isPresentComment(id);
    ResponseDto<Object> NOT_FOUND = validateUser(null == comment, "NOT_FOUND", "BAD_REQUEST", "존재하지 않는 댓글 id 입니다.", "작성자만 수정할 수 있습니다.", comment.validateMember(member));
    if (NOT_FOUND != null) return NOT_FOUND;

    comment.updateComment(requestDto);
    return ResponseDto.success(null);
  }

  //댓글 삭제
  @Transactional
  public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
    ResponseDto<Object> MEMBER_NOT_FOUND = validateUser(null == request.getHeader(
            "Refresh-Token"),
            "MEMBER_NOT_FOUND",
            "MEMBER_NOT_FOUND",
            "로그인이 필요합니다.",
            "로그인이 필요합니다.",
            null == request.getHeader("Authorization"));

    if (MEMBER_NOT_FOUND != null) return MEMBER_NOT_FOUND;

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail(
              "INVALID_TOKEN",
              "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(id);
    ResponseDto<Object> NOT_FOUND = validateUser(
            null == comment,
            "NOT_FOUND",
            "BAD_REQUEST",
            "존재하지 않는 댓글 id 입니다.",
            "작성자만 수정할 수 있습니다.", comment.validateMember(member));
    if (NOT_FOUND != null) return NOT_FOUND;

    commentRepository.delete(comment);
    return ResponseDto.success("success");
  }

  @Transactional(readOnly = true)
  public Comment isPresentComment(Long id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    return optionalComment.orElse(null);
  }


  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }


  //로그인 유효성 검사
  private static ResponseDto<Object> validateUser(boolean request, String MEMBER_NOT_FOUND, String MEMBER_NOT_FOUND1, String message, String message1, boolean request1) {
    if (request) {
      return ResponseDto.fail(MEMBER_NOT_FOUND,
              message);
    }

    if (request1) {
      return ResponseDto.fail(MEMBER_NOT_FOUND1,
              message1);
    }
    return null;
  }
}
