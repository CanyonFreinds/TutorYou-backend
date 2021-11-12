package com.example.wncserver.group.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wncserver.user.domain.User;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
	Optional<StudentGroup> findByGroupAndStudent(Group group, User student);
	List<StudentGroup> findAllByGroup(Group group);
	List<StudentGroup> findAllByStudent(User student);
}
