package com.tj.controller;

import java.io.UnsupportedEncodingException;
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

import com.tj.dto.Board;
import com.tj.service.AttachService;
import com.tj.service.BoardService;
import com.tj.util.FileUtil;

@Controller
public class NoticeController {

	private Logger logger = Logger.getLogger(NoticeController.class);

	@Autowired
	private BoardService bService;
	
	@Autowired
	private AttachService aService;
	
	@Autowired
	private FileUtil fUtil;
	
	@Value("#{config['project.context.path']}")
	private String contextRoot;

	@RequestMapping("/notice/list.do")
	public ModelAndView list(@RequestParam HashMap<String, String> params) {
		ModelAndView mv = new ModelAndView();
		
		logger.debug("----"+params);

		mv.setViewName("/board/noticeList");
		
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
		//logger.debug("currentPage ---- "+currentPage);
		//logger.debug("startBlock ---- "+startBlock);
		//logger.debug("endBlock ---- "+endBlock);
		//logger.debug("totalPage ---- "+totalPage);
		if(params.containsKey("msg")) {
			mv.addObject("msg",params.get("msg"));
		}
		return mv;
	}
	
	@RequestMapping("/notice/goWritePage.do")
	public ModelAndView goWritePage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/board/noticeWrite");
		
		return mv;
	}
	
	@RequestMapping("/notice/write.do")
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
				//params.put("hasFile","1");
				b.setHasFile("1");
			}else {
				//params.put("hasFile","0");
				b.setHasFile("0");
			}
			System.out.println("name : "+mp.getOriginalFilename());
			System.out.println("type : "+mp.getContentType());
			System.out.println("size : "+mp.getSize());
		}
		
		int resultCnt = bService.write(b, mf);
				
		ModelAndView mv = new ModelAndView();
		RedirectView rv = new RedirectView(contextRoot+"/notice/list.do?typeSeq=1");
		mv.setView(rv);
		return mv;
	}
	
	@RequestMapping("/notice/read.do")
	public ModelAndView read(@RequestParam int typeSeq, @RequestParam int boardSeq) {
		ModelAndView mv = new ModelAndView();
		
		Board result = bService.read(typeSeq, boardSeq);
		mv.setViewName("/board/noticeRead");
		mv.addObject("board", result);
		
		if(result.getHasFile().equals("1")) {
			List<HashMap<String, Object>> gfResult = bService.getFile(boardSeq, typeSeq);
			logger.debug(gfResult);
			mv.addObject("files", gfResult);
		}
		
		return mv;
	}
	
	@RequestMapping("/notice/delete.do")
	public ModelAndView delete(@RequestParam int typeSeq, @RequestParam int boardSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			Board resultRead = bService.read(typeSeq, boardSeq);
			if(session.getAttribute("memberId").equals(resultRead.getMemberId())) {
				int result = bService.delete(typeSeq, boardSeq, resultRead.getHasFile());
				if(result!=1) {
					mv.setViewName("/board/noticeRead");
					mv.addObject("board", resultRead);
					mv.addObject("msg", "삭제 실패!");
				}else {
					RedirectView rv = new RedirectView(contextRoot+"/notice/list.do?typeSeq=1");
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
	
	@RequestMapping("/notice/goUpdate.do")
	public ModelAndView goUpdate(@RequestParam int typeSeq, @RequestParam int boardSeq, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if(session.getAttribute("memberId")!=null) {
			Board result = bService.read(typeSeq, boardSeq);
			mv.setViewName("/board/noticeUpdate");
			mv.addObject("board", result);
			
			if(result.getHasFile().equals("1")) {
				List<HashMap<String, Object>> gfResult = bService.getFile(boardSeq, typeSeq);
				logger.debug(gfResult);
				mv.addObject("files", gfResult);
			}
			
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@RequestMapping("/notice/update.do")
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

			//<input type="file" .../> 가 있어야만 함
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
				RedirectView rv = new RedirectView(contextRoot+"/notice/read.do?typeSeq="+ts+"&boardSeq="+bs);
				mv.setView(rv);
				mv.addObject("msg", "성공");
			}else {
				mv.setViewName("/board/noticeRead");
				mv.addObject("msg", "실패");
			}
		}else {
			mv.setViewName("common/error");
			mv.addObject("msg", "다시 로그인 하세요.");
			mv.addObject("nextLocation", "/index.do");
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/notice/download.do")
	public byte[] download(@RequestParam int fileIdx, HttpServletResponse rep) {
		logger.debug("con rec fi ---- "+fileIdx);
		//pk로 첨부파일 정보 조회
		//BoardAttachService 생성
		HashMap<String, Object> fileInfo = aService.getAttachFile(fileIdx);
		logger.debug("con rec fileInfo ---- "+fileInfo);
		
		byte[] b = null;
		if(fileInfo==null) { //파일이 지워졌다면
			
		}else { //파일을 읽어와 돌려보낸다
			b = fUtil.readFile(fileInfo);	//읽기
			
			//한글 인코딩
			String encodingName = null;
			try {
				encodingName = java.net.URLEncoder.encode(fileInfo.get("filename").toString(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//브라우저에 정보 전달
			rep.setHeader("Content-Disposition", "attachment; filename=\"" + encodingName+"\"");
			rep.setContentType(String.valueOf(fileInfo.get("file_type")));
			rep.setHeader("Pragma", "no-cache");
			rep.setHeader("Cache-Control", "no-cache");
			String tmp = String.valueOf(fileInfo.get("file_size"));
			rep.setContentLength(Integer.parseInt(tmp));
		}
		return b;
	}
	
	@RequestMapping("/notice/deleteAttach.do")
	public ModelAndView deleteAttach(@RequestParam int fileIdx, @RequestParam int typeSeq, @RequestParam int boardSeq) {
		logger.debug("rec con fileIdx ---- "+fileIdx);
		
		ModelAndView mv = new ModelAndView();
		
		//트랜젝션이 일어나는 작업은 컨트롤러에서 하지 않는다.
		/*//첨부파일 정보를 가져온다.
		HashMap<String, Object> fileInfo = aService.getAttachFile(fileIdx);
		//db에서 삭제
		int result = aService.delete(fileIdx);
		//물리적으로 삭제
		fUtil.deleteFile(fileInfo);
		//파일 가졌는지 다시 확인하기
		int hm = aService.hmFile(boardSeq);
		if(hm==0) { //가진 파일이 없으면 수정하기
			int chf = bService.changeHasFile(boardSeq);
		}*/
		
		boolean result = bService.deleteAttach(fileIdx, boardSeq, typeSeq);
		
		//삭제가 되든 안되든 페이지를 다시 보여줌
		RedirectView rv = new RedirectView(contextRoot+"/notice/goUpdate.do?typeSeq="+typeSeq+"&boardSeq="+boardSeq);
		mv.setView(rv);
		
		if(result==true) {
			mv.addObject("msg", "첨부파일 삭제 성공");
		}else {
			mv.addObject("msg", "첨부파일 삭제 실패");
		}
		
		return mv;
	}
}
