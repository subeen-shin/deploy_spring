package kr.fast.community.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	/**
	 * uploadFilePath에 파일(file)을 저장하고 저장된 경로 및 새로 변경된 파일명을 반환
	 * @param uploadFilePath 저장할 폴더 경로
	 * @param file 저장할 파일
	 * @return 서버에 저장된 폴더 경로와 파일 명
	 */
	public static String saveFile(String uploadFilePath, MultipartFile file){
		if(file == null || file.getOriginalFilename().isEmpty()) {
			throw new RuntimeException("첨부파일이 없습니다.");
		}
		try {
			String oriFileName = file.getOriginalFilename();
			String savedFileName = UUID.randomUUID().toString()+"_" + oriFileName;
			
			File dest = new File(uploadFilePath + savedFileName);
			file.transferTo(dest);
			return savedFileName;
		}catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("저장 중 예외가 발생했습니다.");
		}
		
	}
}
