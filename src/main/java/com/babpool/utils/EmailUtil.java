package com.babpool.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.PrintWriter;
import java.util.Properties;

import com.babpool.filter.LogFileFilter;

import jakarta.servlet.http.HttpSession;

public class EmailUtil {

    public static void sendVerificationEmail(String recipient, String code) throws Exception {
        final String senderEmail = "ehdrnr65@skuniv.ac.kr";
        final String senderPassword = ApiKeyUtil.get("googleSenderPassword");
        PrintWriter logWriter = LogFileFilter.getWriter();
        if (logWriter != null) {
            logWriter.println("📨 인증 이메일 전송 → 수신자: " + recipient + ", 코드: " + code);
            logWriter.flush();
        }
        System.out.println("인증 이메일 전송 → 수신자: " + recipient + ", 코드: " + code);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("[BobPool] 이메일 인증 코드입니다");
        message.setText("인증코드: " + code + "\n3분 이내로 입력해 주세요.");

        Transport.send(message);
        System.out.println("이메일 전송 성공!");
    }

    // 입력한 인증번호와 세션 저장값을 비교하는 로직
    public static boolean verifyCode(HttpSession session, String userInputCode) {
        String savedCode = (String) session.getAttribute("authCode");
        Long savedTime = (Long) session.getAttribute("authTime");

        if (savedCode == null || savedTime == null) {
            session.setAttribute("emailAuthStatus", "fail");
            return false;
        }

        boolean valid = savedCode.equals(userInputCode);
        boolean withinTime = (System.currentTimeMillis() - savedTime) <= (3 * 60 * 1000);

        if (valid && withinTime) {
            session.setAttribute("isVerified", true);
            session.setAttribute("emailAuthStatus", "success");
            
            PrintWriter logWriter = LogFileFilter.getWriter();
            if (logWriter != null) {
                logWriter.println("인증 성공");
                logWriter.flush();
            }
            
            System.out.println("인증 성공");
            return true;
        } else {
            session.setAttribute("emailAuthStatus", "fail");
            
            
            PrintWriter logWriter = LogFileFilter.getWriter();
            if (logWriter != null) {
                logWriter.println("인증 실패 (만료 or 불일치)");
                logWriter.flush();
            }
            System.out.println("인증 실패 (만료 or 불일치)");
            return false;
        }
    }

}
