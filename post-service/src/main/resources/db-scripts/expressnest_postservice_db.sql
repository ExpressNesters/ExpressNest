-- This SQL creates the database for the ExpressNest PostService microservice.

drop database expressnestpostdb;
drop user expressnestpostdb_user;

create role expressnestpostdb_user with password 'password';
create database expressnestpostdb with template=template0 owner=expressnestpostdb_user;

\connect expressnestpostdb;

alter default privileges grant all on tables to expressnestpostdb_user;
alter default privileges grant all on sequences to expressnestpostdb_user;