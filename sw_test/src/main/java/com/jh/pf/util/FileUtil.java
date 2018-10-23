package com.jh.pf.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	@Value("#{config['file.save.location']}")
	private String saveLocation;

	public byte[] readFile(HashMap<String, Object> fileInfo) {
		File f = new File(saveLocation, String.valueOf(fileInfo.get("fakename")));
		
		byte[] b = null;
		
		if(f.exists()) {
			try {
				b = FileUtils.readFileToByteArray(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public void copyFile(MultipartFile mp, String fakename) throws IOException {
		File ddir = new File(this.saveLocation);
		if(!ddir.exists()) {
			ddir.mkdirs();
		}
		File dfile = new File(ddir, fakename);
		FileCopyUtils.copy(mp.getBytes(), dfile);
	}

	public boolean deleteFile(HashMap<String, Object> hashMap) {
		File f = new File(saveLocation, String.valueOf(hashMap.get("fakename")));
		if(f.exists()) { //물리적
			return f.delete();
		}
		return false;
	}
}
