package com.board.boardStudy.controller;

import com.board.boardStudy.DTO.BoardDTO;
import com.board.boardStudy.DTO.CommentDTO;
import com.board.boardStudy.service.BoardService;
import com.board.boardStudy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor // 생성자 자동으로 만들어주는 어노테이션
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    //View
    @GetMapping("/save")
    public String saveForm() {

        return "save";
    }

    @GetMapping("/list")
    public String findALL(Model model) {
        //DB에서 전체 게시글 데이터를 가져와서
        List<BoardDTO> boardTOList = boardService.findALL();
        model.addAttribute("boardList", boardTOList);

        return "boardList";
    }

    @GetMapping("/{id}")  // 경로상으로 받는 {id}는  PathVariable를 써줘야 한다.
    public String findById(@PathVariable("id") Long id, Model model, @PageableDefault(page = 1) Pageable pageable) {
        boardService.upDateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "boardView";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
       BoardDTO boardDTO = boardService.findById(id);
       model.addAttribute("boardUpdate", boardDTO);
       return "boardUpdate";
    }

    @PostMapping("/save")
    public String save(BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "index";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO, Model model){
      BoardDTO board =  boardService.update(boardDTO);
      model.addAttribute("board", board);

      return "boardView";
    }

    @GetMapping("/delete/{id}")
    public  String boardDelte(@PathVariable("id") Long id){
        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    // /board/paging?page=1
    @GetMapping("/paging") //@PageableDefault(page = 1) 값이 없으면 1로 값을 정해서 사용하겠다.
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        pageable.getPageNumber();
        // page<> 는 list<> 와같이 스프링에서 제공하는 객체
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }

}
