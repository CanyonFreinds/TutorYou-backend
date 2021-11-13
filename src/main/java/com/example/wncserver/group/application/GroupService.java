package com.example.wncserver.group.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.exception.custom.GroupNotFoundException;
import com.example.wncserver.exception.custom.StudentGroupNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.group.domain.Group;
import com.example.wncserver.group.domain.GroupRepository;
import com.example.wncserver.group.domain.StudentGroup;
import com.example.wncserver.group.domain.StudentGroupRepository;
import com.example.wncserver.group.presentation.dto.GroupResponse;
import com.example.wncserver.group.presentation.dto.StudentGroupResponse;
import com.example.wncserver.post.domain.Post;
import com.example.wncserver.user.domain.Role;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final StudentGroupRepository studentGroupRepository;

	@Transactional
	public StudentGroupResponse joinGroupForStudent(final Long groupId, final Long userId) {
		final User student = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		final Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		final StudentGroup studentGroup = StudentGroup.createStudentGroup(student, group);
		studentGroupRepository.save(studentGroup);
		addApplicantCountAndAlarm(group.getPost());
		return StudentGroupResponse.from(studentGroup);
	}

	private void addApplicantCountAndAlarm(final Post post) {
		final int nowApplicantCount = post.getApplicantCount();
		final int limit = post.getTotalStudentCount();
		if (nowApplicantCount < limit) {
			final int addCount = nowApplicantCount + 1;
			post.setApplicantCount(addCount);
			// TODO
			if (addCount == limit) {
				User user = post.getAuthor();
				user.setStudentCount(addCount);
			}
		}
	}

	@Transactional
	public void leaveGroupForStudent(final Long groupId, final Long userId) {
		final User student = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		final Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		final StudentGroup studentGroup = studentGroupRepository.findByGroupAndStudent(group, student).orElseThrow(
			StudentGroupNotFoundException::new);
		studentGroupRepository.delete(studentGroup);
	}

	@Transactional
	public void deleteGroupForTeacher(final Long groupId) {
		final Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
		groupRepository.delete(group);
	}

	@Transactional(readOnly = true)
	public List<GroupResponse> getGroups(Long userId) {
		final User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		List<GroupResponse> result = new ArrayList<>();
		if (Role.ROLE_TEACHER.equals(user.getRole())) {
			final List<Group> groups = user.getGroups();
			result = groupsToGroupResponses(groups);

		} else if (Role.ROLE_STUDENT.equals(user.getRole())) {
			final List<Group> groups = studentGroupRepository.findAllByStudent(user)
				.stream()
				.map(StudentGroup::getGroup)
				.collect(Collectors.toList());
			result = groupsToGroupResponses(groups);
		}
		return result;
	}

	private List<GroupResponse> groupsToGroupResponses(List<Group> groups) {
		List<GroupResponse> result = new ArrayList<>();
		for (Group group : groups) {
			List<String> studentNames = studentGroupRepository.findAllByGroup(group)
				.stream()
				.map(sg -> sg.getStudent().getName())
				.collect(Collectors.toList());

			result.add(GroupResponse.from(group, studentNames));
		}
		return result;
	}
}
