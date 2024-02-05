package org.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

import org.json.JSONObject;

public class CrptApi {
    private static final int DEFAULT_REQUEST_LIMIT = 10;
    private static final int DEFAULT_TIME_UNIT = TimeUnit.SECONDS.ordinal();

    private int requestLimit;
    private TimeUnit timeUnit;
    private ExecutorService executorService;
    private BlockingQueue<Runnable> queue;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        executorService = Executors.newFixedThreadPool(requestLimit);
        queue = new ArrayBlockingQueue<Runnable>(requestLimit);
    }

    public void createDocument(JSONObject document, String signature) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://ismp.crpt.ru/api/v3/lk/documents/create");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    JSONObject request = new JSONObject();
                    request.put("description", document);
                    request.put("signature", signature);
                    connection.getOutputStream().write(request.toString().getBytes());
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        JSONObject response = new JSONObject(connection.getResponseMessage());
                        System.out.println(response);
                    } else {
                        System.out.println("Error: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        queue.offer(task);
        executorService.execute(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
