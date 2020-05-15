package com.mj.jy.board.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mj.jy.board.model.service.BoardService;
import com.mj.jy.board.model.vo.Board;
import com.mj.jy.board.model.vo.BoardDto;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@RequestMapping("boardList.bo")
	public String boardListView(Model model) {
		
		ArrayList<BoardDto> list = boardService.selectBoardList();
		
//		System.out.println(list);
		
		model.addAttribute("list", list);
		
		return "board/boardListView";
	}
	
	@RequestMapping("detail.bo")
	public ModelAndView selectBoard(int bno, ModelAndView mv) {
		
		// 조회수 증가
		int result = boardService.increaseCount(bno);
		
		if(result > 0) { // 조회수 증가 성공 --> 유효한 게시글 --> 조회
			
			BoardDto b = boardService.selectBoard(bno);
			
			mv.addObject("b", b).setViewName("board/boardDetailView");
			 
		}else {	// 조회수 증가 실패 --> 유효한 게시글 x --> 에러페이지
			mv.addObject("msg", "유효한 게시글 아님");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
		
	}
	
	@RequestMapping("enrollForm.bo")
	public String enrollFrom() {
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, HttpServletRequest request, Model model) {
		
		int result = boardService.insertBoard(b);
		
		if(result > 0) {	// 게시판 등록 성공
			return "redirect:boardList.bo";			
		}else {		// 게시판 등록 실패
			model.addAttribute("msg", "게시판 작성 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("updateForm.bo")
	public ModelAndView updateForm(int bno, ModelAndView mv) {
		
		BoardDto b = boardService.selectBoard(bno);
		
		mv.addObject("b", b).setViewName("board/boardUpdateForm");
		
		return mv;
		
	}
	
	@RequestMapping("update.bo")
	public ModelAndView updateBoard(Board b, HttpServletRequest request, ModelAndView mv) {
				
		int result = boardService.updateBoard(b);
		
		if(result > 0) {	// 수정 성공했을 경우 --> 상세보기 요청
			
			mv.addObject("bno", b.getBoardNo()).setViewName("redirect:detail.bo");	// detail.bo?bno=
			
		}else {		// 수정 실패했을 경우 --> errorPage
			mv.addObject("msg", "수정 실패").setViewName("common/errorPage");
		}
		
		return mv;
		
	}
	
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, HttpServletRequest request, Model model) {
		
		int result = boardService.deleteBoard(bno);
		
		if(result > 0) { 
			
			return "redirect:boardList.bo";
			
		}else {	// 삭제 실패
			
			model.addAttribute("msg", "삭제 실패");
			
			return "common/errorPage";
		}
		
	}
	
	
}
