package com.example.wncserver.user.presentation;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.wncserver.exception.custom.EmailDuplicateException;
import com.example.wncserver.exception.custom.InvalidPasswordException;
import com.example.wncserver.exception.custom.RefreshTokenException;
import com.example.wncserver.support.dto.JwtTokenResponse;
import com.example.wncserver.support.util.JwtUtil;
import com.example.wncserver.support.util.RedisUtil;
import com.example.wncserver.user.application.UserDetailsServiceImpl;
import com.example.wncserver.user.application.UserService;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserPrincipal;
import com.example.wncserver.user.presentation.dto.LoginRequest;
import com.example.wncserver.user.presentation.dto.LoginResponse;
import com.example.wncserver.user.presentation.dto.RefreshOrLogoutRequest;
import com.example.wncserver.user.presentation.dto.SignupRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	@PostMapping("/signup")
	public ResponseEntity<Long> signUp(@RequestBody SignupRequest signUpRequest) {
		if (userService.isDuplicateEmail(signUpRequest.getEmail())) {
			throw new EmailDuplicateException();
		}
		User user = userService.signUp(signUpRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/")
			.buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(location).body(user.getId());
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new InvalidPasswordException();
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Long userId = userPrincipal.getId();
		String email = userPrincipal.getEmail();
		String name = userPrincipal.getName();
		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);
		String uuid = UUID.randomUUID().toString();
		List<String> role = userPrincipal.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority).collect(Collectors.toList());

		redisUtil.setDataExpire(uuid, refreshToken, (int)JwtUtil.REFRESH_EXPIRATION_SECONDS);

		LoginResponse jwtResponse = new LoginResponse(accessToken, uuid, userId, email, name, role);
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtTokenResponse> tokenRefresh(@RequestBody RefreshOrLogoutRequest request) {
		String email = request.getEmail();
		String uuid = request.getUuid();
		String oldAccessToken = request.getAccessToken();
		Optional<String> refreshToken = redisUtil.getData(uuid);

		if (refreshToken.isEmpty()) {
			throw new RefreshTokenException();
		}
		if (!jwtUtil.validateJwtToken(refreshToken.get()) ||
			!email.equals(jwtUtil.getUserNameFromJwtToken(refreshToken.get()))) {
			throw new RefreshTokenException();
		}
		UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(email);
		String newAccessToken = jwtUtil.generateJwtToken(userPrincipal);
		redisUtil.setDataExpire(oldAccessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);

		return ResponseEntity.ok(new JwtTokenResponse(userPrincipal.getId(), newAccessToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody RefreshOrLogoutRequest request) {
		String uuid = request.getUuid();
		String accessToken = request.getAccessToken();
		if (redisUtil.getData(uuid).isPresent()) {
			redisUtil.deleteData(uuid);
		}
		redisUtil.setDataExpire(accessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);
		return ResponseEntity.noContent().build();
	}
}
