package com.jh.pf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jh.pf.dto.Board;

public interface BoardDao {

	int getTotalCount(HashMap<String, Object> params);

	ArrayList<HashMap<String, Object>> list(HashMap<String, Object> params);

	void updateHits(int boardSeq, int typeSeq);

	Board getBoard(int boardSeq, int typeSeq);

	int write(Board b);

	int update(Board b);

	int delete(int boardSeq, int typeSeq);

	int changeHasFile(int boardSeq);

	int insertReply(HashMap<String, String> params);

	ArrayList<HashMap<String, Object>> rList(int boardSeq, int typeSeq);

	ArrayList<HashMap<String, Object>> findUnlinked();

	void updateHasFileInfo(HashMap<String, Object> p);

	void insertBatchResult(int size);

}
