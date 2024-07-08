create table users(
    id varchar(36) not null primary key,
    email varchar(80) not null,
    password varchar(60) not null,
    created_at datetime(6) not null
);


create table posts(
    tweet_id int not null primary key auto_increment,
    post_content varchar(200) not null,
    id varchar(36) not null,
    posted_at datetime(6) not null
);