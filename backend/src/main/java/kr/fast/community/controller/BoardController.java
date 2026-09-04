package kr.fast.community.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.fast.community.entity.Board;
import kr.fast.community.service.BoardService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
	
	private final BoardService boardService;
	
	@PostMapping("")
	public ResponseEntity<Object> post(){
		
		List<Board> list = boardService.getBoards();
		return ResponseEntity.ok(list);
		
		
	}

}
