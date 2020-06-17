package io.crypto.beer.telegram.bot.business.action.instagram.profile;

import io.crypto.beer.telegram.bot.business.action.facebook.configuration.FacebookConfig;
import io.crypto.beer.telegram.bot.business.constant.KeyboardPath;
import io.crypto.beer.telegram.bot.engine.entity.Message;
import io.crypto.beer.telegram.bot.engine.services.ResourceHandlerService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeePostCommentsActions {

    static Instagram4j instagram4j;

    public static void comment(Message m, ApplicationContext ctx) {
        log.info("Call SeePostCommentsActions method comment");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        String comment = m.getSession().getInputData();
        InstagramFeedItem post = m.getSession().getInstagramSession().getCurrentPost();
        InstagramUser me = new InstagramUser();
        try {
            instagram4j.sendRequest(new InstagramPostCommentRequest(post.getPk(), comment));
            List<InstagramComment> comments = m.getSession().getInstagramSession().getCurrentPost().getPreview_comments();
            InstagramComment myNewComment = new InstagramComment();
            myNewComment.setText(comment);
            me.setUsername(m.getSession().getInstagramSession().getAccountName());
            myNewComment.setUser(me);
            comments.add(myNewComment);
            post.setPreview_comments(comments);
            m.getSession().getInstagramSession().setCurrentPost(post);

            Integer index = m.getSession().getInstagramSession().getCurrentIndexInPostList();
            m.getSession().getInstagramSession().getPostList().set(index, post);
        } catch (IOException e) {
            System.out.println("Error while commenting post. Error: " + e.getMessage());
            e.printStackTrace();
        }

        ResourceHandlerService.fillMessageConfig(m.getSession(), String.format("%s%s",
                FacebookConfig.keyboardBasePath,
                KeyboardPath.SEE_POSTS_VIEW.getPath()));
    }

    public static void commentPostByHashtag(Message m, ApplicationContext ctx) {
        log.info("Call SeePostCommentsActions method commentPostByHashtag");
        instagram4j = m.getSession().getInstagramSession().getInstagram4j();

        String comment = m.getSession().getInputData();
        InstagramFeedItem post = m.getSession().getInstagramSession().getCurrentPost();
        InstagramUser me = new InstagramUser();

        try {
            instagram4j.sendRequest(new InstagramPostCommentRequest(post.getPk(), comment));
            List<InstagramComment> comments = m.getSession().getInstagramSession().getCurrentPost().getPreview_comments();
            InstagramComment myNewComment = new InstagramComment();
            myNewComment.setText(comment);
            me.setUsername(m.getSession().getInstagramSession().getAccountName());
            myNewComment.setUser(me);
            comments.add(myNewComment);
            post.setPreview_comments(comments);
            m.getSession().getInstagramSession().setCurrentPost(post);
        } catch (IOException e) {
            System.out.println("Error while commenting post. Error: " + e.getMessage());
            e.printStackTrace();
        }

        ResourceHandlerService.fillMessageConfig(m.getSession(), String.format("%s%s",
                FacebookConfig.keyboardBasePath,
                KeyboardPath.FIND_POST_VIEW.getPath()));
    }

}