package com.board.boardStudy.service;

import com.board.boardStudy.DTO.BoardDTO;
import com.board.boardStudy.entity.BoardEntity;
import com.board.boardStudy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        // jap findById() 와 같이 조회를 할때는 자동적으로 Optional로 래핑되어 반환된다.
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    @Transactional // jpa 외 쿼리를 쓸경우 메서드에  트랜젝션널 어노테이션을 꼭 붙여야한다. 아니면 오류남
    public void upDateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO update(BoardDTO boardDTO){
        // jpa는 Entity만 인식하기 떄문에 DTO를 Entity로 변환 해줘야함
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        // jpa에는 따로 update기능을 제공해주지 않는다 대신 save() 기능에 pk(id)값을 포함해주면 알아서 업데이트로 인식해서 진행해준다.
        //수정 완료

                                // 수정 완료된 데이터를 찾아서 다시 가져온다.
        return findById(boardDTO.getId());
    }
}
