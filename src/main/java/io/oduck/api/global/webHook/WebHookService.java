package io.oduck.api.global.webHook;

import static io.oduck.api.global.utils.HttpHeaderUtils.getClientIP;

import io.oduck.api.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.Color;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebHookService {
    @Value("${config.webhook.url}")
    private String url;

    public void sendMsg(Exception e, HttpServletRequest request) {
        DiscordWebhook webhook = new DiscordWebhook(url);

        String clientIP = getClientIP(request);
        String requestURL = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        String requestTime = new Date().toString();
        String requestUserAgent = request.getHeader("User-Agent");

        StackTraceElement[] stackTrace = e.getStackTrace();
        String stackTraceInfo = null;
        if (stackTrace.length > 0) {
            StackTraceElement firstElement = stackTrace[0];
            stackTraceInfo = firstElement.getClassName() + "." + firstElement.getMethodName() + "(" + firstElement.getFileName() + ":" + firstElement.getLineNumber() + ")";
        }

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
            .setTitle("** Error Stack **")
            .setDescription(e.getMessage())
            .setColor(new Color(16711680))
            .addField("HTTP_METHOD", requestMethod, false)
            .addField("REQUEST_ENDPOINT", requestURL, false)
            .addField("CLIENT_IP", clientIP, false)
            .addField("ERROR_STACK", stackTraceInfo, false)
            .addField("TIME", requestTime, false)
            .addField("USER_AGENT", requestUserAgent, false));

        try{
            webhook.execute();
        } catch (IOException exception) {
            throw new CustomException("Discord WebHook Error");
        }
    }
}