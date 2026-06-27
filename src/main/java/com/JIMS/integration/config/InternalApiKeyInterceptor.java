package com.JIMS.integration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@Component
public class InternalApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger logger =
            LoggerFactory.getLogger(InternalApiKeyInterceptor.class);

    @Value("${my.api.key}")
    private String myApiKey;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String requestUri = request.getRequestURI();
        String method     = request.getMethod();
        logger.info("Interceptor: {} {}", method, requestUri);

        // 1️⃣ Allow OPTIONS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 2️⃣ Check Access Token
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            logger.info("Interceptor: Valid access token → user: {}", jwtUtil.getUserId(accessToken));
            return true;
        }

        // 3️⃣ Access Token expired → Check Refresh Token
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String userId = jwtUtil.getUserId(refreshToken);
            logger.info("Interceptor: Refresh token valid → generating new access token for user: {}", userId);

            // Generate new access token
            String newAccessToken = jwtUtil.generateAccessToken(userId);

            // Set new access token in cookie
            Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
            newAccessCookie.setHttpOnly(true);
            newAccessCookie.setSecure(true); // true if HTTPS
            newAccessCookie.setPath("/");
            newAccessCookie.setMaxAge((int) (jwtUtil.getAccessTokenExpiryTime() / 1000));
            response.addCookie(newAccessCookie);

            logger.info("Interceptor: New access token set in cookie");
            return true;
        }

        // 4️⃣ Check API Key
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null && apiKey.equals(myApiKey)) {
            logger.info("Interceptor: Valid API Key");
            return true;
        }

        // 5️⃣ Reject unauthorized
        logger.error("Interceptor: Unauthorized → {}", requestUri);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Unauthorized\",\"code\":\"TOKEN_EXPIRED\"}");
        return false;
    }
}