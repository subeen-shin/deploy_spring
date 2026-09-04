package kr.fast.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.fast.community.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
