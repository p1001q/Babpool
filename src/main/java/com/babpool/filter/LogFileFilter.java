package com.babpool.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class LogFileFilter implements Filter {

    private static PrintWriter writer;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // web.xml에서 절대 경로 받아옴
            String absolutePath = filterConfig.getInitParameter("filename");

            // 바로 파일 오픈
            FileWriter fw = new FileWriter(absolutePath, true);  // append 모드
            writer = new PrintWriter(fw, true);

            writer.println("==== 로그 파일 시작: " + now() + " ====");
        } catch (IOException e) {
            throw new ServletException("로그 파일 생성 실패", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        if (writer != null) {
            writer.println("==== 로그 파일 종료: " + now() + " ====");
            writer.close();
        }
    }

    public static PrintWriter getWriter() {
        return writer;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
