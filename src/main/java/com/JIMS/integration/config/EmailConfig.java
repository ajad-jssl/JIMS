
package com.JIMS.integration.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Configuration
public class EmailConfig {

    @Autowired
    private JavaMailSender mailSender;
    
//    public void sendEmail(String to, String username, String password) throws MessagingException, IOException {
//        String clientId = "<your-client-id>";
//        String clientSecret = "<your-client-secret>";
//        String tenantId = "<your-tenant-id>";
//
//        try {
//        	  String accessToken = OAuth2TokenProvider.getAccessToken(clientId, clientSecret, tenantId);
//
//              MimeMessage mimeMessage = mailSender.createMimeMessage();
//              MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//              helper.setFrom("<your-email@example.com>");
//              helper.setTo(to);
//              helper.setSubject("Your Account Details");
//              helper.setText("Hello " + username + ",\n\nYour account has been created. Your credentials are:\n" +
//                      "Username: " + username + "\nPassword: " + password + "\n\nPlease change your password after the first login.", true);
//
//              mimeMessage.setHeader("Authorization", "Bearer " + accessToken);
//              mailSender.send(mimeMessage);
//        }catch (Exception e) {
//			// TODO: handle exception
//		}
//      
//    }
    public int sendEmail(String to, String username, String password) throws MessagingException {
    	 try {
    	        MimeMessage message = mailSender.createMimeMessage();
    	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
    	        helper.setFrom("jssl.gpms@jssl.in");
    	        
    	        String subject = "JSSL CREDENTIALS GPMS";
    	        String content = "<p>Hello " + username + ",</p>"
    	                + "<p>Here are your account details:</p>"
    	                + "<ul>"
    	                + "<li><strong>Username:</strong> " + username + "</li>"
    	                + "<li><strong>Password:</strong> " + password + "</li>"
    	                + "</ul>";

    	        helper.setTo(to);
    	        helper.setSubject(subject);
    	        helper.setText(content, true); // true = HTML content

    	        mailSender.send(message);
    	        return 1; // Success
    	    } catch (MessagingException e) {
    	        e.printStackTrace();
    	        return 0; // Failure
    	    }
    }
//
//    public void sendEmailLink(String to,  String password) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        // Email Details
//        String subject = "JSSL LOGIN LINK ";
//        String content = "<p>Hello USER </p>"
//                + "<p>Here are your account details:</p>"
//                + "<ul>"
//                + "<li><strong>EMAIL-ID:</strong> " + to + "</li>"
//                + "<li><strong>OTP:</strong> " + password + "</li>"
//                + "</ul>"
//          //      + "<p>Please use the following link to log in:</p>"
//           //     + "<p><a href='" + link + "'>" + link + "</a></p>"
//                + "<p>Thank you!</p>";
//        helper.setFrom("jssl.gpms@jssl.in");
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(content, true); // true = HTML content
//
//        // Send the email
//        mailSender.send(message);
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void sendEmailLink(String to, String otp, String usernameByEmail) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "JSSL – Password Reset Verification";

        String content =
        		"<!DOCTYPE html>"
        		        + "<html>"
        		        + "<head>"
        		        + "  <meta charset='UTF-8'>"
        		        + "  <title>JSSL Intranet System</title>"
        		        + "</head>"

        		        + "<body style='margin:0; padding:0; background-color:#f3f4f6; font-family:Arial, Helvetica, sans-serif;'>"

        		        + "<table width='100%' cellpadding='0' cellspacing='0'>"
        		        + "<tr>"
        		        + "<td align='center' style='padding:30px 10px;'>"

        		        + "<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff; border-radius:10px; box-shadow:0 6px 18px rgba(0,0,0,0.08); overflow:hidden;'>"

        		   
        		        + "<tr>"
        		        + "<td style='background:green; padding:30px; text-align:center;'>"
        		        + "<h1 style='margin:0; font-size:26px; font-weight:600; color:#ffffff; letter-spacing:0.5px;'>"
        		        + "JSSL Intranet System"
        		        + "</h1>"
        		        + "<p style='margin-top:8px; margin-bottom:0; font-size:14px; color:#e5f3eb;'>"
        		        + "Secure Internal Access Portal"
        		        + "</p>"
        		        + "</td>"
        		        + "</tr>"

        		
        		        + "<tr>"
        		        + "<td style='padding:35px 40px; color:#1f2933;'>"

