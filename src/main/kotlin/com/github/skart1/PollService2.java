package com.github.skart1;

import com.github.skart1.storage.game.GameEntity;
import com.github.skart1.storage.game.GameStorage;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PollService2 {
    private static final String APPLICATION_NAME = "Google Sheets API Java PollService";
    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"),
            ".credentials/sheets.googleapis.com-java-quickstart");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    static {
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Sheets sheetsService;
    private final HttpTransport httpTransport;
    private final List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);


    private final String spreadsheetId;
    private final GameStorage gameStorage;


    public PollService2(String spreadsheetId, GameStorage gameStorage)
            throws IOException, GeneralSecurityException {

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        sheetsService = getSheetsService();

        this.spreadsheetId = spreadsheetId;
        this.gameStorage = gameStorage;
    }

    private Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential authorize() throws IOException {
        // Load client secrets.
//        InputStream in = PollService.class.getResourceAsStream("secret.json");

        InputStream in = new FileInputStream("/LinStor/VRGame/src/main/resources/secret.json");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, scopes)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(getRunnable(), 1, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    public void doNow() {
        scheduler.submit(getRunnable());
    }

    private Runnable getRunnable() {
        return this::getGames;
    }

    private void getGames() {
        try {
            String range = "Games!A2:C";

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<String>> values = (List<List<String>>) (List) response.getValues();

            if (values == null) {
                System.err.println("No data found for games list. Possible wrong format?");
            } else {
//                for(int )
//
//
//                List<GameEntity> games = values.stream()
//                        .map(it -> {
//                            new GameParser()
//                        })
//                        .filter(r -> r != null)
//                        .collect(Collectors.toList());
//                gameStorage.setGames(games);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}