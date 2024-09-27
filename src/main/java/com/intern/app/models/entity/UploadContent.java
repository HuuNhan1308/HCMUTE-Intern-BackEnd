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
@Table(name = "upload_content")
public class UploadContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String uploadContentId;

    String fileName;

    @Column(name = "file_data", columnDefinition = "BYTEA")
    private byte[] fileData;

    @OneToOne
    @JoinColumn(name = "ProfileId", nullable = false)
    Profile profile;
}
