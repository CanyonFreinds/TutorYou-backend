package com.example.wncserver.career.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.career.domain.Career;
import com.example.wncserver.career.domain.CareerRepository;
import com.example.wncserver.career.domain.CareerType;
import com.example.wncserver.career.presentation.dto.CareerRequest;
import com.example.wncserver.career.presentation.dto.CareerResponse;
import com.example.wncserver.exception.custom.CareerNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareerService {
	private final UserRepository userRepository;
	private final CareerRepository careerRepository;

	@Transactional
	public CareerResponse changeUserCareer(Long careerId, CareerRequest updateRequest) {
		Career career = careerRepository.findById(careerId).orElseThrow(CareerNotFoundException::new);
		final String content = updateRequest.getContent();
		final String careerType = updateRequest.getCareerType();
		career.updateCareer(content, CareerType.valueOf(careerType));
		return CareerResponse.from(career);
	}

	@Transactional
	public CareerResponse addUserCareer(Long userId, CareerRequest createRequest) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		final String content = createRequest.getContent();
		final CareerType careerType = CareerType.valueOf(createRequest.getCareerType());
		Career career = Career.createCareer(user, content, careerType);
		return CareerResponse.from(careerRepository.save(career));
	}

	@Transactional
	public void deleteCareer(Long careerId) {
		Career career = careerRepository.findById(careerId).orElseThrow(CareerNotFoundException::new);
		careerRepository.delete(career);
	}
}