        		        + "<p style='font-size:16px; margin-top:0;'>"
        		        + "Hello <strong>" + usernameByEmail + "</strong>,"
        		        + "</p>"

        		        + "<p style='font-size:15px; line-height:1.6;'>"
        		        + "We received a request to reset your password for the "
        		        + "<strong>JSSL Intranet System</strong>."
        		        + "</p>"

        		   
        		        + "<div style='background:#f0f7f3; border-left:4px solid #3f8f67; padding:15px 18px; margin:22px 0; border-radius:6px; font-size:15px;'>"
        		        + "<strong>Your OTP:</strong> "
        		        + "<span style='font-size:18px; font-weight:600; letter-spacing:2px; color:#2f6f4f;'>"
        		        + otp
        		        + "</span>"
        		        + "</div>"

        		        + "<p style='font-size:14px; line-height:1.6;'>"
        		        + "Use this OTP to continue with your password reset. "
        		        + "This OTP is valid for a limited time for security reasons."
        		        + "</p>"

        		      
        		        +"<div style=\"text-align:center; margin:35px 0;\">"
        		        + "<a href=\"https://jims.jssl.in/otpval.jsp\" "
        		        + "style=\"background:#2f6f4f; color:#ffffff; padding:14px 32px; "
        		        + "text-decoration:none; font-size:15px; border-radius:6px; "
        		        + "display:inline-block; font-weight:600;\">"
        		        + "Reset Password"
        		        + "</a>"
        		        + "</div>"

        		        + "<p style='font-size:13px; color:#6b7280; line-height:1.5;'>"
        		        + "If you did not request this action, please ignore this email "
        		        + "or contact the IT support team immediately."
        		        + "</p>"

        		        + "</td>"
        		        + "</tr>"

        		      
        		        + "<tr>"
        		        + "<td style='background:#000000; padding:18px; text-align:center; font-size:12px; color:#ffffff;'>"
        		        + "© 2026 JSSL Intranet System · Internal Use Only"
        		        + "</td>"
        		        + "</tr>"

        		        + "</table>"

        		        + "</td>"
        		        + "</tr>"
        		        + "</table>"

        		        + "</body>"
        		        + "</html>";

        helper.setFrom("jssl.gpms@jssl.in");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    
    
    
    
