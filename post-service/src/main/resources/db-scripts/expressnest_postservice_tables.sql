-- This SQL creates the different tables for the ExpressNest PostService microservice.

CREATE SEQUENCE en_post_id_seq;
CREATE SEQUENCE en_comment_id_seq;
CREATE SEQUENCE en_reaction_id_seq;
CREATE SEQUENCE en_attachment_id_seq;

CREATE TABLE en_users (
    user_id BIGINT PRIMARY KEY,
    username  VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
)

CREATE TABLE en_posts1 (
    post_id BIGINT PRIMARY KEY DEFAULT nextval('en_post_id_seq'),
    user_id BIGINT NOT NULL,
    post_text TEXT NOT NUll,
    privacy VARCHAR(255),
    total_comments BIGINT,
    total_reactions BIGINT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE en_comments (
    comment_id BIGINT PRIMARY KEY DEFAULT nextval('en_comment_id_seq'),
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE en_reactions (
    reaction_id BIGINT PRIMARY KEY DEFAULT nextval('en_reaction_id_seq'),
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    reaction_type VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE en_attachments (
    attachment_id BIGINT PRIMARY KEY DEFAULT nextval('en_attachment_id_seq'),
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    attachment_type VARCHAR(255),
    attachment_ref VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE en_users (
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255)
)