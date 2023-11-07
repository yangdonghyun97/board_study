package com.board.boardStudy.DTO;

import com.board.boardStudy.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
// 데이터를 주고 받을때만 사용하는 객체

    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private  int boardHits;

    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    //스프링제공 파일객체 save.html -> Controller 파일 담는 용도
    private MultipartFile boardFile;
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; //파일 첨부 여부 확인용 ( 첨부 1, 미첨부 0)


    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        // 다른 필드들도 필요하다면 여기에 추가
        if(boardEntity.getFileAttached() == 0){
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        }else{
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            // 파일 이름을 가져가야함.
            // orginalFileName, storedFileName : board_file_table(BoardFileEntity)
            // join
            // select * from board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.id=?
            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOrginalFileName());
            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
        }

        return boardDTO;
    }




}
