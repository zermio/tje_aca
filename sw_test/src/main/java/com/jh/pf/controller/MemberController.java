package com.jh.pf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jh.pf.dto.Member;
import com.jh.pf.exception.MemberNotFoundException;
import com.jh.pf.exception.PasswordMissmatchException;
import com.jh.pf.service.MemberService;

@Controller
public class MemberController {
	
	private Logger logger = Logger.getLogger(MemberController.class);
	
	@Value("#{config['context.path']}")
	private String cp;

	@Autowired
	private MemberService mSer;
	
	/**
	 * 로그인페이지로 이동
	 * @param ss
	 * @return
	 */
	@RequestMapping("/member/goLogin.do")
	public ModelAndView goLogin(HttpSession ss) {
		ModelAndView mv = new ModelAndView();
		logger.debug("goLogin start session ---- "+ss);
		if(ss.getAttribute("memberId")==null) {
			mv.setViewName("login");
		}else {
			RedirectView rv = new RedirectView(cp+"/index.do");
			mv.setView(rv);
		}
		return mv;
	}
	
	/**
	 * 회원 가입 시 아이디 중복 체크
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/member/checkId.do")
	public int checkId(@RequestParam HashMap<String, String> params) {
		int result = mSer.checkId(params); 
		return result;
	}
	
	/**
	 * 회원가입
	 * @param params
	 * @return
	 */
	@RequestMapping("/member/join.do")
	public ModelAndView join(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		
		String ui = params.get("ui");
		String up = params.get("up");
		String una = params.get("una");
		String uni = params.get("uni");
		String em = params.get("em");
		//String bd = params.get("bd");

		Member m = new Member();
		m.setMemberId(ui);
		m.setMemberPw(up);
		m.setMemberName(una);
		m.setMemberNick(uni);
		m.setEmail(em);
		//m.setBirthDate(bd);
		
		int result = mSer.join(m);
		
		RedirectView rv = new RedirectView(cp+"/member/goLogin.do");
		mv.setView(rv);
		mv.addObject("msg", (result==1)?"회원가입 성공!": "회원가입 실패!");
		return mv;
	}
	
	/**
	 * 로그인
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("/member/login.do")
	public ModelAndView login(@RequestParam HashMap<String, String> params, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		String ui = params.get("ui");
		String up = params.get("up");
		
		Member m = null;
		String msg ="";

		try {
			m = mSer.login(ui, up);
			session.setAttribute("userType", m.getTypeSeq());
			session.setAttribute("memberIdx", m.getMemberIdx());
			session.setAttribute("memberId", m.getMemberId());
			session.setAttribute("memberName", m.getMemberName());
			session.setAttribute("memberNick", m.getMemberNick());
			
			RedirectView rv = new RedirectView(cp+"/index.do");
			mv.setView(rv);
			return mv;
		} catch(PasswordMissmatchException pme) {
			msg = pme.getMessage();
			mv.setViewName("login");
		} catch (MemberNotFoundException mnfe) {
			msg = mnfe.getMessage();
			mv.setViewName("login");
		} catch (Exception e) {
			msg="존재하지 않는 사용자 입니다.";
			e.printStackTrace();
		}
		mv.addObject("msg", msg);
		return mv;
	}
	
	/**
	 * 로그아웃
	 * @param session
	 * @return
	 */
	@RequestMapping("/member/logout.do")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		session.invalidate();
		
		RedirectView rv = new RedirectView(cp+"/index.do");
		mv.setView(rv);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/member/mList.do")
	public HashMap<String, Object> mList(@RequestParam HashMap<String, String> params){
		logger.debug("con rec params ---- "+params);
		
		int pageSize = Integer.parseInt(params.get("rows"));
		int currentPage = Integer.parseInt(params.get("page"));
		//전체 회원 수 구하기
		int countMember = mSer.cMember(params);
		
		int totalPage = (countMember-1) / pageSize + 1;

		int startIndex = (currentPage-1)*pageSize;

		params.put("startIndex", String.valueOf(startIndex));
		
		ArrayList<HashMap<String, String>> list = mSer.mList(params);
		
		logger.debug("con rec mlist ---- "+list);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put("page", params.get("page"));
		result.put("total", totalPage);
		result.put("rows", list);
		result.put("records", countMember);
		logger.debug("con rec result ---- "+result);

		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/member/delMember.do")
	public HashMap<String,String> delMember(@RequestParam HashMap<String, String> params) {
		logger.debug("con dM rec params ---- "+params);
		
		int result = mSer.delMember(params);
		String sResult = String.valueOf(result);
		
		logger.debug("con rec dM result ---- "+result);
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put("msg", (result==1)?"삭제 성공":"삭제 실패");
		map.put("result", sResult);
		
		logger.debug("dM result map ---- "+map);

		return map;
	}
	
}
