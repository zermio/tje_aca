package com.jh.pf.dao;

import java.util.HashMap;
import java.util.List;

import com.jh.pf.dto.Board;

public interface NoticeDao {

	int getTotalCount(HashMap<String, Object> params);

	List<HashMap<String, Object>> list(HashMap<String, Object> params);

	void updateHits(int boardSeq, int typeSeq);

	Board getBoard(int boardSeq, int typeSeq);

	int write(Board b);

	int update(Board b);

	int delete(int boardSeq, int typeSeq);

	int changeHasFile(int boardSeq);
}
