package com.jh.pf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/home.do")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/index.do")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/devNote.do")
	public String dev() {
		return "devNotes";
	}
	
	@RequestMapping("/something.do")
	public String something() {
		return "mList";
	}
	
	@RequestMapping("/anything.do")
	public String anything() {
		return "board/gList";
	}
	
}
