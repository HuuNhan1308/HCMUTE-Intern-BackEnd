package com.intern.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String profileId;

    String FirstName;
    String LastName;
    String Gender;
    String Bio;


    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;


    @OneToMany(mappedBy = "upload_content")
    List<UploadContent> uploadContents;

}
