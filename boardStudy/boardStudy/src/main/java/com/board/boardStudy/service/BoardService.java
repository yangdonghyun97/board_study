package com.board.boardStudy.service;

import com.board.boardStudy.DTO.BoardDTO;
import com.board.boardStudy.entity.BoardEntity;
import com.board.boardStudy.entity.BoardFileEntity;
import com.board.boardStudy.repository.BoardFileRepositoty;
import com.board.boardStudy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.board.boardStudy.entity.BoardEntity.*;

// DTO -> Entity
// Entity -> DTO

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepositoty boardFileRepositoty;

    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리

        //isEmpty() 값이 있냐 없냐 ( 없냐 기준)
        if(boardDTO.getBoardFile().isEmpty()){
            //첨부 파일이 없는 경우
        // 서비스 단에서 DTO를 Entity객체로 바꿔준 후 db에 저장해준다.
        BoardEntity Entity = toSaveEntity(boardDTO);
        boardRepository.save(Entity);
        }else{
            //첨부 파일 있음
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg -> 97852136181_내사진.jpg 로 만들어준다 ( 중복 파일이름 방지)
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
            */

            MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
            String originalFilename = boardFile.getOriginalFilename(); // 2.
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
            String savePath = "C:\\Users\\KWH\\Desktop\\study\\board_img\\" + storedFileName; // 4.
            boardFile.transferTo(new File(savePath)); // 5.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepositoty.save(boardFileEntity);
        }

    }

    public List<BoardDTO> findALL() {
        List<BoardEntity> boardlist = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>(); // 빈 리스트 객체

        for (BoardEntity boardEntity : boardlist) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;

    }
@Transactional
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

    public BoardDTO update(BoardDTO boardDTO) {
        // jpa는 Entity만 인식하기 떄문에 DTO를 Entity로 변환 해줘야함
        BoardEntity boardEntity = toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        // jpa에는 따로 update기능을 제공해주지 않는다 대신 save() 기능에 pk(id)값을 포함해주면 알아서 업데이트로 인식해서 진행해준다.
        //수정 완료

        // 수정 완료된 데이터를 찾아서 다시 가져온다.
        return findById(boardDTO.getId());
    }

    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        // 배열의 인덱스는 0부터 시작하기 때문, 사용자가 1페이지 클릭하면 그건 인덱스[0]자료라 -1 해준다.
        int page = pageable.getPageNumber() - 1;

        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수

        //한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        //boardRepository에서 받아오기때문에 Entity 객체로 받아진다
        // page 위치에 있는 값음 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부


        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
