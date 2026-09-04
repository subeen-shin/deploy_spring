package kr.fast.community.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtProvider jwtProvider;

 
    /***
     * 요청에 있는 토큰 정보를 가져와서 있으면 로그인 처리
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
    
        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsername(token);
            String role = jwtProvider.get(token, "role");
           // String nickname = jwtProvider.get(token, "nickname");
           // String email = jwtProvider.get(token, "email");
            CustomUserDetails userDetails =
            		new CustomUserDetails(
            				username, "테스트", 
            				"test@naver.com", 
            				List.of(new SimpleGrantedAuthority(role)));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            		userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}