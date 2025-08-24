package com.babpool.utils;

public class NaverMapUrlUtil {

    private static final String BASE_URL = "https://map.naver.com/p/directions/";
    private static final String START_TM_X = "14138890.8265643";
    private static final String START_TM_Y = "4525316.5087329";
    private static final String START_UNICODE = "%EC%84%9C%EA%B2%BD%EB%8C%80%ED%95%99%EA%B5%90%EB%B6%81%EC%95%85%EA%B4%80";
    private static final String START_PLACE_ID = "17563706";

    public static String generateDirectionUrl(double destTmX, double destTmY, String destUnicode, int destPlaceId) {
        return BASE_URL 
            + START_TM_X + "," + START_TM_Y + "," + START_UNICODE + "," + START_PLACE_ID + ",PLACE_POI/"
            + destTmX + "," + destTmY + "," + destUnicode + "," + destPlaceId + ",PLACE_POI/-/walk?c=17.00,0,0,0,dh";
    }
}