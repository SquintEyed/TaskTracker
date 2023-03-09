package org.example.task.tracker.store.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",unique = true)
    private String name;

    @Builder.Default
    @Column(name= "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "projectEntity")
    @Builder.Default
    @Column(name = "task_states")
    private List<TaskStateEntity> taskStates = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null || o.getClass() != this.getClass())
            return false;
        ProjectEntity otherProjectEntity = (ProjectEntity) o;
        if(this.getName().equals(otherProjectEntity.getName()) &&
                this.getCreatedAt().equals(otherProjectEntity.getCreatedAt()) &&
                this.getTaskStates().equals(otherProjectEntity.getTaskStates()));
        return true;
    }

    @Override
    public  int hashCode(){
        return Objects.hash(name,createdAt,taskStates);

    }
}
