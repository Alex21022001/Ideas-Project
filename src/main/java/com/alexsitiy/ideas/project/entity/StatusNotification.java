package com.alexsitiy.ideas.project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STATUS")
public class StatusNotification extends Notification{

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
