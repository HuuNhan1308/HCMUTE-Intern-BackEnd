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
@Table(name = "avatar")
public class Avatar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String avatarId;

    String fileName;
    String fileType;

    String ownerId;

    @Column(name = "file_data", columnDefinition = "BYTEA")
    byte[] fileData;
}
