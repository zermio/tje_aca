package com.jh.pf.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jh.pf.dao.NoticeAttachDao;
import com.jh.pf.dao.NoticeDao;
import com.jh.pf.dto.Board;
import com.jh.pf.service.NoticeService;
import com.jh.pf.util.FileUtil;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	private NoticeDao nDao;
	
	@Autowired
	private FileUtil fUtil;
	
	@Autowired
	private NoticeAttachDao aDao;

	@Override
	public int getTotalCount(HashMap<String, Object> params) {
		int result = nDao.getTotalCount(params);
		return result;
	}

	@Override
	public List<HashMap<String, Object>> list(HashMap<String, Object> params) {
		List<HashMap<String, Object>> result = nDao.list(params);
		return result;
	}

	@Override
	public Board read(int boardSeq, int typeSeq) {
		nDao.updateHits(boardSeq, typeSeq);
		Board result = nDao.getBoard(boardSeq, typeSeq);
		return result;
	}

	@Override
	public List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq) {
		List<HashMap<String, Object>> result = aDao.getFile(boardSeq, typeSeq);
		return result;
	}

	@Override
	public int write(Board b, List<MultipartFile> mf) {
		int resultCnt = nDao.write(b);
		
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
		int result = nDao.update(b);
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
			result = nDao.delete(boardSeq, typeSeq);
			return result;
		}
		result = nDao.delete(boardSeq, typeSeq);
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
			int chf = nDao.changeHasFile(boardSeq);
			result = ((chf==1)&&result);
		}
		/*List<Map<String, Object>> files = getFile(boardSeq, typeSeq);
		if(files==null||files.size()==0) {
			int chf = nDao.changeHasFile(boardSeq);
		}*/
		return result;
	}
}
