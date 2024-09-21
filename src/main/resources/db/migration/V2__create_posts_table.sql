create table posts
(
    id int primary key auto_increment,
    title varchar(50) not null,
    content varchar(50) not null,
    author_id int not null,
    foreign key (author_id) references users (id)
);
