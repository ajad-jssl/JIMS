package com.JIMS.integration.interfaces;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    public boolean sendSMS(String mobile, String otp) {

        try {

            String url =
                    "https://www.fast2sms.com/dev/bulkV2"
                    + "?authorization=" + apiKey
                    + "&route=v3"
                    + "&message=Your OTP is " + otp
                    + "&language=english"
                    + "&numbers=" + mobile;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.set("cache-control", "no-cache");

            HttpEntity<String> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            String.class);

            String result = response.getBody();

            System.out.println(result);

            return result != null &&
                   result.contains("success");

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}