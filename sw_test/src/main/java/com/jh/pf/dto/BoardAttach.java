package com.jh.pf.dto;

import org.apache.ibatis.type.Alias;

@Alias("BoardAttach")
public class BoardAttach {

	private int fileIdx;
	private int boardSeq;
	private int boardType;
	private String filename;
	private String fakename;
	private int fileSize;
	private String fileType;
	private String saveLoc;
	private String createDate;
	
	public int getFileIdx() {
		return fileIdx;
	}
	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}
	public int getBoardSeq() {
		return boardSeq;
	}
	public void setBoardSeq(int boardSeq) {
		this.boardSeq = boardSeq;
	}
	public int getBoardType() {
		return boardType;
	}
	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFakename() {
		return fakename;
	}
	public void setFakename(String fakename) {
		this.fakename = fakename;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getSaveLoc() {
		return saveLoc;
	}
	public void setSaveLoc(String saveLoc) {
		this.saveLoc = saveLoc;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
