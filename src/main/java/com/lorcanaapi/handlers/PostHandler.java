package com.lorcanaapi.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.lorcanaapi.ParameterManager;
import com.lorcanaapi.precursors.AllPrecursor;
import com.lorcanaapi.precursors.FetchPrecursor;
import com.lorcanaapi.precursors.PrecursorManager;
import com.sun.net.httpserver.HttpExchange;

public class PostHandler extends URLHandler {

    public PostHandler() {
        super("post", "post_info", defaultParameterValues(), defaultPrecursorValues());
    }

    private static ParameterManager defaultParameterValues() {
        ParameterManager pm = new ParameterManager();
        System.out.println("PostHandler: defaultParameterValues: pm: " + pm);
        return pm;
    }

    private static PrecursorManager defaultPrecursorValues() {
        PrecursorManager pm = new PrecursorManager();
        return pm;
    }

    private void postAdd(HttpExchange t) throws IOException {
        String requestBody = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("adding: " + requestBody);
        System.out.println("test");
        sendResponse(t, "you added");
    }

    private void postModify(HttpExchange t) {
        // TODO: this
    }

    private void postDelete(HttpExchange t) {
        // TODO: this
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        System.out.println(t.getRequestURI());
        String url = t.getRequestURI().toString();
        if (url.length() <= 6) {
            sendResponse(t, "ERROR: Please specifiy post/add, post/modify, or post/delete");
            return;
        }
        int amountToShave = url.endsWith("/") ? 1 : 0;
        String precursor = url.substring(6, url.length() - amountToShave);
        System.out.println("pre |" + precursor + "|");

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
        System.out.println("response: " + response);
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
