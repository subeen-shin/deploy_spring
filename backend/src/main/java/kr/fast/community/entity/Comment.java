package kr.fast.community.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString

public class Comment {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//DB에서 AUTO_INVREMENT로 기본키 생성
	int id;
	
	String content;
	
	@Column(name="created_at")
	LocalDateTime createdAt;
	
	
	@Column(name="origin_id")
	Integer originId;
	
	
	@Column(name="is_deleted")
	String isDeleted;
	
	
	@Column(name="member_id")
	String memberId;
	
	
	@Column(name="post_id")
	int postId;


	//대댓인 경우
	public Comment(String content, int postId, String memberId, Integer originId) {
		this(content, postId, memberId);
	//	this.content = content;
	//	this.id = id;
	//	this.memberId = memberId;
		this.originId = originId;
	}
	
	//댓글인경우
	public Comment(String content, int postId, String memberId) {
		this.content = content;
		this.postId = postId;
		this.memberId = memberId;
		this.createdAt = LocalDateTime.now();
		this.isDeleted = "N";
	}
	
	@PostPersist //댓글 저장후 originId가 null이면 id로 채워주는 작업해주는 JPA롤백
	public void initOriginId() {
		//댓글인 경우 (originId가 null) originId을 Id로 수정
		if(this.originId == null ) {
			this.originId = this.id;
		}
	}


}
