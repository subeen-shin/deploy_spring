package kr.fast.community.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@ToString
public class CustomUserDetails implements UserDetails {
	
	
	
	private final String username;
	private final String nickname;
	private final String email;
	
	private final Collection<? extends GrantedAuthority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		
		return null;
	}

	@Override
	public String getUsername() {
		
		return username;
	}

	
}
