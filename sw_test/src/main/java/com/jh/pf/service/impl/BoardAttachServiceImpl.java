package com.jh.pf.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jh.pf.dao.BoardAttachDao;
import com.jh.pf.service.BoardAttachService;

@Service
public class BoardAttachServiceImpl implements BoardAttachService{
	
	@Autowired
	private BoardAttachDao aDao;

	@Override
	public HashMap<String, Object> getAttachFile(int fileIdx) {
		HashMap<String, Object> result = aDao.getAttachFile(fileIdx);
		return result;
	}

}
