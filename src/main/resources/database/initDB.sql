CREATE TABLE IF NOT EXISTS users
(
    id    BIGSERIAL PRIMARY KEY ,
    name  VARCHAR(200) ,
    surname  VARCHAR(200) ,
    age INT8 ,
    email VARCHAR(254) NOT NULL ,
    sex VARCHAR(254),
    description TEXT,
    telegram_tag VARCHAR(254),
    hashed_password VARCHAR(254) NOT NULL ,
    salt VARCHAR(254) NOT NULL
);

CREATE TABLE IF NOT EXISTS likes_events
(
    id    BIGSERIAL PRIMARY KEY ,
    userId BIGSERIAL,
    eventId BIGSERIAL,
    catId BIGSERIAL
    );

CREATE TABLE IF NOT EXISTS likes_users
(
    id BIGSERIAL PRIMARY KEY ,
    liker BIGSERIAL,
    userLikeable BIGSERIAL,
    isLiked BOOLEAN,
    eventId BIGSERIAL
);

