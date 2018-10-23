package com.tj.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tj.dto.Board;
import com.tj.service.BoardService;

@Controller
public class BoardController {
	
	private Logger logger = Logger.getLogger(BoardController.class);
	
	@Autowired
	private BoardService bService;
	
	@Value("${project.context.path}")
	private String contextRoot;

	@RequestMapping("/board/list.do")
	public ModelAndView list(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		
		logger.debug("----"+params);
		mv.setViewName("/board/list");
		
		//페이징
		//현재 페이지
		int currentPage = params.containsKey("currentPage")?Integer.parseInt(params.get("currentPage")):1;
		//페이지 사이즈
		int pageSize = params.containsKey("pageSize")?Integer.parseInt(params.get("pageSize")):10;
		//블럭 사이즈
		//int blockSize = params.containsKey("blockSize")?Integer.parseInt(params.get("blockSize")):10;
		int blockSize = 10;
		//총 게시글 수
		int totalArticleCnt = bService.getTotalCount(params);
		
		int totalPage = totalArticleCnt / pageSize + 1;
		
		int startIndex = (currentPage-1)*pageSize;
		//int endIndex = currentPage*pageSize;
		
		int startBlock = ((currentPage-1)/blockSize)*blockSize+1;
		int endBlock = ((currentPage-1)/blockSize)*blockSize+10;
		endBlock = (endBlock>totalPage)?totalPage:endBlock;
		
		//게시글 전체를 가져온다.
		params.put("startIndex", String.valueOf(startIndex));
		params.put("pageSize", String.valueOf(pageSize));
		
		List<HashMap<String, Object>> result = bService.list(params);
		mv.addObject("list", result);
		mv.addObject("currentPage", currentPage);
		mv.addObject("startBlock", startBlock);
		mv.addObject("endBlock", endBlock);
		mv.addObject("totalPage", totalPage);
		//logger.debug("testlist ---- "+result+"----"+result.size());
		logger.debug("currentPage ---- "+currentPage);
		logger.debug("startBlock ---- "+startBlock);
		logger.debug("endBlock ---- "+endBlock);
		logger.debug("totalPage ---- "+totalPage);
		logger.debug("totalArticleCnt ---- "+totalArticleCnt);
		if(params.containsKey("msg")) {
			mv.addObject("msg",params.get("msg"));
		}
		mv.addObject("searchType", params.get("searchType"));
		mv.addObject("searchText", params.get("searchText"));
		
		return mv;
	}
	
	@RequestMapping("/board/goWritePage.do")
	public ModelAndView goWritePage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/board/write");
		
		return mv;
	}
	
	@RequestMapping("/board/write.do")
	public ModelAndView write(@RequestParam HashMap<String, String> params, MultipartHttpServletRequest mr) {
		//logger.debug("/board/write.do params - "+params);
		String typeSeq = params.get("typeSeq");
		String memberId = params.get("memberId");
		String memberNick = params.get("memberNick");
		String title = params.get("title");
		String contents = params.get("contents");
		//contents = contents.replaceAll("\\r", "<br>");
		//contents = contents.replaceAll("\\n", "<br>");
		//contents = contents.replaceAll("\\r\\n", "<br>");
		
		Board b = new Board();
		b.setTypeSeq(typeSeq);
		b.setMemberId(memberId);
		b.setMemberNick(memberNick);
		b.setTitle(title);
		b.setContent(contents);
		
		List<MultipartFile> mf = mr.getFiles("file");
		for(MultipartFile mp : mf) {
			if(mp.getOriginalFilename()!="") {
				b.setHasFile("1");
			}else {
				b.setHasFile("0");
			}
		}
		
		int resultCnt = bService.write(b,mf);
				
		ModelAndView mv = new ModelAndView();
		RedirectView rv = new RedirectView(contextRoot+"/board/list.do?typeSeq=2");
		mv.setView(rv);
		return mv;
	}
	
	@RequestMapping("/board/read.do")
	public ModelAndView read(@RequestParam int typeSeq, @RequestParam int boardSeq) {
		ModelAndView mv = new ModelAndView();
		
		Board result = bService.read(typeSeq, boardSeq);
		mv.setViewName("/board/read");
		mv.addObject("board", result);
		
		if(result.getHasFile().equals("1")) {
			List<HashMap<String, Object>> gfResult = bService.getFile(boardSeq, typeSeq);
			logger.debug(gfResult);
			mv.addObject("files", gfResult);
		}
		
		return mv;
	}
	
	@RequestMapping("/board/delete.do")
	public ModelAndView delete(@RequestParam int typeSeq, @RequestParam int boardSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			Board resultRead = bService.read(typeSeq, boardSeq);
			if(session.getAttribute("memberId").equals(resultRead.getMemberId())) {
				int result = bService.delete(typeSeq, boardSeq, resultRead.getHasFile());
				if(result!=1) {
					mv.setViewName("/board/read");
					mv.addObject("board", resultRead);
					mv.addObject("msg", "삭제 실패! 관리자에게 문의하세요.");
				}else {
					RedirectView rv = new RedirectView(contextRoot+"/board/list.do?typeSeq=2");
					mv.setView(rv);
					mv.addObject("msg", "삭제 성공!");
				}
			}else {
				mv.setViewName("common/error");
				mv.addObject("msg", "삭제 권한이 없습니다.");
				mv.addObject("nextLocation", "/index.do");
			}
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@RequestMapping("/board/goUpdate.do")
	public ModelAndView goUpdate(@RequestParam int typeSeq, @RequestParam int boardSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			Board result = bService.read(typeSeq, boardSeq);
			mv.setViewName("/board/update");
			mv.addObject("board", result);
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@RequestMapping("/board/update.do")
	public ModelAndView update(@RequestParam HashMap<String, String> params, HttpSession session, MultipartHttpServletRequest mr) {
		logger.debug("params ---- "+params);
		
		ModelAndView mv = new ModelAndView();
		String typeSeq = params.get("typeSeq");
		String boardSeq = params.get("boardSeq");
		String memberId = params.get("memberId");
		String memberNick = params.get("memberNick");
		String title = params.get("title");
		String contents = params.get("contents");
		int ts = Integer.parseInt(typeSeq);
		int bs = Integer.parseInt(boardSeq);
		
		logger.debug("typeSeq ---- "+typeSeq);
		logger.debug("boardSeq ---- "+boardSeq);
		
		if(session.getAttribute("memberId").equals(memberId)) {
			Board b = new Board();
			b.setTypeSeq(typeSeq);
			b.setBoardSeq(boardSeq);
			b.setMemberId(memberId);
			b.setMemberNick(memberNick);
			b.setTitle(title);
			b.setContent(contents);
			
			List<MultipartFile> files = mr.getFiles("file");
			if(params.get("hasFile").equals("0")) {
				for(MultipartFile m : files) {
					//첨부한 파일 이름이 공백이 아니라면
					if(!m.getOriginalFilename().equals("")) {
						b.setHasFile("1");
					}else {
						b.setHasFile("0");
					}
				}
			}
			
			int resultCnt = bService.update(b, files);
			
			if(resultCnt==1) {
				
				Board result = bService.read(ts, bs);
				mv.setViewName("/board/read");
				mv.addObject("board", result);
				mv.addObject("msg", "성공");
			}else {
				mv.setViewName("/board/read");
				mv.addObject("msg", "실패");
			}
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
}
