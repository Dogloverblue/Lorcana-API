package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.parameters.MandatorySQLExecutor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;

public class AdminHandler extends URLHandler {

    public AdminHandler() {
        super("admin", "card_info_submissions", defaultParameterValues(), defaultPrecursorValues());
    }

    private static ParameterManager defaultParameterValues() {
        ParameterManager pm = new ParameterManager();
        return pm;
    }

    private static PrecursorManager defaultPrecursorValues() {
        PrecursorManager pm = new PrecursorManager();
        return pm;
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

    @Override
    public void handle(HttpExchange t) throws IOException {
        String url = t.getRequestURI().toString();
        if (url.length() <= 6) {
            sendResponse(t, "ERROR: invalid url, please specify admin/auth, admin/users, etc.");
            return;
        }
        int amountToShave = url.endsWith("/") ? 1 : 0;
        String precursor = url.substring(7, url.length() - amountToShave);

        switch (precursor) {
            case "auth":
                postAuth(t);
                break;
            case "users":
                postUsers(t);
                break;
            case "adduser":
                postAddUser(t);
                break;
            case "removesubmissionsfromuser":
                postRemoveSubmissionsFromUser(t);
                break;
            case "mergedatabase":
                postMergeDatabase(t);
                break;
            case "getcardsfromuser":
                postGetCardsFromUser(t);
                break;
            case "deleteuser":
                postDeleteUser(t);
                break;
            default:
                sendResponse(t, "ERROR: invalid url, please specify admin/auth, admin/users, etc.");
                return;
        }

    }

    private void postDeleteUser(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        JSONObject requestObject = new JSONObject(requestBody);
        String tokenHash = requestObject.getString("tokenHash");
        String deleteSQL = "DELETE FROM ptcjlukd_lordb.user_logins WHERE token_hash = ?;";
        PreparedStatement ps = MandatorySQLExecutor.getPreparedStringStatement(deleteSQL, tokenHash);
        try {
            ps.executeUpdate();
            sendResponse(t, "success");
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: " + e.getMessage());
            return;
        }
    }

    private void postGetCardsFromUser(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        JSONObject requestObject = new JSONObject(requestBody);
        String tokenHash = requestObject.getString("tokenHash");
        String selectSQL = "SELECT * FROM card_info_submissions WHERE Submitter_Token_Hash = ?;";
        JSONArray response = MandatorySQLExecutor.getSQLResponseAsJSON(selectSQL, tokenHash);
        sendResponseJson(t, response.toString());
    }

    private void postMergeDatabase(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        replaceCardInfoData(t);
    }

    private void replaceCardInfoData(HttpExchange t) throws IOException {
        String insertSQL = "INSERT INTO card_info (Name, Type, Cost, Inkable, Color, Classifications, Body_Text, Abilities, Flavor_Text, Strength, Willpower, Lore, Move_Cost, Rarity, Artist, Franchise, Set_Name, Set_Num, Set_ID, Image, Card_Variants, Card_Num, Unique_ID, Date_Added, Date_Modified, Gamemode) "
                +
                "SELECT Name, Type, Cost, Inkable, Color, Classifications, Body_Text, Abilities, Flavor_Text, Strength, Willpower, Lore, Move_Cost, Rarity, Artist, Franchise, Set_Name, Set_Num, Set_ID, Image, Card_Variants, Card_Num, Unique_ID, Date_Added, Date_Modified, Gamemode "
                +
                "FROM card_info_submissions "
                +
                "ON DUPLICATE KEY UPDATE "
                +
                "Name = VALUES(Name), Type = VALUES(Type), Cost = VALUES(Cost), Inkable = VALUES(Inkable), Color = VALUES(Color), Classifications = VALUES(Classifications), Body_Text = VALUES(Body_Text), Abilities = VALUES(Abilities), Flavor_Text = VALUES(Flavor_Text), Strength = VALUES(Strength), Willpower = VALUES(Willpower), Lore = VALUES(Lore), Move_Cost = VALUES(Move_Cost), Rarity = VALUES(Rarity), Artist = VALUES(Artist), Franchise = VALUES(Franchise), Set_Name = VALUES(Set_Name), Set_Num = VALUES(Set_Num), Set_ID = VALUES(Set_ID), Image = VALUES(Image), Card_Variants = VALUES(Card_Variants), Card_Num = VALUES(Card_Num), Unique_ID = VALUES(Unique_ID), Date_Modified = VALUES(Date_Modified), Gamemode = VALUES(Gamemode);";
        String truncateSubmissionSQL = "truncate card_info_submissions;";

        Statement statement = MandatorySQLExecutor.getStatement();
        int rowsAffected = 0;
        try {

            rowsAffected = statement.executeUpdate(insertSQL);

            statement.executeUpdate(truncateSubmissionSQL);

        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(t, "failure");
        }

        sendResponseJson(t, "{\"success\":\"true\",\"rowsAffected\":\"" + rowsAffected + "\"}");

    }

    private boolean isAdminAuthed(String token) {
        JSONArray dbresponse = MandatorySQLExecutor
                .getSQLResponseAsJSON("SELECT permission_level FROM ptcjlukd_lordb.user_logins WHERE token_hash= ?;", token);
        String permissionLevel = dbresponse.getJSONObject(0).getString("permission_level");
        return permissionLevel.equalsIgnoreCase("admin");
    }

    private void postRemoveSubmissionsFromUser(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        JSONObject requestObject = new JSONObject(requestBody);
        String tokenHash = requestObject.getString("tokenHash").trim();
        String deleteSQL = "DELETE FROM card_info_submissions WHERE Submitter_Token_Hash = ?;";
        PreparedStatement ps = MandatorySQLExecutor.getPreparedStringStatement(deleteSQL, tokenHash);
        int rowsAffected = 0;
        try {
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: " + e.getMessage());
            return;
        }
        sendResponseJson(t, "{\"success\":\"true\",\"rowsAffected\":\"" + rowsAffected + "\"}");
    }

    private void postAddUser(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        JSONObject requestObject = new JSONObject(requestBody);

        String name = requestObject.getString("name");
        String newToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(newToken);
        String insertSQL = "INSERT INTO ptcjlukd_lordb.user_logins (name, token_hash, permission_level) VALUES (?, ?, ?);";
        PreparedStatement ps = MandatorySQLExecutor.getPreparedStringStatement(insertSQL, name, tokenHash, "user");
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(t, "ERROR: " + e.getMessage());
            return;
        }
        sendResponseJson(t, "{\"token\":\"" + newToken + "\"}");
    }

