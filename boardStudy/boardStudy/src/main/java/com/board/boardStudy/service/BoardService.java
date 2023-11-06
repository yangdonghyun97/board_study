package com.board.boardStudy.service;

import com.board.boardStudy.DTO.BoardDTO;
import com.board.boardStudy.entity.BoardEntity;
import com.board.boardStudy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// DTO -> Entity
// Entity -> DTO

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        // 서비스 단에서 DTO를 Entity객체로 바꿔준 후 db에 저장해준다.
        BoardEntity Entity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(Entity);
    }

    public List<BoardDTO> findALL() {
        List<BoardEntity> boardlist = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>(); // 빈 리스트 객체

        for (BoardEntity boardEntity : boardlist) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;

    }

    public BoardDTO findById(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);

        return boardDTO;
    }
}
