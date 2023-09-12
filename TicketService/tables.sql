create table transporter(
    id int primary key generated by default as identity,
    title varchar(30) not null,
    phone_number varchar(20) not null unique 
);

create table route(
    id int primary key generated by default as identity,
    departure_point varchar(30) not null,
    destination_point varchar(30) not null,
    travel_time int,
    transporter_id int references transporter(id) on delete set null 
);

create table person(
    id int primary key generated by default as identity,
    initials varchar(30) not null,
    username varchar(30) unique not null,
    password varchar(30) not null,
    role varchar(10) not null
);

create table ticket(
    id int primary key generated by default as identity,
    route int references route(id) on delete set null,
    date_time timestamp,
    place_number int not null,
    price int not null,
    reserved_by int default null references person(id) on delete set null
);

create table token(
    id int primary key generated by default as identity,
    token varchar(50) not null unique,
    expiry_date timestamp not null,
    user_id int references person(id) on delete cascade
);