    public int sendTicketRaisedEmail(
            String ticketNo,
            String machineType,
            String machineName,
            String whatBroke,
            String problemDesc,
            String reportedBy,
            String empCode,
            String empName,
            String mobileNo) {

        String[] assignorEmails = {
            "aftab.b@jssl.in"
            
        };

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("jssl.gpms@jssl.in");
            helper.setTo(assignorEmails);
            helper.setSubject("New Maintenance Ticket – " + ticketNo);

            String mobileRow = (mobileNo != null && !mobileNo.trim().isEmpty())
                ? "<tr>"
                + "<td style='padding:9px 14px;font-size:13px;color:#5a7a6a;white-space:nowrap;background:#f7faf8;'>Mobile</td>"
                + "<td style='padding:9px 14px;font-size:13px;color:#2d3f35;'>" + mobileNo + "</td>"
                + "</tr>"
                : "";

            String content =
                "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='margin:0;padding:0;background:#eef4f1;"
                +       "font-family:Arial,Helvetica,sans-serif;'>"

                + "<table width='100%' cellpadding='0' cellspacing='0'>"
                + "<tr><td align='center' style='padding:32px 10px;'>"

                + "<table width='600' cellpadding='0' cellspacing='0'"
                +        " style='background:#ffffff;border-radius:10px;"
                +        "border:1px solid #d4e8dd;overflow:hidden;'>"

                // ── Header ────────────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#3d7a5c;padding:24px 30px;'>"
                + "<table width='100%' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td>"
                + "<h1 style='margin:0;font-size:20px;font-weight:700;color:#ffffff;'>"
                + "JSSL Maintenance System"
                + "</h1>"
                + "<p style='margin:5px 0 0;font-size:12px;color:#b8d9cb;letter-spacing:0.3px;'>"
                + "Ticket Raised – Action Required"
                + "</p>"
                + "</td>"
                + "<td align='right' style='vertical-align:middle;'>"
                + "<span style='background:#fff3cd;color:#2a6645;font-size:12px;"
                +       "font-weight:700;padding:5px 13px;border-radius:20px;"
                +       "white-space:nowrap;letter-spacing:0.3px;'>"
                + "● OPEN"
                + "</span>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"

                // ── Ticket banner ─────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#f2f8f5;padding:16px 30px;"
                +     "border-bottom:1px solid #d4e8dd;'>"
                + "<p style='margin:0;font-size:11px;color:#7a9e8c;text-transform:uppercase;"
                +     "letter-spacing:0.5px;'>Ticket Number</p>"
                + "<p style='margin:5px 0 0;font-size:24px;font-weight:700;"
                +     "color:#2a5c42;letter-spacing:0.5px;'>"
                + ticketNo
                + "</p>"
                + "</td>"
                + "</tr>"

                // ── Body ──────────────────────────────────────────────────────
                + "<tr>"
                + "<td style='padding:26px 30px;color:#2d3f35;'>"

                + "<p style='font-size:14px;margin-top:0;line-height:1.6;color:#4a6659;'>"
                + "A new maintenance ticket has been raised and is awaiting assignment. "
                + "Please review the details below and assign a technician."
                + "</p>"

                // ── Machine Information ────────────────────────────────────────
                + "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>"
                + "Machine Information"
                + "</p>"
                + "<table cellpadding='0' cellspacing='0'"
                +        " style='width:100%;border:1px solid #d4e8dd;"
                +        "border-radius:7px;overflow:hidden;margin-bottom:18px;font-size:13px;'>"
                + "<tr>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "width:150px;background:#f7faf8;'>Machine Type</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;'>" + machineType + "</td>"
                + "</tr>"
                + "<tr style='border-top:1px solid #eaf3ee;'>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "background:#f7faf8;'>Machine</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;font-weight:600;'>" + machineName + "</td>"
                + "</tr>"
                + "<tr style='border-top:1px solid #eaf3ee;'>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "background:#f7faf8;vertical-align:top;'>What Broke</td>"
                + "<td style='padding:9px 14px;'>"
                + formatWhatBroke(whatBroke)
                + "</td>"
                + "</tr>"
                + "</table>"

                // ── Problem Description ────────────────────────────────────────
                + "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>"
                + "Problem Description"
                + "</p>"
                + "<div style='background:#f7faf8;border-left:3px solid #3d7a5c;"
                +     "padding:13px 16px;border-radius:0 6px 6px 0;"
                +     "font-size:13px;line-height:1.7;color:#2d3f35;margin-bottom:18px;'>"
                + problemDesc
                + "</div>"

                // ── Reported By ───────────────────────────────────────────────
                + "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>"
                + "Reported By"
                + "</p>"
                + "<table cellpadding='0' cellspacing='0'"
                +        " style='width:100%;border:1px solid #d4e8dd;"
                +        "border-radius:7px;overflow:hidden;margin-bottom:22px;font-size:13px;'>"
                + "<tr>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "width:150px;background:#f7faf8;'>Employee Code</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;font-weight:700;"
                +     "letter-spacing:0.3px;'>" + empCode + "</td>"
                + "</tr>"
                + "<tr style='border-top:1px solid #eaf3ee;'>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "background:#f7faf8;'>Name</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;'>" + empName + "</td>"
                + "</tr>"
                + mobileRow
                + "</table>"

                // ── CTA button ────────────────────────────────────────────────
                + "<div style='text-align:center;margin:26px 0 6px;'>"
                + "<a href='https://jims.jssl.in/Maintenanceassign.jsp'"
                +    " style='background:#3d7a5c;color:#ffffff;padding:12px 34px;"
                +    "text-decoration:none;font-size:14px;font-weight:700;"
                +    "border-radius:6px;display:inline-block;letter-spacing:0.3px;'>"
                + "Assign Technician"
                + "</a>"
                + "</div>"

                + "<p style='font-size:12px;color:#8aaa99;text-align:center;margin-top:14px;'>"
                + "Log in to the JSSL Intranet System to act on this ticket."
                + "</p>"

                + "</td>"
                + "</tr>"

                // ── Footer ────────────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#2a5c42;padding:15px 30px;"
                +     "text-align:center;font-size:11px;color:#8ec4ab;'>"
                + "© 2026 JSSL Intranet System &nbsp;·&nbsp; Internal Use Only"
                + "</td>"
                + "</tr>"

                + "</table>"
                + "</td></tr></table>"
                + "</body></html>";

            helper.setText(content, true);
            mailSender.send(message);
            return 1;

        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String formatWhatBroke(String whatBroke) {
        if (whatBroke == null || whatBroke.trim().isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (String item : whatBroke.split(",")) {
            item = item.trim();
            if (!item.isEmpty()) {
                sb.append("<span style='display:inline-block;background:#d6f0e4;"
                        + "color:#2a5c42;font-size:12px;font-weight:600;"
                        + "padding:3px 10px;border-radius:20px;margin:2px 4px 2px 0;"
                        + "border:1px solid #b8ddc8;'>"
                        + item + "</span>");
            }
        }
        return sb.toString();
    }
    
    
    
    public int sendTicketAssignedEmail(
            String workerEmail,
            String workerName,
            String ticketNo,
            String machineType,
            String machineName,
            String whatBroke,
            String problemDesc,
            String assignNote) {
 
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setFrom("jssl.gpms@jssl.in");
            helper.setTo(workerEmail);
            helper.setSubject("Ticket Assigned to You – " + ticketNo);
 
            String noteRow = (assignNote != null && !assignNote.trim().isEmpty())
                ? "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>Assignor Note</p>"
                + "<div style='background:#f7faf8;border-left:3px solid #3d7a5c;"
                +     "padding:13px 16px;border-radius:0 6px 6px 0;"
                +     "font-size:13px;line-height:1.7;color:#2d3f35;margin-bottom:18px;'>"
                + assignNote
                + "</div>"
                : "";
 
            String content =
                "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='margin:0;padding:0;background:#eef4f1;"
                +       "font-family:Arial,Helvetica,sans-serif;'>"
 
                + "<table width='100%' cellpadding='0' cellspacing='0'>"
                + "<tr><td align='center' style='padding:32px 10px;'>"
 
                + "<table width='600' cellpadding='0' cellspacing='0'"
                +        " style='background:#ffffff;border-radius:10px;"
                +        "border:1px solid #d4e8dd;overflow:hidden;'>"
 
                // ── Header ──────────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#3d7a5c;padding:24px 30px;'>"
                + "<table width='100%' cellpadding='0' cellspacing='0'>"
                + "<tr>"
                + "<td>"
                + "<h1 style='margin:0;font-size:20px;font-weight:700;color:#ffffff;'>"
                + "JSSL Maintenance System"
                + "</h1>"
                + "<p style='margin:5px 0 0;font-size:12px;color:#b8d9cb;'>"
                + "Ticket Assigned – Action Required"
                + "</p>"
                + "</td>"
                + "<td align='right' style='vertical-align:middle;'>"
                + "<span style='background:#fff3cd;color:#7a5c00;font-size:12px;"
                +       "font-weight:700;padding:5px 13px;border-radius:20px;"
                +       "white-space:nowrap;letter-spacing:0.3px;'>"
                + "● ASSIGNED"
                + "</span>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
 
                // ── Ticket banner ────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#f2f8f5;padding:16px 30px;"
                +     "border-bottom:1px solid #d4e8dd;'>"
                + "<p style='margin:0;font-size:11px;color:#7a9e8c;"
                +     "text-transform:uppercase;letter-spacing:0.5px;'>Ticket Number</p>"
                + "<p style='margin:5px 0 0;font-size:24px;font-weight:700;"
                +     "color:#2a5c42;letter-spacing:0.5px;'>"
                + ticketNo
                + "</p>"
                + "</td>"
                + "</tr>"
 
                // ── Body ─────────────────────────────────────────────────────
                + "<tr>"
                + "<td style='padding:26px 30px;color:#2d3f35;'>"
 
                // Greeting
                + "<p style='font-size:14px;margin-top:0;line-height:1.6;color:#2d3f35;'>"
                + "Hello <strong>" + workerName + "</strong>,"
                + "</p>"
                + "<p style='font-size:14px;line-height:1.6;color:#4a6659;margin-top:0;'>"
                + "A maintenance ticket has been <strong>assigned to you</strong>. "
                + "Please attend to it at the earliest."
                + "</p>"
 
                // ── Machine Information ──────────────────────────────────────
                + "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>"
                + "Machine Information"
                + "</p>"
                + "<table cellpadding='0' cellspacing='0'"
                +        " style='width:100%;border:1px solid #d4e8dd;"
                +        "border-radius:7px;overflow:hidden;margin-bottom:18px;font-size:13px;'>"
                + "<tr>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "width:150px;background:#f7faf8;'>Machine Type</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;'>" + machineType + "</td>"
                + "</tr>"
                + "<tr style='border-top:1px solid #eaf3ee;'>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "background:#f7faf8;'>Machine</td>"
                + "<td style='padding:9px 14px;color:#2d3f35;font-weight:600;'>" + machineName + "</td>"
                + "</tr>"
                + "<tr style='border-top:1px solid #eaf3ee;'>"
                + "<td style='padding:9px 14px;color:#5a7a6a;white-space:nowrap;"
                +     "background:#f7faf8;vertical-align:top;'>What Broke</td>"
                + "<td style='padding:9px 14px;'>"
                + formatWhatBroke(whatBroke)
                + "</td>"
                + "</tr>"
                + "</table>"
 
                // ── Problem Description ──────────────────────────────────────
                + "<p style='font-size:11px;font-weight:700;color:#3d7a5c;"
                +     "text-transform:uppercase;letter-spacing:0.7px;"
                +     "margin:22px 0 8px;padding-bottom:5px;"
                +     "border-bottom:2px solid #c8e6d8;'>"
                + "Problem Description"
                + "</p>"
                + "<div style='background:#f7faf8;border-left:3px solid #3d7a5c;"
                +     "padding:13px 16px;border-radius:0 6px 6px 0;"
                +     "font-size:13px;line-height:1.7;color:#2d3f35;margin-bottom:18px;'>"
                + problemDesc
                + "</div>"
 
                // ── Assignor note (conditional) ──────────────────────────────
                + noteRow
 
                // ── CTA ──────────────────────────────────────────────────────
                + "<div style='text-align:center;margin:26px 0 6px;'>"
                + "<a href='https://jims.jssl.in/Maintenanceworker.jsp'"
                +    " style='background:#3d7a5c;color:#ffffff;padding:12px 34px;"
                +    "text-decoration:none;font-size:14px;font-weight:700;"
                +    "border-radius:6px;display:inline-block;letter-spacing:0.3px;'>"
                + "View &amp; Start Work"
                + "</a>"
                + "</div>"
 
                + "<p style='font-size:12px;color:#8aaa99;text-align:center;margin-top:14px;'>"
                + "Log in to the JSSL Intranet System to update the ticket status."
                + "</p>"
 
                + "</td>"
                + "</tr>"
 
                // ── Footer ───────────────────────────────────────────────────
                + "<tr>"
                + "<td style='background:#2a5c42;padding:15px 30px;"
                +     "text-align:center;font-size:11px;color:#8ec4ab;'>"
                + "© 2026 JSSL Intranet System &nbsp;·&nbsp; Internal Use Only"
                + "</td>"
                + "</tr>"
 
                + "</table>"
                + "</td></tr></table>"
                + "</body></html>";
 
            helper.setText(content, true);
            mailSender.send(message);
            return 1;
 
        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
   }