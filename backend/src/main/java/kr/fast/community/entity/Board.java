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
@Table(name = "board")
@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString

public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//DB에서 AUTO_INVREMENT로 기본키 생성
	int id;
	
	@Column(name="name")
	String name;
}
