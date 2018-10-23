package com.tj.dao;


import java.util.HashMap;

import com.tj.dto.Member;

public interface MemberDao {
	/**
	 * @param HashMap<String, Object> memberInfo
	 * @return jdbcTemplate.update();
	 */
	public int join(Member m);
	/**
	 * @param memberId
	 * @return MemberDto mDto || null
	 */
	public Member findMember(String memberId);
	/**
	 * @param memberPw
	 * @return String jdbcTemplate.queryForObject()
	 */
	public String makeCipherText(String memberPw);
	/**
	 * 
	 * @param params
	 * @return
	 */
	public int checkId(HashMap<String, String> params);
}
