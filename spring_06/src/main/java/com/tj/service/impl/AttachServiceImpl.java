package com.tj.service.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tj.controller.BoardController;
import com.tj.dao.BoardDao;
import com.tj.service.AttachService;

@Service
public class AttachServiceImpl implements AttachService{

	private Logger logger = Logger.getLogger(AttachServiceImpl.class);
	
	@Autowired
	private BoardDao bDao;

	@Override
	public HashMap<String, Object> getAttachFile(int fileIdx) {
		logger.debug("ser rec fileIdx ---- "+fileIdx);
		
		HashMap<String, Object> result = bDao.getAttachFile(fileIdx);
		logger.debug("ser rec result ---- "+result);
		
		return result;
	}

	@Override
	public int delete(int fileIdx) {
		int result = bDao.deleteAttach(fileIdx);
		return result;
	}

	@Override
	public int hmFile(int boardSeq) {
		int result = bDao.hmFile(boardSeq);
		return result;
	}

	
}
