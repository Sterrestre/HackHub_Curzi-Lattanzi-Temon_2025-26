package it.unicam.cs.ids.infrastructure;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GmailClientFactory {

    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND);

    public static Gmail createGmailClient() throws Exception {

        InputStream credentialsStream = GmailCredentialsProvider.getCredentialsStream();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),
                new InputStreamReader(credentialsStream)
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                clientSecrets,
                SCOPES
        ).setAccessType("offline").build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Hackathon App").build();
    }
}