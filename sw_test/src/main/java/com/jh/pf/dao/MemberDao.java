package com.jh.pf.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.jh.pf.dto.Member;

public interface MemberDao {

	int checkId(HashMap<String, String> params);

	int join(Member m);

	Member findMember(String ui);

	String makeCipher(String up);
	
	ArrayList<HashMap<String, String>> mList(HashMap<String, String> params);

	int cMember(HashMap<String, String> params);

	int delMember(HashMap<String, String> params);

}
