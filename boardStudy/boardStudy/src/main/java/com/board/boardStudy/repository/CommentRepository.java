package com.board.boardStudy.repository;

import com.board.boardStudy.entity.BoardEntity;
import com.board.boardStudy.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    // select * from commet_table where board_id=? order by id desc;

    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
