package kr.fast.community.dto;

import java.util.regex.Pattern;

public record SignupRequest(String id, String pw, String email) {
	//아이디는 영어,숫자로 구성되어 있으며 3글자 이상
	public boolean validId() {
		String regex = "^\\w{3,}$";
		return Pattern.matches(regex, this.id);
	}
	
	//비번은 3글자 이상
	public boolean validPw() {
		return this.pw != null && this.pw.length() >=3;
	}
	
	//이메일은 필수. 1글자 이상
	public boolean validEmail() {
		return this.email!= null && this.email.length() > 0;	
		}
}
