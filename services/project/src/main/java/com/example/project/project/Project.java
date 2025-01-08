package com.example.project.project;

import com.example.project.projectAssignment.ProjectAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "project")
    private List<ProjectAssignment> assignments;

    public void update(ProjectUpdateRequest request) {
        if (request.name() != null) name = request.name();
        if (request.description() != null) description = request.description();
    }
}
