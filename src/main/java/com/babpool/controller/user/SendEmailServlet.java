package com.babpool.controller.user;

import java.io.IOException;
import java.util.UUID;
import com.babpool.utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebServlet("/SendEmailServlet")
public class SendEmailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String inputCode = request.getParameter("code");

        if (email != null) {
            String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            session.setAttribute("authCode", code);
            session.setAttribute("authTime", System.currentTimeMillis());

            session.setAttribute("enteredEmail", email);
            session.removeAttribute("enteredCode");

            try {
                EmailUtil.sendVerificationEmail(email, code);
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect("join.jsp");
            return;
        }

        if (inputCode != null) {
            session.setAttribute("enteredCode", inputCode);

            boolean success = EmailUtil.verifyCode(session, inputCode);

            if (!success) {
                session.removeAttribute("enteredEmail");
                session.removeAttribute("enteredCode");
            }

            response.sendRedirect("join.jsp");
        }
    }
}
