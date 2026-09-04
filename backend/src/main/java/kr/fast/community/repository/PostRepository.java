package kr.fast.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.fast.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Page<Post> findAllByIsDeletedContaining(String isDeleted, Pageable pageable);

	Page<Post> findAllByIsDeletedAndTitleContaining(String isDeleted, String keyword, Pageable pageable);

	Page<Post> findAllByIsDeletedAndMemberIdContaining(String isDeleted, String keyword, Pageable pageable);

	Post findByIdAndIsDeleted(int postId, String isDeleted);

	// 여기서 사용하는 쿼리에는 테이블명 대신 엔티티명으로, 컬럼명 대신 엔티티 필드명으로 작성
	// 변수값은 :변수명으로 처리하고 @Param("변수명")을 통해 넘겨줌
	@Modifying
	@Query("update Post p set"
	         + " p.upCount = (select count(*) from Like pl where pl.postId = :postId and pl.state = 1),"
	         + " p.downCount = (select count(*) from Like pl where pl.postId = :postId and pl.state = -1)"
	         + " where p.id = :postId")
	   void updateLikeAndDislikeCount(@Param("postId") int postId);

}
