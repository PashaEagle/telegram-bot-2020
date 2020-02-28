package io.crypto.beer.telegram.bot.business.instagram.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.util.List;

@Data
@Builder
public class InstagramSession {

    //Main class of instagram library
    private Instagram4j instagram4j;

    //Logged account info
    private String accountName;
    private String password;
    private InstagramLoginResult instagramLoginResult;

    //Found user info
    private InstagramUser instagramUser;
    private boolean currentUserFollowed;
    private List<InstagramUserSummary> userList;
    private Integer currentIndexInUserList;

    //Found post info
    private InstagramFeedItem currentPost;
    private boolean currentPostLiked;
    private List<InstagramFeedItem> postList;
    private Integer currentIndexInPostList;
}