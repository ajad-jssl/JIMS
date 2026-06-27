package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/pushapi")
public class SmsController {

    @GetMapping("/sendmsg")
    public Map<String, Object> sendMsg(
            @RequestParam String username,
            @RequestParam String dest,
            @RequestParam String apikey,
            @RequestParam String signature,
            @RequestParam String msgtype,
            @RequestParam("msgtxt") String msgText,
            @RequestParam(required = false) String custref,
            @RequestParam(required = false) String entityid,
            @RequestParam(required = false) String templateid,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String converturl,
            @RequestParam(required = false) String campaign
    ) {

        Map<String, Object> response = new LinkedHashMap<>();

        // simulate request id
        String reqId = UUID.randomUUID().toString().replace("-", "");

        response.put("code", "6001");
        response.put("desc", "Message received by platform.");
        response.put("reqId", reqId);
        response.put("time", LocalDateTime.now().toString());

        if (custref != null) {
            response.put("custRef", custref);
        }

        response.put("partMessageIds", List.of(reqId));
        response.put("totalMessageParts", 1);

        if (campaign != null) {
            response.put("campaignName", campaign);
        }

        // optional: echo input (useful for Postman testing)
        Map<String, Object> requestEcho = new HashMap<>();
        requestEcho.put("username", username);
        requestEcho.put("dest", dest);
        requestEcho.put("apikey", apikey);
        requestEcho.put("signature", signature);
        requestEcho.put("msgtype", msgtype);
        requestEcho.put("msgtxt", msgText);
        requestEcho.put("entityid", entityid);
        requestEcho.put("templateid", templateid);
        requestEcho.put("domain", domain);
        requestEcho.put("converturl", converturl);

        response.put("request", requestEcho);

        return response;
    }
}