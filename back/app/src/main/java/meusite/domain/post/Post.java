package meusite.domain.post;

import meusite.domain.User.User;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.jpa.UserJpaEntity;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Post {
    private String content;
    private UserJpaEntity user;
    private long tweetId;
    private Instant timeStampTweet;

    public String getContent() {
        return content;
    }

    public Post(String content, UserJpaEntity user, Instant timeStampTweet) {
        this.content = content;
        this.user = user;
        this.timeStampTweet = timeStampTweet;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public Instant getTimeStampTweet() {
        return timeStampTweet;
    }

    public void setTimeStampTweet(Instant timeStampTweet) {
        this.timeStampTweet = timeStampTweet;
    }



}
