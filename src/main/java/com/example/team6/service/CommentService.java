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
  private final TokenProvider tokenProvider;
  private final PostService postService;

  //생성자를 통해서 의존성 주입
  // TODO : 왜 이렇게 해줘야하는지 작성 용문님 숙제 ( 의존성 주입의 3가지 방법 )
  public CommentService(CommentRepository commentRepository, TokenProvider tokenProvider, PostService postService) {
    this.commentRepository = commentRepository;
    this.tokenProvider = tokenProvider;
    this.postService = postService;
  }

  //comment 생성
  @Transactional
  public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {

    //로그인 유효성 검사
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

    // 내용을 담아줄 savecomment 생성
    Comment savecomment = new Comment(member, post, requestDto.getContent());
//    Comment comment = Comment.builder()
//            .member(member)
//            .post(post)
//            .content(requestDto.getContent())
//            .build();
    Comment savedcomment = commentRepository.save(savecomment); //DB에 저장

    //고객에게 보여야하는 정보를 new연산자를 통해서 생성
    CommentResponseDto setcomment = new CommentResponseDto(
            savedcomment.getMember().getNickname(),
            savedcomment.getContent(),
            savedcomment.getCreatedAt(),
            savedcomment.getModifiedAt());

    //client에게 setcomment 값을 return
    return ResponseDto.success(setcomment);
//            CommentResponseDto.builder()
//                    .id(comment.getId())
//                    .author(comment.getMember().getNickname())
//                    .content(comment.getContent())
//                    .createdAt(comment.getCreatedAt())
//                    .modifiedAt(comment.getModifiedAt())
//                    .build()
//    );
  }


  // comment 조회
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllCommentsByPost(Long postId) {
    Post post = postService.isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    //모든 comment를 commentlist에 담기
    List<Comment> commentList = commentRepository.findAllByPost(post);

    //고객에게 보여질 내용을 담을 list 생성
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    //for문을 통해서 필요한 내용만 가져옴 가져온 내용을 list에 담아내기
    for (Comment comment : commentList) {
      CommentResponseDto commentResponseDto1 = new CommentResponseDto(
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
    //list에 담은 내용을 client에게 return
    return ResponseDto.success(commentResponseDtoList);
  }

  //comment 업데이트
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

  //comment 삭제
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


  //refresh token 확인
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
