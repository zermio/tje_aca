package com.jh.pf.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jh.pf.dto.Board;
import com.jh.pf.service.BoardAttachService;
import com.jh.pf.service.BoardService;
import com.jh.pf.util.FileUtil;

@Controller
public class BoardController {
	
	Logger logger = Logger.getLogger(BoardController.class);
	
	@Autowired
	private BoardService bSer;
	
	@Autowired
	private BoardAttachService aSer;
	
	@Autowired
	private FileUtil fUtil;
	
	@Value("#{config['list.page.size']}")
	private int lps;
	
	@Value("#{config['list.block.size']}")
	private int lbs;
	
	@Value("#{config['context.path']}")
	private String cp;

	@RequestMapping("/board/list.do")
	public ModelAndView list(@RequestParam HashMap<String, Object> params) {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("board/list");
		
		int currentPage = params.containsKey("currentPage")?(int)params.get("currentPage"):1;
		
		int pageSize = params.containsKey("pageSize")?(int)params.get("pageSize"):lps;
		
		int blockSize = lbs;

		int totalArticleCnt = bSer.getTotalCount(params);
		
		int totalPage = (totalArticleCnt-1) / pageSize + 1;

		int startIndex = (currentPage-1)*pageSize;

		int startBlock = ((currentPage-1)/blockSize)*blockSize+1;
		
		int endBlock = ((currentPage-1)/blockSize)*blockSize+blockSize;
		endBlock = (endBlock>totalPage)?totalPage:endBlock;
		
		params.put("startIndex", String.valueOf(startIndex));
		params.put("pageSize", String.valueOf(pageSize));
		
		ArrayList<HashMap<String, Object>> result = bSer.list(params);
		
		mv.addObject("list", result);
		mv.addObject("currentPage", currentPage);
		mv.addObject("startBlock", startBlock);
		mv.addObject("endBlock", endBlock);
		mv.addObject("totalPage", totalPage);
		if(params.containsKey("msg")) {
			mv.addObject("msg",params.get("msg"));
		}
		return mv;
	}
	
	@RequestMapping("/board/read.do")
	public ModelAndView read(@RequestParam int boardSeq, @RequestParam int typeSeq) {
		ModelAndView mv = new ModelAndView();
		
		Board result = bSer.read(boardSeq,typeSeq);
		if(result!=null) {
			bSer.updateHits(boardSeq, typeSeq);
		}
		
		mv.setViewName("board/read");
		mv.addObject("board", result);
		if(result.getHasFile().equals("1")) {
			List<HashMap<String, Object>> gfResult = bSer.getFile(boardSeq, typeSeq);
			mv.addObject("files", gfResult);
		}
		
		ArrayList<HashMap<String, Object>> list = bSer.rList(boardSeq,typeSeq);
		mv.addObject("rList", list);
		
		return mv;
	}
	
	@RequestMapping("/board/goWrite.do")
	public ModelAndView goWritePage(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		
		String bt = params.get("bt");
		
		mv.setViewName("board/write");
		mv.addObject("bt", bt);
		
		return mv;
	}
	
	@RequestMapping("/board/write.do")
	public ModelAndView write(@RequestParam HashMap<String, String> params, MultipartHttpServletRequest mr) {
		ModelAndView mv = new ModelAndView();
		
		int typeSeq = Integer.parseInt(params.get("typeSeq"));
		int memberIdx = Integer.parseInt(params.get("memberIdx"));
		String memberId = params.get("memberId");
		String memberNick = params.get("memberNick");
		String title = params.get("title");
		String contents = params.get("contents");
		int bt = Integer.parseInt(params.get("bt"));
		
		Board b = new Board();
		b.setTypeSeq(typeSeq);
		b.setMemberIdx(memberIdx);
		b.setMemberId(memberId);
		b.setMemberNick(memberNick);
		b.setTitle(title);
		b.setContent(contents);
		
		logger.debug("rec mr ---- "+mr);
		List<MultipartFile> mf = mr.getFiles("file");
		logger.debug("mf ---- "+mf);
		for(MultipartFile mp : mf) {
			if(mp.getOriginalFilename()!="") {
				b.setHasFile("1");
			}else {
				b.setHasFile("0");
			}
		}
		int result = bSer.write(b, mf);
		
		RedirectView rv = null;
		if(bt==0) {
			rv = new RedirectView(cp+"/board/list.do?typeSeq="+typeSeq);
		}else if(bt==1){
			rv = new RedirectView(cp+"/anything.do");
		}
		mv.setView(rv);
		if(result==1) {
			mv.addObject("msg", "글쓰기 성공");
		}else {
			mv.addObject("msg", "글쓰기 실패");
		}
		return mv;
	}
	
