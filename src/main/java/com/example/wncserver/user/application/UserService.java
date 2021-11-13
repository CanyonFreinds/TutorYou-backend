package com.example.wncserver.user.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.wncserver.career.domain.Career;
import com.example.wncserver.career.domain.CareerRepository;
import com.example.wncserver.career.domain.CareerType;
import com.example.wncserver.career.presentation.dto.CareerRequest;
import com.example.wncserver.exception.custom.EvaluationBadRequestException;
import com.example.wncserver.exception.custom.InvalidPasswordException;
import com.example.wncserver.exception.custom.ReportBadRequestException;
import com.example.wncserver.exception.custom.StudentGroupNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.group.domain.StudentGroup;
import com.example.wncserver.group.domain.StudentGroupRepository;
import com.example.wncserver.support.util.S3UploadUtil;
import com.example.wncserver.user.domain.Role;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserQueryRepository;
import com.example.wncserver.user.presentation.dto.SignupRequest;
import com.example.wncserver.user.domain.UserRepository;
import com.example.wncserver.user.presentation.dto.UserNameUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserPasswordUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserResponse;
import com.example.wncserver.user.presentation.dto.admin.AdminResponse;
import com.example.wncserver.user.presentation.dto.admin.AdminTeacherResponse;
import com.example.wncserver.user.presentation.dto.teacher.TeacherPageResponse;
import com.example.wncserver.user.presentation.dto.teacher.TeacherPointUpdateRequest;
import com.example.wncserver.user.presentation.dto.teacher.TeacherReportRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final CareerRepository careerRepository;
	private final PasswordEncoder encoder;
	private final S3UploadUtil s3UploadUtil;
	private final StudentGroupRepository studentGroupRepository;
	private final UserQueryRepository userQueryRepository;

	@Transactional
	public User signup(final SignupRequest request) {
		final String email = request.getEmail();
		final String password = encoder.encode(request.getPassword());
		final String name = request.getName();
		final String role = request.getRole();
		final User user = User.createUser(email, password, name, Role.valueOf(role));
		if (Role.valueOf(role).equals(Role.ROLE_TEACHER)) {
			createCareerInSignup(request.getCareers(), user);
		}
		return userRepository.save(user);
	}

	private void createCareerInSignup(final List<CareerRequest> requests, final User user) {
		for (CareerRequest createRequest : requests) {
			final String content = createRequest.getContent();
			final CareerType careerType = CareerType.valueOf(createRequest.getCareerType());
			Career career = Career.createCareer(user, content, careerType);
			careerRepository.save(career);
		}
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
	public String changeUserImageUrl(final MultipartFile file, final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		String imageUrl = s3UploadUtil.uploadProfileImage(file, userId);
		user.setImageUrl(imageUrl);
		return imageUrl;
	}

	@Transactional
	public void deleteUser(final Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
	}

	@Transactional
	public boolean changeTeacherPoint(final TeacherPointUpdateRequest updateRequest, final Long userId) {
		final Long studentId = updateRequest.getStudentId();
		final Long groupId = updateRequest.getGroupId();
		final StudentGroup studentGroup = studentGroupRepository.findByGroup_IdAndStudent_Id(groupId, studentId)
			.orElseThrow(StudentGroupNotFoundException::new);
		if (studentGroup.isEvaluate()) {
			throw new EvaluationBadRequestException();
		}
		User teacher = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		final double point = teacher.getPoint();
		final int voterCount = teacher.getVoterCount();
		teacher.setPoint(((point * voterCount) + updateRequest.getPoint()) / (voterCount + 1));
		teacher.setVoterCount(voterCount + 1);
		studentGroup.setEvaluate(true);
		return true;
	}

	@Transactional
	public boolean reportTeacher(final TeacherReportRequest request, final Long userId) {
		final Long studentId = request.getStudentId();
		final Long groupId = request.getGroupId();
		final StudentGroup studentGroup = studentGroupRepository.findByGroup_IdAndStudent_Id(groupId, studentId)
			.orElseThrow(StudentGroupNotFoundException::new);
		if (studentGroup.isReport()) {
			throw new ReportBadRequestException();
		}
		User teacher = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		teacher.setBanCount(teacher.getBanCount() + 1);
		studentGroup.setReport(true);
		return true;
	}

	@Transactional(readOnly = true)
	public TeacherPageResponse getTeachers(final String query, final String sort, final String order,
		final Pageable pageable) {
		return userQueryRepository.findAllByQuery(query, sort, order, pageable);
	}

	@Transactional
	public AdminTeacherResponse manageBanTeacher(final Long teacherId) {
		User teacher = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
		teacher.setBaned(!teacher.isBaned());
		return AdminTeacherResponse.from(teacher);
	}

	@Transactional(readOnly = true)
	public AdminResponse getTeacherListByIsBaned(final boolean isBaned, final Pageable pageable) {
		return userQueryRepository.findAllByIsBaned(isBaned, pageable);
	}
}
