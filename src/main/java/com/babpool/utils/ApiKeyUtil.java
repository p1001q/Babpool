package com.babpool.utils;

import java.io.InputStream;
import java.util.Properties;

public class ApiKeyUtil {
    private static final String CONFIG_PATH = "config/api-keys.properties"; // 클래스패스 기준
    private static final Properties props = new Properties();

    // 클래스가 처음 로딩될 때 단 1번 실행
    static {
        try (InputStream input = ApiKeyUtil.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                System.err.println("ApiKeyUtil: '" + CONFIG_PATH + "' 파일을 클래스패스에서 찾을 수 없습니다.");
                throw new RuntimeException("API 키 파일 로딩 실패");
            }
            props.load(input);
            System.out.println("ApiKeyUtil: API 키 파일 로딩 성공");
        } catch (Exception e) {
            System.err.println("ApiKeyUtil: API 키 파일 로딩 중 오류 발생");
            e.printStackTrace();
        }
    }

    /**
     * 지정된 키 이름에 해당하는 값을 반환합니다.
     * 키가 존재하지 않으면 null 반환 + 경고 출력
     */
    public static String get(String keyName) {
        if (!props.containsKey(keyName)) {
            System.err.println("ApiKeyUtil: 키 '" + keyName + "'가 properties 파일에 존재하지 않습니다.");
            return null;
        }
        return props.getProperty(keyName);
    }
}
