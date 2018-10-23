package com.jh.pf.dao;

import java.util.HashMap;
import java.util.List;

public interface NoticeAttachDao {

	List<HashMap<String, Object>> getFile(int boardSeq, int typeSeq);

	int attach(HashMap<String, Object> ab);

	int deleteAttach(int i);

	HashMap<String, Object> getAttachFile(int fileIdx);

	int hmFile(int boardSeq);

}
