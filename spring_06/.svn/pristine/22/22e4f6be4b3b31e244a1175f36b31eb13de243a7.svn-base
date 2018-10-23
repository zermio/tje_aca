package com.tj.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tj.dao.MemberDao;
import com.tj.dto.Member;
import com.tj.exception.MemberNotFoundException;
import com.tj.exception.PasswordMissmatchException;
import com.tj.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDao mDao;

	@Override
	public int join(Member m) {
		int resultCnt = mDao.join(m);
		return resultCnt;
	}

	@Override
	public Member login(String ui, String up) throws Exception {
		Member result = mDao.findMember(ui);
		if(result!=null) {
			String cpw = mDao.makeCipherText(up);
			String inpw = result.getMemberPw();
			if(inpw.equals(cpw)) {
				return result;
			}else{
				throw new PasswordMissmatchException();
			}
		}else {
			throw new MemberNotFoundException();
		}
	}

	@Override
	public int checkId(HashMap<String, String> params) {
		int result = mDao.checkId(params);
		return result;
	}
}
