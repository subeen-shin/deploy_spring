package kr.fast.community.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String title;
	
	private String content;
	@Column(name="created_at", nullable = false)
	private Date createdAt = new Date();
	@Column(name="view_count")
	private int viewCount;
	@Column(name="up_count")
	private int upCount;
	@Column(name="down_count")
	private int downCount;
	@Column(name="is_deleted", nullable = false)
	private String isDeleted = "N";
	@Column(name="member_Id")
	private String memberId;
	
	@ManyToOne(fetch=FetchType.LAZY)//다대일. 여러 게시글이 하나의 게시판과 연결. 지연 로딩
	@JoinColumn(name="board_id")//자동으로 join문을 만들어 게시판 정보를 가져옴
	private Board board;
	
	
	public Post(String title, String content, String writer, Integer boardId) {
		this.title = title;
		this.content = content;
		this.memberId = writer;
		//this.boardId = boardId;
	}


	public void updateView() {
		this.viewCount++;
		
	}


	public void delete() {
		isDeleted = "Y";
		
	}


	public void update(String title, String content) {
		this.title = title;
		this.content = content;
		
	}


	public Post(String title, String content, Board board, String memberId) {
		this.title = title;
		this.content = content;
		this.board = board;
		this.memberId = memberId;
	}
}


	
