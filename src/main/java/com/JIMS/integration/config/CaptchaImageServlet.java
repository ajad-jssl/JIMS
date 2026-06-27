package com.JIMS.integration.config;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


	@CrossOrigin
	@RestController
	@RequestMapping("/api")
	public class CaptchaImageServlet {

		
		 private static String latestCaptcha; 
		
		 @Autowired
			private RateLimiterService rateLimiterService;
		
		
		 @GetMapping("/captcha-image")
		 public void getCaptcha(HttpServletResponse response, 
		                        HttpSession session,
		                        HttpServletRequest request) throws IOException {

		     // ✅ Rate limit by IP — 10 captcha requests per minute per IP
		     String captchaIpKey = "CAPTCHA_IP_" + request.getRemoteAddr();
		     boolean allowed = rateLimiterService.tryConsume(captchaIpKey, 10, 1);

		     if (!allowed) {
//		         logger.warn("Captcha rate limit exceeded for IP: {}", request.getRemoteAddr());
		         response.setStatus(429);
		         response.setContentType("image/png");

		         // ✅ Return a blank/error image instead of crashing
		         int width = 160, height = 50;
		         BufferedImage errorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		         Graphics2D g = errorImage.createGraphics();
		         g.setColor(Color.LIGHT_GRAY);
		         g.fillRect(0, 0, width, height);
		         g.setColor(Color.RED);
		         g.setFont(new Font("Arial", Font.BOLD, 12));
		         g.drawString("To many request", 10, 30);
		         g.dispose();
		         ImageIO.write(errorImage, "png", response.getOutputStream());
		         return;
		     }

		     // ✅ Normal captcha generation below — your existing code unchanged
		     int width = 160;
		     int height = 50;
		     BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		     Graphics2D g = bufferedImage.createGraphics();
		     Random rand = new Random();

		     String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
		     StringBuilder captchaStr = new StringBuilder();

		     g.setColor(Color.WHITE);
		     g.fillRect(0, 0, width, height);
		     g.setFont(new Font("Arial", Font.BOLD, 40));

		     for (int i = 0; i < 5; i++) {
		         String ch = String.valueOf(chars.charAt(rand.nextInt(chars.length())));
		         captchaStr.append(ch);
		         g.setColor(new Color(rand.nextInt(150), rand.nextInt(150), rand.nextInt(150)));
		         g.drawString(ch, 25 * i + 10, 35 + rand.nextInt(10));
		     }

		     for (int i = 0; i < 6; i++) {
		         g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		         g.drawLine(rand.nextInt(width), rand.nextInt(height), 
		                    rand.nextInt(width), rand.nextInt(height));
		     }

		     latestCaptcha = captchaStr.toString();
		     session.setAttribute("captcha", captchaStr.toString());

		     response.setContentType("image/png");
		     ImageIO.write(bufferedImage, "png", response.getOutputStream());
		     g.dispose();
		 }
	    public static String getLatestCaptcha() {
	        return latestCaptcha;
	    }
	}
