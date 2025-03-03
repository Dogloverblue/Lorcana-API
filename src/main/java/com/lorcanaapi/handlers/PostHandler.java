package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.MandatorySQLExecutor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;

public class PostHandler extends URLHandler {

    public PostHandler() {
        super("post", "card_info_submissions", defaultParameterValues(), defaultPrecursorValues());
        PostHandler.refreshUnaddedCards();
    }

    private static ParameterManager defaultParameterValues() {
        ParameterManager pm = new ParameterManager();
        return pm;
    }

    private static PrecursorManager defaultPrecursorValues() {
        PrecursorManager pm = new PrecursorManager();
        return pm;
    }

    private boolean isTokenValid(String token) {
        String tokenHash = hashToken(token);
        String querySQL = "SELECT COUNT(*) FROM user_logins WHERE token_hash = ?";

        try (PreparedStatement preparedStatement = MandatorySQLExecutor.getPreparedStatement(querySQL)) {

            preparedStatement.setString(1, tokenHash);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void postAdd(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAuthed(t, requestBody)) {
            return;
        }
        JSONObject jsonObject = new JSONObject(requestBody);
        String tokenHash = hashToken(jsonObject.getString("Token"));

        String insertSQL = "INSERT INTO card_info_submissions (Submitter_Token_Hash, Name, Type, Cost, Inkable, Color, Classifications, Body_Text, Abilities, Flavor_Text, Strength, Willpower, Lore, Move_Cost, Rarity, Artist, Franchise, Set_Name, Set_Num, Set_ID, Image, Card_Variants, Card_Num, Unique_ID, Date_Added, Date_Modified, Gamemode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE "
            + "Submitter_Token_Hash = VALUES(Submitter_Token_Hash), Name = VALUES(Name), Type = VALUES(Type), Cost = VALUES(Cost), Inkable = VALUES(Inkable), Color = VALUES(Color), Classifications = VALUES(Classifications), Body_Text = VALUES(Body_Text), Abilities = VALUES(Abilities), Flavor_Text = VALUES(Flavor_Text), Strength = VALUES(Strength), Willpower = VALUES(Willpower), Lore = VALUES(Lore), Move_Cost = VALUES(Move_Cost), Rarity = VALUES(Rarity), Artist = VALUES(Artist), Franchise = VALUES(Franchise), Set_Name = VALUES(Set_Name), Set_Num = VALUES(Set_Num), Set_ID = VALUES(Set_ID), Image = VALUES(Image), Card_Variants = VALUES(Card_Variants), Card_Num = VALUES(Card_Num), Unique_ID = VALUES(Unique_ID), Date_Modified = VALUES(Date_Modified), Gamemode = VALUES(Gamemode);";


        try (PreparedStatement preparedStatement = MandatorySQLExecutor.getPreparedStatement(insertSQL)) {
            preparedStatement.setString(1, tokenHash);
            preparedStatement.setString(2, jsonObject.getString("Name"));
            preparedStatement.setString(3, jsonObject.getString("Type"));
            preparedStatement.setInt(4, jsonObject.getInt("Cost"));
            preparedStatement.setBoolean(5, jsonObject.getInt("Inkable") == 1);
            preparedStatement.setString(6, jsonObject.getString("Color"));
            preparedStatement.setString(7, jsonObject.optString("Classifications", null));
            preparedStatement.setString(8, jsonObject.optString("Body_Text", null));
            preparedStatement.setString(9, jsonObject.optString("Abilities", null));
            preparedStatement.setString(10, jsonObject.optString("Flavor_Text", null));
            if (jsonObject.isNull("Strength")) {
                preparedStatement.setNull(11, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(11, jsonObject.getInt("Strength"));
            }
            if (jsonObject.isNull("Willpower")) {
                preparedStatement.setNull(12, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(12, jsonObject.getInt("Willpower"));
            }
            if (jsonObject.isNull("Lore")) {
                preparedStatement.setNull(13, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(13, jsonObject.getInt("Lore"));
            }
            if (jsonObject.isNull("Move_Cost")) {
                preparedStatement.setNull(14, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(14, jsonObject.getInt("Move_Cost"));
            }
            preparedStatement.setString(15, jsonObject.getString("Rarity"));
            preparedStatement.setString(16, jsonObject.getString("Artist"));
            preparedStatement.setString(17, jsonObject.optString("Franchise"));
            preparedStatement.setString(18, jsonObject.getString("Set_Name"));
            preparedStatement.setInt(19, jsonObject.getInt("Set_Num"));
            preparedStatement.setString(20, jsonObject.getString("Set_ID"));
            preparedStatement.setString(21, jsonObject.optString("Image", null));
            preparedStatement.setString(22, jsonObject.optString("Card_Variants", null));
            preparedStatement.setInt(23, jsonObject.getInt("Card_Num"));
            preparedStatement.setString(24, jsonObject.getString("Unique_ID"));
            preparedStatement.setTimestamp(25, Timestamp.valueOf(jsonObject.getString("Date_Added")));
            preparedStatement.setTimestamp(26, Timestamp.valueOf(jsonObject.getString("Date_Modified")));
            preparedStatement.setString(27, jsonObject.optString("Gamemode", null));

            preparedStatement.executeUpdate();

            sendResponse(t, "Sucess!");

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: " + e.getMessage());
        }
    }

    private void postModify(HttpExchange t) throws IOException {
        sendResponse(t, "ERROR: Not implemented yet");
    }

    private void postDelete(HttpExchange t) throws IOException {
        sendResponse(t, "ERROR: Not implemented yet");
    }

    private void postGet(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAuthed(t, requestBody)) {
            return;
        }
        JSONObject jsonObject = new JSONObject(requestBody);
        if (!jsonObject.has("Unique_ID")) {
            sendResponse(t, "ERROR: No field Unique_ID");
            return;
        }
        String uniqueID = jsonObject.getString("Unique_ID");
        String querySQL = "SELECT * FROM card_info_submissions WHERE Unique_ID = ?";
        JSONArray jsonArray = MandatorySQLExecutor.getSQLResponseAsJSON(querySQL, uniqueID);
        if (jsonArray != null && jsonArray.length() > 0) {
            sendResponseJson(t, jsonArray.get(0).toString());
            return;
        }

        querySQL = "SELECT * FROM card_info WHERE Unique_ID = ?";
        jsonArray = MandatorySQLExecutor.getSQLResponseAsJSON(querySQL, uniqueID);
        if (jsonArray != null && jsonArray.length() > 0) {
            sendResponseJson(t, jsonArray.get(0).toString());
            return;
        }

        sendResponseJson(t, "{}");
    }

    private boolean isNormalAndAuthed(HttpExchange t, String requestBody) throws IOException {
        if (requestBody.isEmpty()) {
            sendResponse(t, "ERROR: No request body");
            return false;
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: Invalid JSON");
            return false;
        }

        if (!jsonObject.has("Token") || !isTokenValid(jsonObject.getString("Token"))) {
            sendResponse(t, "ERROR: Invalid token");
            return false;
        }
        return true;
    }

    private static final Set<String> UNADDED_CARDS = new TreeSet<String>();

    private static final String UNADDED_SET_ID = "ARI";
    private static final int NUMBER_OF_CARDS_IN_UNADDED_SET = 204;
    
    private static void refreshUnaddedCards() {
        String querySQL = "SELECT Unique_ID FROM card_info WHERE Set_ID = ?";
        JSONArray jsonArray = MandatorySQLExecutor.getSQLResponseAsJSON(querySQL, UNADDED_SET_ID);
        Set<String> addedCards = new HashSet<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            addedCards.add(jsonArray.getJSONObject(i).getString("Unique_ID"));
        }
        
        querySQL = "SELECT Unique_ID FROM card_info_submissions WHERE Set_ID = ?";
        jsonArray = MandatorySQLExecutor.getSQLResponseAsJSON(querySQL, UNADDED_SET_ID);
        for (int i = 0; i < jsonArray.length(); i++) {
            addedCards.add(jsonArray.getJSONObject(i).getString("Unique_ID"));
        }
        
        UNADDED_CARDS.clear();
        for (int i = 1; i <= NUMBER_OF_CARDS_IN_UNADDED_SET; i++) {
            String uniqueID = UNADDED_SET_ID + "-"+ String.format("%03d", i);
            if (!addedCards.contains(uniqueID)) {
                UNADDED_CARDS.add(uniqueID);
            }
        }
    }

    private void postGetUnaddedCards(HttpExchange t) throws IOException {
        sendResponseJson(t, new JSONArray(UNADDED_CARDS).toString());
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String url = t.getRequestURI().toString();
        if (url.length() <= 6) {
            sendResponse(t, "ERROR: Please specifiy post/add, post/modify, or post/delete");
            return;
        }
        int amountToShave = url.endsWith("/") ? 1 : 0;
        String precursor = url.substring(6, url.length() - amountToShave);

        switch (precursor) {
            case "add":
                postAdd(t);
                refreshUnaddedCards();
                break;
            case "modify":
                postModify(t);
                break;
            case "delete":
                postDelete(t);
                break;
            case "getcard":
                postGet(t);
                break;
            case "getunaddedcards":
                postGetUnaddedCards(t);
                break;
            default:
                sendResponse(t, "ERROR: Please specifiy post/add, post/modify, or post/delete");
                return;
        }

    }

    private void sendResponse(HttpExchange t, String response) throws IOException {
        response = "{\"message\":\"" + response + "\"}";
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        t.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        t.getResponseHeaders().set("Content-Type",
                String.format("application/json; charset=%s", StandardCharsets.UTF_8));

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void sendResponseJson(HttpExchange t, String response) throws IOException {
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        t.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        t.getResponseHeaders().set("Content-Type",
                String.format("application/json; charset=%s", StandardCharsets.UTF_8));

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
