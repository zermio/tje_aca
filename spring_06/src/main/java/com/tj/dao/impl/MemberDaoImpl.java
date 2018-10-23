package com.tj.dao.impl;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tj.dao.MemberDao;
import com.tj.dto.Member;

public class MemberDaoImpl implements MemberDao{
	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Override
	public int join(Member m) {
		String sql = "insert into member (type_seq, member_id, member_pw, member_name, member_nick, email, birth_date, create_date) values(1,?,sha2(md5(?),512),?,?,?,?,now())";
		int resultCnt = jdbcTemplate.update(sql, new Object[] {m.getMemberId(),m.getMemberPw(),m.getMemberName(),m.getMemberNick(),m.getEmail(),m.getBirthDate()});
		
		return resultCnt;
	}

	@Override
	public Member findMember(String ui) {
		String sql = "select * from member where member_id=?";
		try {
			Member m = (Member) jdbcTemplate.queryForObject(sql, new Object[] {ui}, new BeanPropertyRowMapper(Member.class));
			return m;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String makeCipherText(String up) {
		String sql = "select sha2(md5(?),512)";
		String cpw = jdbcTemplate.queryForObject(sql, new Object[] {up}, String.class);
		return cpw;
	}

	@Override
	public int checkId(HashMap<String, String> params) {
		String sql = "select count(member_idx) from member where member_id=?";
		int result = jdbcTemplate.queryForObject(sql, new Object[] {params.get("ui")}, Integer.class);
		return result;
	}
	
}
