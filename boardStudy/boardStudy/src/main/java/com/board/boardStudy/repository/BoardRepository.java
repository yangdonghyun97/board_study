package com.board.boardStudy.repository;

import com.board.boardStudy.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long> { // <엔티티 클래스, pk타입>
}
