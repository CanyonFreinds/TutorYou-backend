package com.example.wncserver.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.exception.custom.InvalidPasswordException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.user.domain.Role;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.presentation.dto.SignupRequest;
import com.example.wncserver.user.domain.UserRepository;
import com.example.wncserver.user.presentation.dto.UserNameUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserPasswordUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Transactional
	public User signUp(final SignupRequest request) {
		final String email = request.getEmail();
		final String password = encoder.encode(request.getPassword());
		final String name = request.getName();
		final String authority = request.getRole();
		final User user = User.createUser(email, password, name, Role.valueOf(authority));
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public boolean isDuplicateEmail(final String email) {
		return userRepository.existsUserByEmail(email);
	}

	@Transactional(readOnly = true)
	public UserResponse getUserInformation(final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		return UserResponse.from(user);
	}

	@Transactional
	public boolean changeUserPassword(final UserPasswordUpdateRequest updateRequest, final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		if (!encoder.matches(updateRequest.getBeforePassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}
		user.setPassword(encoder.encode(updateRequest.getAfterPassword()));
		return true;
	}

	@Transactional
	public boolean changeUserName(final UserNameUpdateRequest updateRequest, final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		user.setName(updateRequest.getName());
		return true;
	}

	@Transactional
	public void deleteUser(final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
	}
}
