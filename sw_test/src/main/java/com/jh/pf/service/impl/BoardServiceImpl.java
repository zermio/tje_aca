package com.jh.pf.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jh.pf.dao.BoardAttachDao;
import com.jh.pf.dao.BoardDao;
import com.jh.pf.dto.Board;
import com.jh.pf.service.BoardService;
import com.jh.pf.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService {
	
	Logger logger = Logger.getLogger(BoardServiceImpl.class);

	@Autowired
	private BoardDao bDao;
	
	@Autowired
	private FileUtil fUtil;
	
	@Autowired
	private BoardAttachDao aDao;
	
	@Override
	public int getTotalCount(HashMap<String, Object> params) {
		int result = bDao.getTotalCount(params);
		return result;
	}

	@Override
	public ArrayList<HashMap<String, Object>> list(HashMap<String, Object> params) {
		ArrayList<HashMap<String, Object>> result = bDao.list(params);
		return result;
	}
	
	@Override
	public void updateHits(int boardSeq, int typeSeq) {
		bDao.updateHits(boardSeq, typeSeq);
	}
	
	@Override
	public Board read(int boardSeq, int typeSeq) {
		Board result = bDao.getBoard(boardSeq, typeSeq);
		
		return result;
	}
	
	@Override
	public List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq) {
		List<HashMap<String, Object>> result = aDao.getFile(boardSeq, typeSeq);
		return result;
	}

	@Override
	public int write(Board b, List<MultipartFile> mf) {
		int resultCnt = bDao.write(b);
		
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
			int resultAttach = aDao.attach(ab);
		}
		return resultCnt;
	}

	@Override
	public int update(Board b, List<MultipartFile> files) {
		if(b.getHasFile().equals("1")) {
			HashMap<String, Object> ab = new HashMap<>();
			for(MultipartFile mp : files) {
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
			aDao.attach(ab);
		}
		int result = bDao.update(b);
		return result;
	}

	@Override
	public int delete(int boardSeq, int typeSeq, String hasFile) {
		int result = 0;
		if(hasFile.equals("1")) {
			List<HashMap<String, Object>> readAttach =  aDao.getFile(boardSeq, typeSeq);
			for(int i=0;i<readAttach.size();i++) {
				aDao.deleteAttach((int)readAttach.get(i).get("file_idx"));
				fUtil.deleteFile(readAttach.get(i));
			}
			/*for(HashMap<String, Object> file : readAttach) {
				fUtil.deleteFile(file);
			}*/
			result = bDao.delete(boardSeq, typeSeq);
			return result;
		}
		result = bDao.delete(boardSeq, typeSeq);
		return result;
	}

	@Override
	public boolean deleteAttach(int fileIdx, int boardSeq, int typeSeq) {
		boolean result = false;
		HashMap<String, Object> fileInfo = aDao.getAttachFile(fileIdx);
		result = (aDao.deleteAttach(fileIdx)==1);
		if(result) {
			result = (fUtil.deleteFile(fileInfo)==result);
		}else {
			return result;
		}
		int hm = aDao.hmFile(boardSeq);
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

	@Override
	public int insertReply(HashMap<String, String> params) {
		int result = bDao.insertReply(params);
		return result;
	}

	@Override
	public ArrayList<HashMap<String, Object>> rList(int boardSeq, int typeSeq) {
		ArrayList<HashMap<String, Object>> result = bDao.rList(boardSeq,typeSeq);
		return result;
	}
	
	@Override
	@Transactional(rollbackFor= {Exception.class})
	public int findUnlinked() {
		ArrayList<HashMap<String, Object>> targets = null;
		try {
			targets = bDao.findUnlinked();
			
			HashMap<String, Object> p = new HashMap<String, Object>();
			p.put("seqs", targets);
			if(targets.size() > 0) {
				bDao.updateHasFileInfo(p);
				bDao.insertBatchResult(targets.size());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return targets==null?0:targets.size();
	}

}
