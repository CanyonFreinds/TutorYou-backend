package com.example.wncserver.group.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.group.application.GroupService;
import com.example.wncserver.group.presentation.dto.GroupResponse;
import com.example.wncserver.group.presentation.dto.StudentGroupResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GroupController {
	private final GroupService groupService;

	@PostMapping("/groups/{groupId}")
	public ResponseEntity<StudentGroupResponse> joinGroup(@PathVariable final Long groupId, @RequestParam final Long userId) {
		return ResponseEntity.ok(groupService.joinGroupForStudent(groupId, userId));
	}

	@GetMapping("/groups")
	public ResponseEntity<List<GroupResponse>> getGroups(@RequestParam final Long userId) {
		return ResponseEntity.ok(groupService.getGroups(userId));
	}
}
