package kr.fast.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.fast.community.dto.LoginRequest;
import kr.fast.community.dto.MessageResponse;
import kr.fast.community.dto.SignupRequest;
import kr.fast.community.security.CustomUserDetails;
import kr.fast.community.service.AuthService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {

		try {
			MessageResponse messageResponse = authService.signup(signupRequest);
			return ResponseEntity.ok(messageResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(false, e.getMessage()));

		}

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 서비스야 사원증 발급해줘. 이이디 비번줄게
			String accessToken = authService.login(request);
			map.put("accessToken", accessToken);
			map.put("state", new MessageResponse(true, "로그인을 했습니다"));

		} catch (Exception e) {
			e.printStackTrace();
			map.put("state", new MessageResponse(false, e.getMessage()));
		}
		return ResponseEntity.ok(map);
	}


	@GetMapping("/me")
	public ResponseEntity<Object> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(userDetails != null) {
			map.put("username", userDetails.getUsername());
			map.put("email", userDetails.getEmail());
			map.put("nickname", userDetails.getNickname());
			
			List<String> list = new ArrayList<String>();
			for(GrantedAuthority tmp : userDetails.getAuthorities()) {
				list.add(tmp.getAuthority());
			}
			map.put("role", list);
		}
		return ResponseEntity.ok(map);

	}
}
