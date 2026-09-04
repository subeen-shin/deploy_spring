package kr.fast.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "post_like")
@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString

public class Like {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	int state;
	@Column(name="member_id")
	String memberId;
	
	@Column(name="post_id")
	int postId;
	
	public Like(int postId, String memberId, Integer state) {
		this.postId = postId;
		this.memberId = memberId;
		this.state = state;
	}

	public void updateState(int state) {
		this.state = state;
		
	}


}
