package kr.fast.community.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.fast.community.entity.Board;
import kr.fast.community.repository.BoardRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	public List<Board> getBoards() {
		return boardRepository.findAll();
	}

}
