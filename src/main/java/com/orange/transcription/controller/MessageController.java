package com.orange.transcription.controller;

import com.orange.transcription.dtos.Message;
import com.orange.transcription.dtos.ResponseMessage;
import com.orange.transcription.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;


@Controller
public class MessageController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/private-message")
    @SendToUser("/queue/private-messages")
    public ResponseMessage getPrivateMessage(final Message message,
                                             final Principal principal,
                                             @Header("simpSessionId") String sessionId) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape(
                "Sending private message to user " + principal.getName() + ": "
                        + message.getMessageContent())
        );
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
