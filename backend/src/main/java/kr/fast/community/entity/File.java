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
@Table(name = "file")
@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(name="original_name")
	String origianlName;
	
	@Column(name="saved_name")
	String savedName;
	
	@Column(name="post_id")
	int postId;
	
	public File(String originalName, String savedName, int postId) {
		this.origianlName = originalName;
		this.savedName = savedName;
		this.postId = postId;
	}
}