	@RequestMapping("/board/goUpdate.do")
	public ModelAndView goUpdate(@RequestParam int boardSeq, @RequestParam int typeSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		if(session.getAttribute("memberId")!=null) {
			Board result = bSer.read(boardSeq, typeSeq);
			mv.setViewName("board/update");
			mv.addObject("board", result);
			
			logger.debug("goup rec read to board ---- "+result);
			
			if(result.getHasFile().equals("1")) {
				List<HashMap<String, Object>> gfResult = bSer.getFile(boardSeq, typeSeq);
				mv.addObject("files", gfResult);
			}
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@RequestMapping("/board/update.do")
	public ModelAndView update(@RequestParam HashMap<String, String> params, HttpSession session, MultipartHttpServletRequest mr) {
		ModelAndView mv = new ModelAndView();
		
		int typeSeq = Integer.parseInt(params.get("typeSeq"));
		int boardSeq = Integer.parseInt(params.get("boardSeq"));
		String memberId = params.get("memberId");
		String memberNick = params.get("memberNick");
		String title = params.get("title");
		String contents = params.get("contents");
		
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
					if(!m.getOriginalFilename().equals("")) {
						b.setHasFile("1");
					}else {
						b.setHasFile("0");
					}
				}
			}
			int resultCnt = bSer.update(b, files);
			
			if(resultCnt==1) {
				RedirectView rv = new RedirectView(cp+"/board/read.do?typeSeq="+typeSeq+"&boardSeq="+boardSeq);
				mv.setView(rv);
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
	
	@RequestMapping("/board/delete.do")
	public ModelAndView delete(@RequestParam int boardSeq, @RequestParam int typeSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			Board resultRead = bSer.read(boardSeq, typeSeq);
			if(session.getAttribute("memberId").equals(resultRead.getMemberId())) {
				int result = bSer.delete(boardSeq, typeSeq, resultRead.getHasFile());
				if(result!=1) {
					mv.setViewName("board/read");
					mv.addObject("board", resultRead);
					mv.addObject("msg", "삭제 실패!");
				}else {
					RedirectView rv = new RedirectView(cp+"/board/list.do?typeSeq="+typeSeq);
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
			mv.addObject("msg", "다시 로그인 해주세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@RequestMapping("/board/deleteAttach.do")
	public ModelAndView deleteAttach(@RequestParam int fileIdx, @RequestParam int typeSeq, @RequestParam int boardSeq) {
		ModelAndView mv = new ModelAndView();
		
		boolean result = bSer.deleteAttach(fileIdx, boardSeq, typeSeq);
		
		RedirectView rv = new RedirectView(cp+"/board/goUpdate.do?typeSeq="+typeSeq+"&boardSeq="+boardSeq);
		mv.setView(rv);
		
		if(result==true) {
			mv.addObject("msg", "첨부파일 삭제 성공");
		}else {
			mv.addObject("msg", "첨부파일 삭제 실패");
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/board/download.do")
	public byte[] download(@RequestParam int fileIdx, HttpServletResponse rep) {
		HashMap<String, Object> fileInfo = aSer.getAttachFile(fileIdx);
		
		byte[] b = null;
		if(fileInfo==null) { 
			
		}else { 
			b = fUtil.readFile(fileInfo);	
			
			String encodingName = null;
			try {
				encodingName = java.net.URLEncoder.encode(fileInfo.get("filename").toString(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rep.setHeader("Content-Disposition", "attachment; filename=\"" + encodingName+"\"");
			rep.setContentType(String.valueOf(fileInfo.get("file_type")));
			rep.setHeader("Pragma", "no-cache");
			rep.setHeader("Cache-Control", "no-cache");
			String tmp = String.valueOf(fileInfo.get("file_size"));
			rep.setContentLength(Integer.parseInt(tmp));
		}
		return b;
	}
	
	@ResponseBody
	@RequestMapping("/board/gList.do")
	public HashMap<String, Object> gList(@RequestParam HashMap<String, Object> params) {
		
		logger.debug("con rec params ---- "+params);
		
		int currentPage = params.containsKey("currentPage")?(int)params.get("currentPage"):1;
		
		int pageSize = params.containsKey("pageSize")?(int)params.get("pageSize"):lps;
		
		int blockSize = lbs;

		int totalArticleCnt = bSer.getTotalCount(params);
		
		int totalPage = (totalArticleCnt-1) / pageSize + 1;

		int startIndex = (currentPage-1)*pageSize;

		int startBlock = ((currentPage-1)/blockSize)*blockSize+1;
		
		int endBlock = ((currentPage-1)/blockSize)*blockSize+blockSize;
		endBlock = (endBlock>totalPage)?totalPage:endBlock;
		
		params.put("startIndex", String.valueOf(startIndex));
		params.put("pageSize", String.valueOf(pageSize));
		
		ArrayList<HashMap<String, Object>> list = bSer.list(params);
		logger.debug("con rec list ---- "+list);
		
		HashMap<String, Object> result = new HashMap<String, Object>();

		result.put("page", currentPage);
		result.put("total", totalPage);
		result.put("rows", list);
		result.put("records", totalArticleCnt);
		logger.debug("con rec result ---- "+result);
		
		return result;
	}
	
		
	@RequestMapping("/board/reply.do")
	public ModelAndView reply(@RequestParam HashMap<String, String> params, HttpSession session) {
		logger.debug("reply.do rec req params ---- "+params);
		String typeSeq = params.get("typeSeq");
		String boardSeq = params.get("boardSeq");
		ModelAndView mv = new ModelAndView();
		RedirectView rv = new RedirectView(cp+"/board/read.do?typeSeq="+typeSeq+"&boardSeq="+boardSeq);
		
		if(session.getAttribute("memberId")!=null) {
			int result = bSer.insertReply(params);
			mv.setView(rv);
			if(result==1) {
				mv.addObject("msg", "댓글 작성 완료");
			}else {
				mv.addObject("msg", "댓글 작성 실패");
			}
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 해주세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
}
