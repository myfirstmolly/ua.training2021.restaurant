create schema if not exists restaurant;
use restaurant;

-- clear previous tables if they exist

drop table if exists requestItem;
drop table if exists dish;
drop table if exists category;
drop table if exists request;
drop table if exists request_status;
drop table if exists user;
drop table if exists user_role;

drop trigger if exists dish_check_price;
drop trigger if exists request_has_dishes_before_insert_update_price;
drop trigger if exists request_items_before_insert_update_price;
drop trigger if exists request_items_before_update;
drop trigger if exists request_items_before_delete;


-- ddl

/**
--------------------------------------------------------------
    USER ROLES
--------------------------------------------------------------
 */
create table if not exists user_role
(
    id   int primary key,
    name varchar(45) unique not null
);

-- since Role is enum, ids for this table must start with 0
insert into user_role
values (0, 'manager'),
       (1, 'customer');


/**
--------------------------------------------------------------
    USER
--------------------------------------------------------------
 */
create table if not exists user
(
    id           int primary key auto_increment,
    username     varchar(16) unique not null,
    password     varchar(60)        not null,
    name         varchar(32)        not null,
    phone_number varchar(14)        not null,
    email        varchar(320)       null,
    role_id      int                not null,
    constraint fk_user_role_id
        foreign key (role_id) references user_role (id)
            on update cascade
            on delete restrict
);

insert into user
values (default, 'manager', 'password', 'rita', '(067)346-31-37', null, 0),
       (default, 'customer', 'password', 'rita', '(067)346-31-37', null, 1);


/**
--------------------------------------------------------------
    REQUEST STATUS
--------------------------------------------------------------
 */
create table if not exists request_status
(
    id          int primary key,
    name        varchar(32) unique not null,
    description varchar(200)       not null
);

-- since Status is enum, ids must start from 0
insert into request_status
values (0, 'Opened', 'Client hasn\'t approved their request yet'),
       (1, 'Cooking', 'Our chef is cooking your order. We will let you know as soon as it\'s done'),
       (2, 'Delivering', 'Your order is being delivered'),
       (3, 'Done', 'Order is done');


/**
--------------------------------------------------------------
    REQUEST
--------------------------------------------------------------
 */
create table if not exists request
(
    id               int primary key auto_increment,
    customer_id      int          not null,
    status_id        int          not null,
    delivery_address varchar(128) not null,
    total_price      int          not null default 0,
    approved_by      int          null     default null,
    created_at       timestamp    not null default current_timestamp,
    updated_at       timestamp    not null default current_timestamp on update current_timestamp,
    constraint fk_request_customer_id
        foreign key (customer_id) references user (id)
            on update cascade
            on delete cascade,
    constraint fk_request_approved_by_user_id
        foreign key (approved_by) references user (id)
            on update cascade
            on delete set null,
    constraint fk_request_status_id
        foreign key (status_id) references request_status (id)
            on update cascade
            on delete restrict
);

/**
--------------------------------------------------------------
    CATEGORY
--------------------------------------------------------------
 */
create table if not exists category
(
    id       int primary key auto_increment,
    name     varchar(32) unique not null,
    name_ukr varchar(32) unique not null
);

insert into category
values (default, 'appetizer', 'закуска'),
       (default, 'salad', 'салат'),
       (default, 'soup', 'суп'),
       (default, 'hot dish', 'гаряча страва'),
       (default, 'pasta', 'паста'),
       (default, 'fish', 'риба'),
       (default, 'dessert', 'десерт'),
       (default, 'drink', 'напій');


/**
--------------------------------------------------------------
    DISH
--------------------------------------------------------------
 */
create table if not exists dish
(
    id              int primary key auto_increment,
    name            varchar(32) unique not null,
    name_ukr        varchar(32) unique not null,
    price           int                not null default 0,
    description     varchar(2048)      null,
    description_ukr varchar(2048)      null,
    image_path      varchar(300)       not null,
    category_id     int                not null,
    constraint fk_dish_has_category_category_id
        foreign key (category_id) references category (id)
            on update cascade
            on delete restrict
);

-- trigger to check if dish price is less than zero.
-- if price is less than zero, than it's set to 0.
delimiter //
create trigger dish_check_price
    before insert
    on dish
    for each row
begin
    if new.price < 0 then
        set new.price = 0;
    end if;
end;
// delimiter ;


/**
--------------------------------------------------------------
    REQUEST ITEM
--------------------------------------------------------------
 */
create table if not exists request_item
(
    id         int primary key auto_increment,
    request_id int,
    dish_id    int,
    quantity   int unsigned not null default 1,
    price      int          not null,
    created_at timestamp    not null default current_timestamp,
    unique (request_id, dish_id),
    constraint fk_order_has_dishes_request_id
        foreign key (request_id) references request (id)
            on update cascade
            on delete cascade,
    constraint fk_order_has_dishes_dish_id
        foreign key (dish_id) references dish (id)
            on update cascade
            on delete restrict
);


-- after inserting to table request_item, total price of request
-- will be recalculated.
delimiter //
create trigger request_items_before_insert_update_price
    before insert
    on request_item
    for each row
begin
    declare pr int;
    select price into pr from dish where id = new.dish_id;
    update request set total_price = total_price + pr * new.quantity where id = new.request_id;
    set new.price = pr;
end;
// delimiter ;


-- after updating any row in table request_item, total price of request
-- will be recalculated.
delimiter //
create trigger request_items_before_update
    before update
    on request_item
    for each row
begin
    declare pr int;
    select price into pr from dish where id = new.dish_id;
    if new.quantity >= old.quantity
    then
        update request
        set total_price = total_price + pr * (new.quantity - old.quantity)
        where id = new.request_id;
    else
        update request
        set total_price = total_price - pr * (old.quantity - new.quantity)
        where id = new.request_id;
    end if;
    set new.price = pr;
end;
// delimiter ;


-- after deleting any row in table request_item, total price of request
-- will be recalculated.
delimiter //
create trigger request_items_before_delete
    before delete
    on request_item
    for each row
begin
    declare pr int;
    select price into pr from dish where id = old.dish_id;
    update request
    set total_price = total_price - pr * old.quantity
    where id = old.request_id;
end;
// delimiter ;


delimiter //
create trigger request_before_update
    before update
    on request
    for each row
begin
    if NEW.status_id <= OLD.status_id
    then
        set NEW.status_id = OLD.status_id;
    end if;
end;
// delimiter ;