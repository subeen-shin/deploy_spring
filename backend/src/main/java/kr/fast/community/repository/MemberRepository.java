package kr.fast.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.fast.community.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

	boolean existsByEmail(String email);

	
}
