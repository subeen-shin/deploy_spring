package kr.fast.community.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.fast.community.dto.CommentRequest;
import kr.fast.community.dto.LikeRequest;
import kr.fast.community.dto.MessageResponse;
import kr.fast.community.dto.PageResponse;
import kr.fast.community.entity.Comment;
import kr.fast.community.entity.File;
import kr.fast.community.entity.Post;
import kr.fast.community.security.CustomUserDetails;
import kr.fast.community.service.PostService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
   
   private final PostService postService;
   
   @Operation(summary = "게시글 목록 조회", description = "검색어, 타입, 페이지를 이용하여 게시글 목록을 조회")
   @GetMapping("")
   public ResponseEntity<Object> get(
		 @Parameter(
				 description = "검색 타입", 
				 schema = @Schema(type = "String", allowableValues = {"all, title, writer"}))
         @RequestParam(required = false, defaultValue = "all", name="type")String type,
         @Parameter(description = "검색어")
         @RequestParam(required = false, defaultValue = "",name="keyword")String keyword,
         @Parameter(description = "정렬")
         @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC)
            Pageable pageable){
    
      PageResponse<Post> pageResponse = postService.getPosts(type, keyword, pageable);
      return ResponseEntity.ok(pageResponse);
   }
   
   @GetMapping("/{게시글번호}")
   public ResponseEntity<Object> idGet(@PathVariable("게시글번호")int 게시글번호){
      try {
         //서비스야 게시글 가져와. 번호 여기있어.
         Post post = postService.getPost(게시글번호);
         List<File> files = postService.getFiles(게시글번호);
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("post", post);
         map.put("files", files);
         return ResponseEntity.ok(map);
      }catch (Exception e) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
   }
   @PostMapping("")
   public ResponseEntity<Object> post(
         @RequestPart("post")  PostRequest request, //화면에서 보낸 게시글 정보
         @RequestPart(value="files", required = false) List<MultipartFile> files, 
         @AuthenticationPrincipal CustomUserDetails userDetails //로그인한 회원 정보
      ){
      MessageResponse ms;
      try {
         ms = postService.insertPost(request, userDetails, files);         
      }catch (Exception e) {
         ms = new MessageResponse(false, e.getMessage());
      }
      return ResponseEntity.ok(ms);
   }

   @PostMapping("/{게시글번호}/comments")
   public ResponseEntity<Object> commentsPost(
		@PathVariable("게시글번호") int 게시글번호,
		@RequestBody CommentRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails
			){
	   MessageResponse ms 
	   	= postService.insertComment(게시글번호, request, userDetails);
	   return ResponseEntity.ok(ms);
   }
   
   @GetMapping("/{게시글번호}/comments")
   public ResponseEntity<Object> commentsGet(
		   @PathVariable("게시글번호")int postId,
		   @PageableDefault(size=3, sort="originId", direction = Sort.Direction.DESC)
           Pageable pageable){
	
	   
	   //서비스야 댓글 목록 가져와. 게시글 번호와 페이지 정보 줄게
	   //게시글 목록에 페이지 정보도 같이줘.
	   
	   PageResponse<Comment> pageResponse = postService.getComments(postId, pageable);
	   return ResponseEntity.ok(pageResponse);
   }
   
   @PostMapping("/{게시글번호}/likes")
   public ResponseEntity<Object> likeGet(
		   @PathVariable("게시글번호")int postId,
	   	   @AuthenticationPrincipal CustomUserDetails details,
	   	   @RequestBody LikeRequest request){
	   MessageResponse ms;
	   Map<String, Object> map = new HashMap<String, Object>();
	   try {
		   int state = postService.like(postId, details, request);
		   String msg;
		   switch (state) {
		   case 1: msg = "좋아요를 눌렀습니다."; break;
		   case -1: msg = "싫어요를 눌렀습니다."; break;
		   default:
			   msg = request.state() == 1 ? "좋아요를 취소했습니다." : "싫어요를 취소했습니다.";
		   }
		   
		   map.put("state", state);
		   ms = new MessageResponse(true, msg);
				   
	   }catch (Exception e) {
		   ms = new MessageResponse(false, e.getMessage());
	   }
	   map.put("ms", ms);
	   return ResponseEntity.ok(map);
   }

}
	   

