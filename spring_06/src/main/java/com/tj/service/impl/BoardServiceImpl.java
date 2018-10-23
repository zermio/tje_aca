package com.tj.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tj.controller.BoardController;
import com.tj.dao.BoardDao;
import com.tj.dto.Board;
import com.tj.service.BoardService;
import com.tj.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService{
	
	private Logger logger = Logger.getLogger(BoardController.class);
	
	@Autowired
	private BoardDao bDao;
	
	@Autowired
	private FileUtil fUtil;

	@Override
	public int write(Board b, List<MultipartFile> mf) {
		int resultCnt = bDao.write(b);
		//System.out.println("After boardSeq --- "+b.getBoardSeq());
		
		//첨부파일 있으면 board_attach 테이블에 등록
		if(b.getHasFile().equals("1")) {
			HashMap<String, Object> ab = new HashMap<>();
			for(MultipartFile mp : mf) {
				ab.put("boardSeq", b.getBoardSeq());
				ab.put("boardType", b.getTypeSeq());
				ab.put("filename", mp.getOriginalFilename());
				ab.put("fileSize", mp.getSize());
				ab.put("fileType", mp.getContentType());
				if(!mp.getOriginalFilename().equals("")) {
					String fakename = UUID.randomUUID().toString().replace("-", "");
					ab.put("fakename", fakename);
					try {
						fUtil.copyFile(mp, fakename);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			int resultAttach = bDao.attach(ab);
		}
		return resultCnt;
	}

	@Override
	public List<HashMap<String, Object>> list(HashMap<String, String> params) {
		logger.debug("service list params ---- "+params);
		List<HashMap<String, Object>> result = bDao.list(params);
		return result;
	}

	@Override
	public int getTotalCount(HashMap<String,String> params) {
		return bDao.getTotalCount(params);
	}

	@Override
	public Board read(int typeSeq, int boardSeq ) {
		int resultUp = bDao.updateHits(typeSeq, boardSeq);
		Board result = bDao.getBoard(typeSeq, boardSeq);
		
		return result;
	}

	@Override
	public List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq) {
		List<HashMap<String, Object>> result = bDao.getFile(boardSeq, typeSeq);
		logger.debug("getFile result ---- "+result);
		return result;
	}
	
	@Override
	public int delete(int typeSeq, int boardSeq, String hasFile) {
		int result = 0;
		if(hasFile.equals("1")) {
			//글번호, 타입으로 첨부파일을 삭제하는 DAO 호출
			List<HashMap<String, Object>> readAttach =  bDao.getFile(boardSeq, typeSeq);
			logger.debug("get ser readl ---- "+readAttach);
			logger.debug("get ser readlm0 ---- "+readAttach.get(0));
			for(int i=0;i<readAttach.size();i++) {
				bDao.deleteAttach((int)readAttach.get(i).get("file_idx"));
				fUtil.deleteFile(readAttach.get(i));
			}
			/*for(HashMap<String, Object> file : readAttach) {
				fUtil.deleteFile(file);
			}*/
			result = bDao.delete(typeSeq, boardSeq);
			return result;
		}
		result = bDao.delete(typeSeq, boardSeq);
		return result;
	}

	@Override
	public int update(Board b, List<MultipartFile> mf) {
		if(b.getHasFile().equals("1")) {
			HashMap<String, Object> ab = new HashMap<>();
			for(MultipartFile mp : mf) {
				ab.put("boardSeq", b.getBoardSeq());
				ab.put("boardType", b.getTypeSeq());
				ab.put("filename", mp.getOriginalFilename());
				ab.put("fileSize", mp.getSize());
				ab.put("fileType", mp.getContentType());
				if(!mp.getOriginalFilename().equals("")) {
					String fakename = UUID.randomUUID().toString().replace("-", "");
					ab.put("fakename", fakename);
					try {
						fUtil.copyFile(mp, fakename);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			int resultAttach = bDao.attach(ab);
		}
		
		int result = bDao.update(b);
		return result;
	}

	@Override
	public int changeHasFile(int boardSeq) {
		int result = bDao.changeHasFile(boardSeq);
		return result;
		
	}

	@Override
	public boolean deleteAttach(int fileIdx, int boardSeq, int typeSeq) {
		
		boolean result = false;
		
		//첨부파일 정보를 가져온다.
		HashMap<String, Object> fileInfo = bDao.getAttachFile(fileIdx);
		//db에서 삭제
		result = (bDao.deleteAttach(fileIdx)==1);
		if(result) {
			//물리적으로 삭제
			result = (fUtil.deleteFile(fileInfo)==result);
		}else {
			return result;
		}
		//파일 가졌는지 다시 확인하기
		int hm = bDao.hmFile(boardSeq);
		 //가진 파일이 없으면 수정하기
		if(hm==0) {
			int chf = bDao.changeHasFile(boardSeq);
			result = ((chf==1)&&result);
		}
		/*List<Map<String, Object>> files = getFile(boardSeq, typeSeq);
		if(files==null||files.size()==0) {
			int chf = bDao.changeHasFile(boardSeq);
		}*/
		
		return result;
	}

	

}