package kr.fast.community.controller;

import kr.fast.community.entity.Board;
import kr.fast.community.entity.Post;

public record PostRequest(
		String title, 
		String content, 
		Integer boardId) {

	public boolean validTitle() {
		
		return title != null && title.trim().length() != 0;
	}

	public boolean validContent() {
		
		return content != null && content.trim().length() != 0;
	}

	public boolean validBoardId() {
		
		return boardId != 0;
	}

	public Post toPost(Board board, String memberId) {
		return new Post(title, content, board, memberId);
	}
	
}


