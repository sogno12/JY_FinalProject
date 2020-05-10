package com.mj.jy.namecard.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.mj.jy.businessRoom.model.service.BusinessRoomService;
import com.mj.jy.businessRoom.model.vo.BusinessDTO;
import com.mj.jy.namecard.model.service.NamecardService;
import com.mj.jy.namecard.model.vo.Namecard;
import com.mj.jy.namecard.model.vo.PageInfo;
import com.mj.jy.namecard.model.vo.Pagination;

@Controller
public class NamecardController {

	@Autowired
	private NamecardService nService;
	
	@Autowired
	private BusinessRoomService bService;

	/**
	 * 명함신청 입력폼controller
	 * 
	 * @return
	 */
	@RequestMapping("namecardForm.nc")
	public String namecardForm() {
		return "convenience/namecard/namecardForm";
	}

	/**
	 * 명함신청 insert controller
	 * 
	 * @param n
	 * @param model
	 * @return
	 */
	@RequestMapping("insertNamecard.nc")
	public String insertNamecard(Namecard n, Model model) {

		// System.out.println(n);

		int result = nService.insertNamecard(n);

		// System.out.println(result);

		if (result > 0) {
			model.addAttribute("msg", "명함신청이 완료되었습니다.");
			return "convenience/namecard/namecardSuccess";
		} else {
			model.addAttribute("msg", "명함을 다시 신청해주십시오.");
			return "common/errorPage";
		}

	}

	/**
	 * 예약메인view
	 * 
	 * @return
	 */
	@RequestMapping("reservationList.nc")
	public String namecardSelectList(Model model,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {

		int nameListCount = nService.getNameListCount();
		int broomListCount = nService.getBroomListCount();
		
		  PageInfo pi = Pagination.getPageInfo(nameListCount, currentPage, 10, 5);
		  PageInfo p2 = Pagination.getPageInfo(broomListCount, currentPage, 10, 5);
		  
		  ArrayList<Namecard> nlist = nService.selectNameList(pi);
		 

		  ArrayList<BusinessDTO> blist = bService.selectBroomList();
		  
		
		  model.addAttribute("pi",pi); 
		  model.addAttribute("p2",p2);
		  model.addAttribute("nlist", nlist);
		  model.addAttribute("blist", blist);
		 
		return "convenience/reservation/reservationList";
	}
	
	@RequestMapping(value="nameUpdate.nc")
	public void nameUpdate(int namecardNo, HttpServletResponse response) throws JsonIOException, IOException {
		
		 response.setContentType("application/json; charset=utf-8");
		
		int result = nService.nameUpdate(namecardNo);
	
		String result2 = "";
		
		if(result > 0) {
			result2 = "1";
		} else {
			result2 = "0";
		}
		
		 new Gson().toJson(result2, response.getWriter());
		
	}

}
