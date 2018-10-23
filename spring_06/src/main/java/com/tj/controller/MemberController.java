package com.tj.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tj.dto.Member;
import com.tj.exception.MemberNotFoundException;
import com.tj.exception.PasswordMissmatchException;
import com.tj.service.MemberService;

@Controller
public class MemberController {
	private Logger logger = Logger.getLogger(MemberController.class);
	
	// Service DI 구현 코드
	@Autowired private MemberService mService;
	
	@RequestMapping("/member/goLoginPage.do")
	public ModelAndView goLoginPage(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			RedirectView rv = new RedirectView("/s06/index.do");
			mv.setView(rv);
		}else {
			mv.setViewName("login");
		}
		return mv;
	}

	/**
	 * @param memberInfo
	 * @return ModelAndView
	 */
	@RequestMapping("/member/join.do")
	public ModelAndView join(@RequestParam HashMap<String, String> params) {
		String ui = params.get("ui");
		String up = params.get("up");
		String una = params.get("una");
		String uni = params.get("uni");
		String em = params.get("em");
		String bd = params.get("bd");

		Member m = new Member();
		m.setMemberId(ui);
		m.setMemberPw(up);
		m.setMemberName(una);
		m.setMemberNick(uni);
		m.setEmail(em);
		m.setBirthDate(bd);

		int resultCnt = mService.join(m);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		mv.addObject("msg", (resultCnt==1)?"회원가입 성공!": "회원가입 실패!");
		return mv;
	}

	@RequestMapping("/member/login.do")
	public ModelAndView login(@RequestParam HashMap<String, String> params, HttpSession session){
		String ui = params.get("ui");
		String up = params.get("up");

		String msg ="";
		ModelAndView mv = new ModelAndView();
		Member m = null;
		try {
			m = mService.login(ui, up);
			session.setAttribute("typeSeq", m.getTypeSeq());
			session.setAttribute("memberId", m.getMemberId());
			session.setAttribute("memberName", m.getMemberName());
			session.setAttribute("memberNick", m.getMemberNick());
			
			logger.debug("member type ----"+m.getTypeSeq());
			
			RedirectView rv = new RedirectView("/s06/index.do");
			mv.setView(rv);
			return mv;
		} catch(PasswordMissmatchException pme) {
			msg = pme.getMessage();
			mv.setViewName("login");
		} catch (MemberNotFoundException mnfe) {
			msg = mnfe.getMessage();
			mv.setViewName("login");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			msg="존재하지 않는 사용자 입니다.";
			e.printStackTrace();
		}

		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping("/member/logout.do")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mv = new ModelAndView();
		RedirectView rv = new RedirectView("/s06/index.do");
		mv.setView(rv);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/member/checkId.do")
	public int checkId(@RequestParam HashMap<String, String> params) {
		logger.debug("params ----"+params);
		int result = mService.checkId(params);
		logger.debug("checkIdCount ---- "+result);
		
		return result;
	}
}

