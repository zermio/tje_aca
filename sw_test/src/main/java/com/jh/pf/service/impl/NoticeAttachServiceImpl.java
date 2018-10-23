package com.jh.pf.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jh.pf.dao.NoticeAttachDao;
import com.jh.pf.service.NoticeAttachService;

@Service
public class NoticeAttachServiceImpl implements NoticeAttachService{

	@Autowired
	private NoticeAttachDao aDao;
	
	@Override
	public HashMap<String, Object> getAttachFile(int fileIdx) {
		HashMap<String, Object> result = aDao.getAttachFile(fileIdx);
		return result;
	}

}
