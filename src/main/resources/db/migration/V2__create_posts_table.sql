create table posts
(
    id int primary key auto_increment,
    title varchar(255) not null,
    content varchar(255) not null,
    author_id int not null,
    foreign key (author_id) references users (id)
);
