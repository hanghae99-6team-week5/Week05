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

  // TODO : 왜 이렇게 해줘야하는지 작성 용문님 숙제 ( 의존성 주입의 3가지 방법 )
  public CommentService(CommentRepository commentRepository, TokenProvider tokenProvider, PostService postService) {
    this.commentRepository = commentRepository;
    this.tokenProvider = tokenProvider;
    this.postService = postService;
  }


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

    //1 : 모든 파라미터가 필요없는 생성자로 만든 객체 @NoArgsConstructor
    Comment commentNoArgs = new Comment(); // 메모리 자원을 소모
    //1-1:[ @NoArgsConstructor ] 모든 파라미터가 필요없는 생성자로 만든 객체에 필드 값을 할당
    commentNoArgs.saveComment(requestDto , member , post); // 리소스가 소모

    // 변경가능성이 높다 -> 가변성이 높다.
    // 객체지향 5대 원칙중 : 수정에는 닫혀있고 확장에는 열려있다.
    // commentNoArgs.setPost();
    // commentNoArgs.setContent();
    // commentNoArgs.setMember();

    // nullable true -> 저장할 때가 아닌 업데이트될 때 멤버가 할당되야하는 프로세스
    //commentNoArgs.setMember();


    //2 : [ @AllAegsConstructor ] 모든 파라미터가 필요한 생성자로 만든 객체
    // 메모리 자원 소모
    Comment commentAllArgs = new Comment(member, post, requestDto.getContent());

//    Comment comment = Comment.builder()
//        .member(member)
//        .post(post)
//        .content(requestDto.getContent())
//        .build();

    Comment savedComment = commentRepository.save(commentNoArgs);

    // [ @AllAegsConstructor ]
    //1. CommentResponseDto를 생성함과 동시에 필드 값을 파라미터로 할당받아 값을 할당합니다.
    //CommentResponseDto commentResponseDtoAllArgs = new CommentResponseDto(savedComment.getId(), savedComment.getMember().getNickname(),savedComment.getContent(),savedComment.getCreatedAt(),savedComment.getModifiedAt());

    // [ @NoArgsConstructor ]
    //1. CommentResponseDto 객체를 만들고
    CommentResponseDto commentResponseDtoNoArgs = new CommentResponseDto();

    //2. 객체에 저희가 원하는 값만을 셋팅하고
    commentResponseDtoNoArgs.setCommentResponseDto(savedComment.getId() , savedComment.getMember().getNickname() , savedComment.getContent() , savedComment.getCreatedAt() , savedComment.getModifiedAt());

    //3. 리턴합니다.
    return ResponseDto.success(commentResponseDtoNoArgs);
//        CommentResponseDto.builder()
//            .id(comment.getId())
//            .author(comment.getMember().getNickname())
//            .content(comment.getContent())
//            .createdAt(comment.getCreatedAt())
//            .modifiedAt(comment.getModifiedAt())
//            .build()

  }



  @Transactional(readOnly = true)
  public ResponseDto<?> getAllCommentsByPost(Long postId) {
    Post post = postService.isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    CommentRequestDto commentRequestDto = new CommentRequestDto(comment.getId(), comment.getMember().getNickname(),comment.getContent(),comment.getCreatedAt(),comment.getModifiedAt());
    for (Comment comment : commentList) {
      commentResponseDtoList.add(
//          CommentResponseDto.builder()
//              .id(comment.getId())
//              .author(comment.getMember().getNickname())
//              .content(comment.getContent())
//              .createdAt(comment.getCreatedAt())
//              .modifiedAt(comment.getModifiedAt())
//              .build()
//      );
    }
    return ResponseDto.success(commentResponseDtoList);
  }

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
//    return ResponseDto.success();
  }

  @Transactional
  public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
    ResponseDto<Object> MEMBER_NOT_FOUND = validateUser(null == request.getHeader("Refresh-Token"), "MEMBER_NOT_FOUND", "MEMBER_NOT_FOUND", "로그인이 필요합니다.", "로그인이 필요합니다.", null == request.getHeader("Authorization"));
    if (MEMBER_NOT_FOUND != null) return MEMBER_NOT_FOUND;

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(id);
    ResponseDto<Object> NOT_FOUND = validateUser(null == comment, "NOT_FOUND", "BAD_REQUEST", "존재하지 않는 댓글 id 입니다.", "작성자만 수정할 수 있습니다.", comment.validateMember(member));
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
