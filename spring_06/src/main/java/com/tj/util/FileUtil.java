package com.tj.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


public class FileUtil {
	
	Logger logger = Logger.getLogger(FileUtil.class);
	
	//private String saveLocation = "c:/dev/upload";
	
	//@Value("${file.save.location}")
	@Value("#{config['file.save.location']}")
	private String saveLocation;
	
	public void copyFile(MultipartFile mf, String fakename) throws IOException {
		logger.debug("saveLocation bf ---- "+saveLocation);
		//폴더 지정
		File ddir = new File(this.saveLocation);
		logger.debug("saveLocation af ---- "+saveLocation);
		//디렉토리 확인 후 없으면 생성
		if(!ddir.exists()) {
			ddir.mkdirs();
		}
		logger.debug("ddir af ---- "+ddir);
		//파일 지정
		File dfile = new File(ddir, fakename);
		FileCopyUtils.copy(mf.getBytes(), dfile);
	}
	
	public byte[] readFile(HashMap<String, Object> fileInfo) {
		//파일찾기
		logger.debug("util find fakename ---- "+String.valueOf(fileInfo.get("fakename")));
		File f = new File(saveLocation, String.valueOf(fileInfo.get("fakename")));
		
		logger.debug("util exist f? ---- "+f.exists());
		logger.debug("util what f? ---- "+f);
		byte[] b = null;
		
		if(f.exists()) {
			try {
				b = FileUtils.readFileToByteArray(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			
		}
		logger.debug("util what b? ---- "+b);
		return b;
	}
	
	public boolean deleteFile(HashMap<String, Object> fileInfo) {
		File f = new File(saveLocation, String.valueOf(fileInfo.get("fakename")));
		if(f.exists()) { //물리적
			return f.delete();
		}
		return false;
	}
}
