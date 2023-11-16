package com.ssafy.attraction.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attraction")
public class AttractionController {
	
	public AttractionController() {}
	
	@GetMapping("/attraction")
	public String attraction(HttpSession session, Model model) {
		//로그인되어있으면 지도 접근 가능
		if (session.getAttribute("userDto") != null) {
			
			return "attraction/attraction";
			
		} else {
			model.addAttribute("msg", "로그인 후 사용 바랍니다.");
			
			return "/error/error";
		}
	}
}
