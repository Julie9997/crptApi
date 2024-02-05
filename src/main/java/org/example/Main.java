package org.example;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 10);

        JSONObject document = new JSONObject();
        document.put("name", "John Doe");
        document.put("age", 30);
        document.put("address", "123 Main St");

        String signature = "1234567890abcdef";

        api.createDocument(document, signature);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
