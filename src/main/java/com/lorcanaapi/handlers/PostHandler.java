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
import java.util.Base64;

import org.json.JSONObject;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.MandatorySQLExecutor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;

public class PostHandler extends URLHandler {

    public PostHandler() {
        super("post", "card_info_submissions", defaultParameterValues(), defaultPrecursorValues());
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
        if (requestBody.isEmpty()) {
            sendResponse(t, "ERROR: No request body");
            return;
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: Invalid JSON");
            return;
        }

        if (!isTokenValid(jsonObject.getString("Token"))) {
            sendResponse(t, "ERROR: Invalid token");
            return;
        }

        String uniqueID = jsonObject.getString("Unique_ID");
        String checkSQL = "SELECT COUNT(*) FROM card_info_submissions WHERE Unique_ID = ?";

        try (PreparedStatement checkStatement = MandatorySQLExecutor.getPreparedStatement(checkSQL)) {
            checkStatement.setString(1, uniqueID);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                sendResponse(t, "ERROR: Card " + uniqueID + " already exists");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: " + e.getMessage());
            return;
        }

        String insertSQL = "INSERT INTO card_info_submissions (Submitter_Token_Hash, Name, Type, Cost, Inkable, Color, Classifications, Body_Text, Abilities, Flavor_Text, Strength, Willpower, Lore, Move_Cost, Rarity, Artist, Franchise, Set_Name, Set_Num, Set_ID, Image, Card_Variants, Card_Num, Unique_ID, Date_Added, Date_Modified, Gamemode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String tokenHash = hashToken(jsonObject.getString("Token"));

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
            preparedStatement.setString(17, jsonObject.getString("Franchise"));
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
                break;
            case "modify":
                postModify(t);
                break;
            case "delete":
                postDelete(t);
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

}
