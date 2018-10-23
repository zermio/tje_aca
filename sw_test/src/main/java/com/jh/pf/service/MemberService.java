package com.jh.pf.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.jh.pf.dto.Member;


@Service
public interface MemberService {

	int checkId(HashMap<String, String> params);

	int join(Member m);

	Member login(String ui, String up) throws Exception;
	
	ArrayList<HashMap<String, String>> mList(HashMap<String, String> params);

	int cMember(HashMap<String, String> params);

	int delMember(HashMap<String, String> params);

}
