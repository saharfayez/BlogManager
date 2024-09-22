create table users
(
    id int primary key auto_increment,
    user_name varchar(50)  unique not null,
    password varchar(255) not null
);