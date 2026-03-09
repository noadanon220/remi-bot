package com.danono.remi_bot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-from}")
    private String whatsappFrom;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendWhatsAppMessage(String to, String body) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(whatsappFrom),
                body
        ).create();
    }
}