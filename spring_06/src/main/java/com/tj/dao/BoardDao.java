package com.tj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tj.dto.Board;

public interface BoardDao {

	public int write(Board b);
	
	public int attach(HashMap<String,Object> ab);

	public List<HashMap<String, Object>> list(HashMap<String, String> params);
	
	public int getTotalCount(HashMap<String,String> params);
	
	public int updateHits(int typeSeq, int boardSeq);
	
	public Board getBoard(int typeSeq, int boardSeq);
	
	public int delete(int typeSeq, int boardSeq);

	public int update(Board b);

	public List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq);

	public HashMap<String, Object> getAttachFile(int fileIdx);

	public int deleteAttach(int fileIdx);

	public int hmFile(int boardSeq);

	public int changeHasFile(int boardSeq);
}
