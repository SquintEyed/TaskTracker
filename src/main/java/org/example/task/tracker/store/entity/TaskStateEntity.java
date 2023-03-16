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
@Table(name = "task_states")
public class TaskStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Builder.Default
    @Column(name= "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "ordinal")
    private Integer ordinal;

    @ManyToOne
    @JoinColumn(name = "projects_id")
    private ProjectEntity projectEntity;

    @OneToMany(mappedBy = "taskStateEntity")
    @Builder.Default
    private List<TaskEntity> tasks = new ArrayList<>();

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(this == null || this.getClass() != o.getClass()){
            return false;
        }
        TaskStateEntity other = (TaskStateEntity) o;
        return (Objects.equals(getName(),other.getName()) &&
                Objects.equals(getCreatedAt(),other.getCreatedAt()) &&
                Objects.equals(getOrdinal(),other.getOrdinal()) &&
                Objects.equals(getTasks(),other.getTasks()));
    }

    @Override
    public int hashCode(){
        return Objects.hash(name,createdAt,ordinal,tasks);
    }
}
