CREATE TABLE IF NOT EXISTS users
(
  id serial primary key,
  username varchar(50) unique not null,
  password varchar(50) not null,
  email varchar(255) unique not null
);

CREATE TABLE IF NOT EXISTS sentence_str
(
  id serial primary key,
  greeting_str varchar(50) unique not null,
  cheer_phrase varchar(1000) not null,
  ins_date timestamp with time zone not null,
  update_date timestamp with time zone not null,
  del_flag boolean not null
);