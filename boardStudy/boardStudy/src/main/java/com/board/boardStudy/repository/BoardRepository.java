package com.board.boardStudy.repository;

import com.board.boardStudy.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Long> { // <엔티티 클래스, pk타입>
@Modifying // 삭제 , 업데이트 할때 무조건 사용해야하는 어노테이션
@Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
// @Param("id")를 꼭 써야하는건 아니지만 jpa query가 id 값을 잘 매핑할 정확도를 높이기 위해서 써야한다.
}
