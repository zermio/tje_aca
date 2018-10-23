package com.tj.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tj.dto.Board;

public interface BoardService {

	public int write(Board b, List<MultipartFile> mf);

	public List<HashMap<String, Object>> list(HashMap<String, String> params);
	
	public int getTotalCount(HashMap<String,String> params);
	
	public Board read(int typeSeq, int boardSeq);
	
	public int delete(int typeSeq, int boardSeq, String hasFile);

	public int update(Board b, List<MultipartFile> mf);

	public List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq);

	public int changeHasFile(int boardSeq);
	
	public boolean deleteAttach(int fileIdx, int boardSeq, int typeSeq);
	
	
	
}
