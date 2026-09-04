package kr.fast.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.fast.community.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {

	Like findByPostIdAndMemberId(int postId, String memberId);

}
