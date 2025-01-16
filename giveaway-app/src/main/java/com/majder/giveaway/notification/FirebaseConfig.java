package com.majder.giveaway.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream serviceAccount =
//                new FileInputStream("path/to/your-service-account-file.json");

        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json");

        if (serviceAccount == null) {
            throw new IOException("Service account file not found in resources");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
