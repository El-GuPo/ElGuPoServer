CREATE TABLE IF NOT EXISTS users
(
    id    BIGSERIAL PRIMARY KEY ,
    name  VARCHAR(200) ,
    surname  VARCHAR(200) ,
    age INT8 ,
    email VARCHAR(254) NOT NULL ,
    hashed_password VARCHAR(254) NOT NULL ,
    salt VARCHAR(254) NOT NULL
);