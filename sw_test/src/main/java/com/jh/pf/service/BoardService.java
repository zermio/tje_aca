package com.jh.pf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jh.pf.dto.Board;

public interface BoardService {

	int getTotalCount(HashMap<String, Object> params);

	ArrayList<HashMap<String, Object>> list(HashMap<String, Object> params);
	
	void updateHits(int boardSeq, int typeSeq);

	Board read(int boardSeq, int typeSeq);

	List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq);

	int write(Board b, List<MultipartFile> mf);

	int update(Board b, List<MultipartFile> files);

	int delete(int boardSeq, int typeSeq, String hasFile);

	boolean deleteAttach(int fileIdx, int boardSeq, int typeSeq);

	int insertReply(HashMap<String, String> params);

	ArrayList<HashMap<String, Object>> rList(int boardSeq, int typeSeq);

	int findUnlinked();

}
