package com.example.wncserver.support.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.wncserver.exception.custom.AccessTokenException;
import com.example.wncserver.support.util.JwtUtil;
import com.example.wncserver.support.util.RedisUtil;
import com.example.wncserver.user.application.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
	private final RedisUtil redisUtil;
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws RuntimeException {
		log.debug("JwtInterceptor.preHandle");
		String jwt = parseJwt(request);
		if (jwt != null) {
			if (jwtUtil.validateJwtToken(jwt)) {
				Optional<String> isLockedAccessToken = redisUtil.getData(jwt);
				if (isLockedAccessToken.isEmpty()) {
					String username = jwtUtil.getUserNameFromJwtToken(jwt);

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					throw new AccessTokenException(CustomStatus.IS_LOCKED_TOKEN.toString());
				}
			} else {
				throw new AccessTokenException(CustomStatus.INVALID_TOKEN.toString());
			}
		}
		return true;
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}