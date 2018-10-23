package com.tj.service;

import java.util.HashMap;

public interface AttachService {

	public HashMap<String, Object> getAttachFile(int fileIdx);

	public int delete(int fileIdx);

	public int hmFile(int boardSeq);
}
