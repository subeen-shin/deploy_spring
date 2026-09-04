package kr.fast.community.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.fast.community.dto.LoginRequest;
import kr.fast.community.dto.MessageResponse;
import kr.fast.community.dto.SignupRequest;
import kr.fast.community.entity.Member;
import kr.fast.community.repository.MemberRepository;
import kr.fast.community.security.JwtProvider;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final MemberRepository memberRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final JwtProvider jwtProvider;

	@Transactional
	public MessageResponse signup(SignupRequest request) {
		//아이디, 비번, 이메일 체크
		if(!request.validId()) {
			throw new IllegalArgumentException("아이디는 영어 또는 숫자로 이루어져 있으며 3자 이상입니다.");
		}
		if(!request.validPw()) {
			throw new IllegalArgumentException("비번은 3글자 이상입니다.");
		}
		if(!request.validEmail()) {
			throw new IllegalArgumentException("이메일은 필수 항목입니다.");
		}
		//아이디 중복 검사
		boolean existsById = memberRepository.existsById(request.id());
		if(existsById) {
			throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
		}
		//이메일 중복 검사
		boolean existsByEmail = memberRepository.existsByEmail(request.email());
		if(existsByEmail) {
			throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
		}
		//비번 암호화
		String encodedPw = passwordEncoder.encode(request.pw());
		//회원 가입(member 엔티티 객체 필요)
		Member member = new Member(request.id(), encodedPw, request.email());
		memberRepository.save(member);
		//성공 여부를 결과로 전달
		return new MessageResponse(true, "회원가입을 완료했습니다.");
	}

	public String login(LoginRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("서버 이상입니다.");
		}
		//회원 정보를 가져옴. 없으면 예외 발생
		Member user = memberRepository.findById(request.id())
				.orElseThrow(
					()-> new IllegalArgumentException("아이디나 비번이 일치하지 않습니다."));
		
		//비번 확인해서 일치하지 않으면 예외발생 
		if(!passwordEncoder.matches(request.pw(), user.getPw())) {
			throw new IllegalArgumentException("아이디나 비번이 일치하지 않습니다.");
		}
		//사원증(토큰) 발급
		String accessToken = jwtProvider.createToken(user.getId(), user.getRole() /*,user.getNickname(), user.getEmail()*/);
		
		//사원증 리턴
		return accessToken;
	}
	
	
	
}
