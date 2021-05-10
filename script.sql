create schema if not exists restaurant;
use restaurant;

-- clear prev tables

drop table if exists bill;
drop table if exists dish;
drop table if exists category;
drop table if exists request;
drop table if exists request_status;
drop table if exists user;
drop table if exists user_role;

drop trigger if exists dish_check_price;
drop trigger if exists request_has_dishes_before_insert_update_price;


-- ddl

create table if not exists user_role (
	id int primary key,
	name varchar(45) unique not null
);

insert into user_role
	values (0, 'manager'), (1, 'customer');

create table if not exists user (
	id int primary key auto_increment,
	username varchar(16) unique not null,
    password varchar(60) not null,
    name varchar(32) not null,
    phone_number varchar(14) not null,
    email varchar(320) null,
    role_id int not null,
    constraint fk_user_role_id
		foreign key(role_id) references user_role(id)
        on update cascade
        on delete restrict
);

insert into user values 
	(default, 'manager', 'password', 'rita', '(067)346-31-37', null, 0),
	(default, 'customer', 'password', 'rita', '(067)346-31-37', null, 1);

create table if not exists request_status (
	id int primary key,
    name_eng varchar(32) unique not null,
    name_ukr varchar(32) unique not null,
    description_eng varchar(2048) not null,
    description_ukr varchar(2048) not null
);

insert into request_status values
	(0, 'Opened', 'Відкрито', 
    'Client hasn\'t approved their request yet', 'Клієнт ще не підтвердив замовлення'),
    (1, 'Cooking', 'Готується',
    'Our chef is cooking your order. We will let you know as soon as it\'s done',
    'Наш повар вже готує Ваше замовлення!'),
    (2, 'Delivering', 'Доставляється', 
    'Your order is being delivered', 'Ваше замовлення вже прямує до Вас'),
    (3, 'Done', 'Виконано', 
    'Order is done', 'Замовлення виконано');
    

create table if not exists request (
	id int primary key auto_increment,
    customer_id int not null,
    status_id int not null,
    delivery_address varchar(128) not null,
    total_price int not null default 0,
    approved_by int null default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    constraint fk_request_customer_id
		foreign key(customer_id) references user(id)
        on update cascade
        on delete cascade,
	constraint fk_request_approved_by_user_id
		foreign key(customer_id) references user(id)
        on update cascade
        on delete cascade,
	constraint fk_request_status_id
		foreign key(status_id) references request_status(id)
        on update cascade
        on delete restrict
);

insert into request values
	(default, 2, 1, 'bazhana 7b', default, default, default, default);

create table if not exists category (
	id int primary key auto_increment,
    name_eng varchar(32) unique not null,
    name_ukr varchar(32) unique not null
);

insert into category values 
	(default, 'appetizer', 'закуска'),
    (default, 'salad', 'салат'),
    (default, 'soup', 'суп'),
    (default, 'hot dish', 'тепла страва'),
	(default, 'pasta', 'паста'),
	(default, 'fish', 'риба'),
    (default, 'dessert', 'десерт'),
    (default, 'drink', 'напій');


create table if not exists dish (
	id int primary key auto_increment,
    name_eng varchar(32) unique not null,
    name_ukr varchar(32) unique not null,
    price int not null default 0,
    description_eng varchar(2048) null,
    description_ukr varchar(2048) null,
    image_path varchar(100) not null,
	category_id int not null,
	constraint fk_dish_has_category_category_id
		foreign key(category_id) references category(id)
        on update cascade
        on delete restrict
);

delimiter //
create trigger dish_check_price before insert on dish 
for each row
begin
	if new.price < 0 then set new.price = 0;
    end if;
end;
// delimiter ;

insert into dish values 
	(default, 'Quesadilla', 'Кесадилья', '8000', 
    'A Mexican cuisine dish, consisting of a tortilla that is filled primarily with cheese, and sometimes meats, spices, and other fillings, and then cooked on a griddle or stove. Traditionally, a corn tortilla is used, but it can also be made with a flour tortilla.',
    'Cтрава мексиканської кухні, яку готують переважно із складеної кукурудзяної або пшеничної тортильї із начинкою з розплавленого сиру',
    'quesadilla.jpg', 4);
    

create table if not exists bill (
    request_id int,
    dish_id int,
    quantity int unsigned not null default 1,
    price int not null,
    created_at timestamp not null default current_timestamp,
    primary key(request_id, dish_id),
    constraint fk_order_has_dishes_request_id
		foreign key(request_id) references request(id)
        on update cascade
        on delete cascade,
    constraint fk_order_has_dishes_dish_id
		foreign key(dish_id) references dish(id)
        on update cascade
        on delete restrict
);

delimiter //
create trigger bill_before_insert_update_price before insert on bill 
for each row
begin
	declare pr int;
    select price into pr from dish where id = new.dish_id;
	update request set total_price = total_price + pr * new.quantity where id = new.request_id;
	set new.price = pr;
end;
// delimiter ;
    
insert into bill values
	(1, 1, 2, null, default);