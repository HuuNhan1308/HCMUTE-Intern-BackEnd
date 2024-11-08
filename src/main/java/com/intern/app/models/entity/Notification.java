package com.intern.app.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String notificationId;

    String title;
    String content;
    String path;
    Boolean read;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    Profile owner;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    Profile profile;
}
