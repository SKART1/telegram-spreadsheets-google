package com.github.skart1

import com.github.skart1.storage.game.GameEntity
import com.github.skart1.storage.game.GameStorage
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class PollService @Throws(IOException::class, GeneralSecurityException::class)
constructor(private val spreadsheetId: String, private val gameStorage: GameStorage) {

    private val scheduler = Executors.newScheduledThreadPool(1)
    private val sheetsService: Sheets = getSheetsService()
    private val httpTransport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val scopes = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY)

    @Throws(IOException::class)
    private fun getSheetsService(): Sheets {
        val credential = authorize()
        return Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    @Throws(IOException::class)
    private fun authorize(): Credential {
        // Load client secrets.
        val inputStream = PollService::class.java.getResourceAsStream("secret.json")

        //val inputStream = FileInputStream("/LinStor/VRGame/src/main/resources/secret.json")

        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(DATA_STORE_FACTORY!!)
                .setAccessType("offline")
                .build()
        val credential = AuthorizationCodeInstalledApp(
                flow, LocalServerReceiver()).authorize("user")
        println("Credentials saved to " + DATA_STORE_DIR.absolutePath)
        return credential
    }

    fun start() {
        scheduler.scheduleAtFixedRate(getRunnable(), 1, 10, TimeUnit.SECONDS)
    }

    fun stop() {
        scheduler.shutdownNow()
    }

    fun doNow() {
        scheduler.submit(getRunnable())
    }

    private fun getRunnable(): Runnable {
        return Runnable {
            getGames()
        }
    }

    private fun getGames(): String {
        val range = "Games!A2:C"

        val response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute()
        val values = response.getValues() as List<List<String>>

        val gamesAndErrors = values.mapIndexed {
            i, list ->
            val parser = GameParser(i, list)
            parser.parse()
            when (parser.error) {
                GameParser.ErrorLevel.ERROR -> {
                    Pair<GameEntity?, StringBuilder>(null, parser.parseMessages)
                }
                GameParser.ErrorLevel.WARNING -> {
                    Pair<GameEntity?, StringBuilder>(parser.getResult(), parser.parseMessages)
                }
                GameParser.ErrorLevel.NONE -> {
                    Pair<GameEntity?, StringBuilder?>(parser.getResult(), null)
                }
            }
        }

        //Games
        val games = gamesAndErrors
                .map(Pair<GameEntity?, StringBuilder?>::first)
                .filterNotNull()
        gameStorage.setGames(games)

        //Message
        return gamesAndErrors
                .map(Pair<GameEntity?, StringBuilder?>::second)
                .filterNotNull()
                .reduce { left, right ->  left.append("\n").append(right) }.toString()
    }

    companion object {
        private val APPLICATION_NAME = "Google Sheets API Java PollService"
        private val DATA_STORE_DIR = File(System.getProperty("user.home"),
                ".credentials/sheets.googleapis.com-java-quickstart")
        private val DATA_STORE_FACTORY = FileDataStoreFactory(DATA_STORE_DIR)
        private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    }
}