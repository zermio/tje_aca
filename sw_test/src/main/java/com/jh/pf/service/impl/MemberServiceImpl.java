package com.jh.pf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jh.pf.dao.MemberDao;
import com.jh.pf.dto.Member;
import com.jh.pf.exception.MemberNotFoundException;
import com.jh.pf.exception.PasswordMissmatchException;
import com.jh.pf.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDao mDao;
	
	@Override
	public int checkId(HashMap<String, String> params) {
		int result = mDao.checkId(params);
		return result;
	}

	@Override
	public int join(Member m) {
		int result = mDao.join(m);
		return result;
	}

	@Override
	public Member login(String ui, String up) throws Exception {
		Member result = mDao.findMember(ui);
		if(result!=null) {
			String cpw = mDao.makeCipher(up);
			String pw = result.getMemberPw();
			if(cpw.equals(pw)) {
				return result;
			}else {
				throw new PasswordMissmatchException();
			}
		}else {
			throw new MemberNotFoundException();
		}
	}

	@Override
	public ArrayList<HashMap<String, String>> mList(HashMap<String, String> params) {
		ArrayList<HashMap<String, String>> result = mDao.mList(params);
		return result;
	}

	@Override
	public int cMember(HashMap<String, String> params) {
		int result = mDao.cMember(params);
		return result;
	}

	@Override
	public int delMember(HashMap<String, String> params) {
		int result = mDao.delMember(params);
		return result;
	}
}
