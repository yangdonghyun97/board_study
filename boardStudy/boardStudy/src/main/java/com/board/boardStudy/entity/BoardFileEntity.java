package com.board.boardStudy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name ="board_file_table")
public class BoardFileEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orginalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") // board_id 라는 컬럼을 참조외래키로 하겠다
    private BoardEntity boardEntity; // 부모 테이블로 쓰겠다


    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOrginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;

    }
}
