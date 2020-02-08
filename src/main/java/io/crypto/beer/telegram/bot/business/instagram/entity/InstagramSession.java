package io.crypto.beer.telegram.bot.business.instagram.entity;

import lombok.Builder;
import lombok.Data;
import org.brunocvcunha.instagram4j.Instagram4j;

@Data
@Builder
public class InstagramSession {

    private Instagram4j instagram4j;
    private String accountName;
    private String password;
    private String confirmationUrl;
}