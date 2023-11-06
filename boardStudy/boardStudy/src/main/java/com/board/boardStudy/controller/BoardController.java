package com.board.boardStudy.controller;

import com.board.boardStudy.DTO.BoardDTO;
import com.board.boardStudy.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor // 생성자 자동으로 만들어주는 어노테이션
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


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
    public String findById(@PathVariable("id") Long id, Model model) {
        boardService.upDateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardView";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
       BoardDTO boardDTO = boardService.findById(id);
       model.addAttribute("boardUpdate", boardDTO);
       return "boardUpdate";
    }

    @PostMapping("/save")
    public String save(BoardDTO boardDTO) {
        boardService.save(boardDTO);
        return "index";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO, Model model){
      BoardDTO board =  boardService.update(boardDTO);
      model.addAttribute("board", board);

      return "boardView";
    }
}
