package io.crypto.beer.telegram.bot.business.instagram.entity;

import lombok.Builder;
import lombok.Data;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.util.List;

@Data
@Builder
public class InstagramSession {

    private Instagram4j instagram4j;
    private String accountName;
    private String password;

    private InstagramLoginResult instagramLoginResult;
    private InstagramUser instagramUser;
    private Boolean currentUserFollowed;
    private InstagramFeedItem currentPost;
    private List<InstagramUserSummary> userList;
    private Integer currentIndexInUserList;
}