    private void postAuth(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (requestBody.isEmpty()) {
            sendResponse(t, "ERROR: No request body");
            return;
        }
        String token = hashToken(new JSONObject(requestBody).getString("token"));
        String username = new JSONObject(requestBody).getString("username");
        JSONArray dbresponse = MandatorySQLExecutor.getSQLResponseAsJSON(
                "SELECT permission_level FROM ptcjlukd_lordb.user_logins WHERE token_hash= ? AND name = ?;", token, username);
        String permissionLevel = dbresponse.length() > 0 ? dbresponse.getJSONObject(0).getString("permission_level")
                : "fail";
        String response = permissionLevel.equalsIgnoreCase("admin") ? "success" : "fail";

        sendResponse(t, response);
    }

    private void postUsers(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!isNormalAndAdminAuthed(t, requestBody)) {
            return;
        }
        JSONArray response = MandatorySQLExecutor.getSQLResponseAsJSON("SELECT * FROM ptcjlukd_lordb.user_logins;");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int amountOfContributions = MandatorySQLExecutor
                    .getSQLResponseAsJSON("SELECT COUNT(*) FROM card_info_submissions WHERE Submitter_Token_Hash = ?;",
                            user.getString("token_hash"))
                    .getJSONObject(0).getInt("COUNT(*)");
            user.put("amount", amountOfContributions);
        }
        sendResponseJson(t, response.toString());

    }

    private boolean isNormalAndAdminAuthed(HttpExchange t, String requestBody) throws IOException {
        if (requestBody.isEmpty()) {
            sendResponse(t, "ERROR: No request body");
            return false;
        }
        JSONObject requestObject;
        try {
            requestObject = new JSONObject(requestBody);
        } catch (Exception e) {
            sendResponse(t, "ERROR: Invalid request JSON");
            return false;
        }
        if (!requestObject.has("adminToken")) {
            sendResponse(t, "ERROR: No token provided");
            return false;
        }
        String token = hashToken(requestObject.getString("adminToken"));
        if (!isAdminAuthed(token)) {
            sendResponse(t, "ERROR: Invalid token");
            return false;
        }
        return true;
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
