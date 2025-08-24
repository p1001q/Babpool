package com.babpool.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchAndConvertPlaceUtil {

    private static final String CLIENT_ID = ApiKeyUtil.get("naver.clientId");
    private static final String CLIENT_SECRET = ApiKeyUtil.get("naver.clientSecret");

    // 장소명으로 WGS84 좌표 가져오기
    public static double[] getPlaceCoordinates(String placeName) throws IOException {
        String encodedPlaceName = URLEncoder.encode(placeName, StandardCharsets.UTF_8);
        String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + encodedPlaceName + "&display=1";

        HttpURLConnection connection = null;
        int attempts = 0;

        while (attempts < 3) {
            try {
                URL url = new URL(apiURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);
                    in.close();

                    JSONObject json = new JSONObject(response.toString());
                    JSONArray items = json.getJSONArray("items");
                    if (items.length() > 0) {
                        JSONObject item = items.getJSONObject(0);
                        double x = Double.parseDouble(item.getString("mapx")) / 1e7;
                        double y = Double.parseDouble(item.getString("mapy")) / 1e7;
                        return new double[]{x, y};
                    }
                } else {
                    BufferedReader err = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                    while (err.readLine() != null); // 오류 메시지 무시
                    err.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
            attempts++;
        }
        return null;
    }

    // WGS84 → TM (EPSG:3857)
    public static double[] convertWGS84To3857(double wgsX, double wgsY) {
        double x = wgsX * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + wgsY) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        return new double[]{x, y};
    }
}
