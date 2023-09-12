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
    date_time timestamp with time zone,
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

insert into transporter(title, phone_number) values('transporter1', '11111111111');
insert into transporter(title, phone_number) values('transporter2', '22222222222');
insert into transporter(title, phone_number) values('transporter3', '33333333333');
insert into transporter(title, phone_number) values('transporter4', '44444444444');
insert into transporter(title, phone_number) values('transporter5', '55555555555');
insert into transporter(title, phone_number) values('transporter6', '66666666666');
insert into transporter(title, phone_number) values('transporter7', '77777777777');
insert into transporter(title, phone_number) values('transporter8', '88888888888');
insert into transporter(title, phone_number) values('transporter9', '99999999999');

insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure1', 'destination1', 100, 1);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure2', 'destination2', 200, 2);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure3', 'destination3', 300, 3);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure4', 'destination4', 400, 4);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure5', 'destination5', 500, 5);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure6', 'destination6', 600, 6);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure7', 'destination6', 700, 7);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure8', 'destination6', 800, 8);
insert into route(departure_point, destination_point, travel_time, transporter_id) values('departure9', 'destination6', 900, 9);

insert into ticket(route, date_time, place_number, price) values(1, '2023.09.20 15:00', 3, 111);
insert into ticket(route, date_time, place_number, price) values(2, '2023.09.20 16:00', 4, 222);
insert into ticket(route, date_time, place_number, price) values(3, '2023.09.20 17:00', 5, 333);
insert into ticket(route, date_time, place_number, price) values(4, '2023.09.20 18:00', 6, 444);
insert into ticket(route, date_time, place_number, price) values(5, '2023.09.20 19:00', 7, 555);
insert into ticket(route, date_time, place_number, price) values(6, '2023.09.20 20:00', 8, 666);
insert into ticket(route, date_time, place_number, price) values(7, '2023.09.20 21:00', 9, 777);
insert into ticket(route, date_time, place_number, price) values(8, '2023.09.20 22:00', 10, 888);
insert into ticket(route, date_time, place_number, price) values(9, '2023.09.20 23:00', 11, 999);