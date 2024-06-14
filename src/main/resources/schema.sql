create table if not exists users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table if not exists authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index if not exists ix_auth_username on authorities (username,authority);

-- had l fichier schema medyour ghi bach tcréé bih des tables f bd ama labghiti t inserer aw update khassk dir
--  fichier akhor smito data.sql tema tadir ga3 les requetes sql
-- JPA hia li tatkalaf bach t executer had les 2 fichiers