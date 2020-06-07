package io.crypto.beer.telegram.bot.business.instagram.entity;

import com.restfb.Connection;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;
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
public class FacebookSession {

    private String loginUrl;

    //Logged account info
    private User loggedUser;
    private String loggedUserId = "me";
    private String loggedUserPictureUrl;

    //Current account info
    private User currentUser;
    private String currentUserId;
    private String currentUserPictureUrl;

    //Current account photos
    private Connection<Photo> currentUserPhotos;
    private Integer currentUserPhotoIndex;

    //Current account feed
    private Connection<Post> currentUserFeed;
    private Integer currentUserFeedIndex;
}