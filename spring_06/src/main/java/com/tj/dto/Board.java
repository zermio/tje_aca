package com.tj.dto;

import org.apache.ibatis.type.Alias;

@Alias("Board")
public class Board {

	private String typeSeq;
	private String boardSeq;
	private String memberId;
	private String memberNick;
	private String title;
	private String content;
	private String hasFile;
	private int hits;
	private String createDate;
	private String updateDate;
	
	@Override
	public String toString() {
		return "Board [typeSeq=" + typeSeq + ", boardSeq=" + boardSeq + ", memberId=" + memberId + ", memberNick="
				+ memberNick + ", title=" + title + ", content=" + content + ", hits=" + hits + ", createDate="
				+ createDate + ", updateDate=" + updateDate + "]";
	}
	public String getTypeSeq() {
		return typeSeq;
	}
	public void setTypeSeq(String typeSeq) {
		this.typeSeq = typeSeq;
	}
	public String getBoardSeq() {
		return boardSeq;
	}
	public void setBoardSeq(String boardSeq) {
		this.boardSeq = boardSeq;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberNick() {
		return memberNick;
	}
	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getHasFile() {
		return hasFile;
	}
	public void setHasFile(String hasFile) {
		this.hasFile = hasFile;
	}
	
	
}
