package com.example.project.projectAssignment;

import java.util.List;

public record ProjectAssignmentResponse(
        Integer projectId,
        List<Integer> userIds
) {
}
