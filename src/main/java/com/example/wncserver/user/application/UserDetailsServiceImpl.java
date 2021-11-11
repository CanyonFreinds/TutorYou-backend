package com.example.wncserver.user.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.user.domain.User;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.user.domain.UserRepository;
import com.example.wncserver.user.domain.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		User user = userRepository.findByEmail(username)
			.orElseThrow(UserNotFoundException::new);
		return UserPrincipal.create(user);
	}
}