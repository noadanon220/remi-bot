package com.danono.remi_bot.controller;

import com.danono.remi_bot.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {

    private final MessageService messageService;

    public WhatsAppController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String handleIncomingMessage(
            @RequestParam("Body") String messageBody,
            @RequestParam("From") String sender
    ) {

        String reply = messageService.processIncomingMessage(sender, messageBody);

        return """
                <Response>
                    <Message>%s</Message>
                </Response>
                """.formatted(escapeXml(reply));
    }

    private String escapeXml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}