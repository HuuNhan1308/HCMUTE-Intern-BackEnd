package com.intern.app.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseStorageConfig {

    @PostConstruct
    public void initializeFirebase() throws IOException {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/serviceAccountKey.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("hcmute-intern-storage.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);

            log.info("FirebaseApp initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
