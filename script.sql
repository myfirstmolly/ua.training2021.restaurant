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
    if new.quantity > old.quantity
    then
        update request
        set total_price = total_price + pr * (new.quantity - old.quantity)
        where id = new.request_id;
    else
        if NEW.quantity < OLD.quantity then
            update request
            set total_price = total_price - pr * (old.quantity - new.quantity)
            where id = new.request_id;
        end if;
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


delimiter //
create trigger request_itm_before_update_qty_2
    before update
    on request_item
    for each row
begin
    if NEW.quantity > 30
    then
        set new.quantity = 30;
    end if;
end;
// delimiter ;


drop procedure if exists insert_order_item;
DELIMITER //
create procedure insert_order_item(in user_param int,
                                   in dish_param int)
begin
    declare order_param_id int;
    declare item_param int;
    declare qty int;
    declare order_qty int;
    declare item_qty int;
    set order_qty = (select count(*) from request where customer_id = user_param and status_id = 1);
    set item_param = null;

    if order_qty = 0
    then
        insert into request(customer_id, status_id) values (user_param, 1);
        set order_param_id = (select LAST_INSERT_ID());
    else
        set order_param_id = (select id from request where customer_id = user_param and status_id = 1);
    end if;

    set item_qty = (select count(*) from request_item where dish_id = dish_param and request_id = order_param_id);
    if item_qty = 0
    then
        insert into request_item(request_id, dish_id, quantity) values (order_param_id, dish_param, 1);
        set item_param = (select LAST_INSERT_ID());
        set qty = 1;
    else
        select id, quantity
        into item_param, qty
        from request_item
        where dish_id = dish_param
          and request_id = order_param_id;
        update request_item set quantity = qty + 1 where id = item_param;
    end if;
end//
DELIMITER ;


-- tables for deleted data
create table if not exists deleted_user
(
    id           int primary key,
    username     varchar(16)  not null,
    password     varchar(60)  not null,
    name         varchar(32)  not null,
    phone_number varchar(14)  not null,
    email        varchar(320) null,
    role_id      int          not null,
    constraint fk_deleted_user_role_id
        foreign key (role_id) references user_role (id)
            on update cascade
            on delete restrict
);


create table if not exists deleted_dish
(
    id              int primary key,
    name            varchar(32) unique not null,
    name_ukr        varchar(32) unique not null,
    price           int                not null default 0,
    description     varchar(2048)      null,
    description_ukr varchar(2048)      null,
    image_path      varchar(300)       not null,
    category_id     int                not null,
    constraint fk_deleted_dish_has_category_category_id
        foreign key (category_id) references category (id)
            on update cascade
            on delete restrict
);

delimiter //
create trigger user_before_delete
    before delete
    on user
    for each row
begin
    insert into deleted_user(id, username, password, name, phone_number, email, role_id)
    values (old.id, old.username, old.password, old.name, old.phone_number, old.email, old.role_id);
end;
// delimiter ;


delimiter //
create trigger dish_before_delete
    before delete
    on dish
    for each row
begin
    insert into dish(id, name, name_ukr, price, description, description_ukr, image_path, category_id)
    values (old.id, old.name, old.name_ukr, old.price, old.description, old.description_ukr, old.image_path,
            old.category_id);
end;
// delimiter ;


delimiter //
create trigger dish_before_update
    before update
    on dish
    for each row
begin
    update request_item ri join request r on ri.request_id = r.id
    set ri.price = new.price
    where r.status_id = 0
      and ri.dish_id = new.id;
    if new.price > old.price
    then
        update request join request_item i on request.id = i.request_id
        set total_price = total_price + i.quantity * (new.price - old.price)
        where i.dish_id = new.id
          and request.status_id = 0;
    else
        if NEW.price < OLD.price then
            update request join request_item i on request.id = i.request_id
            set total_price = total_price - i.quantity * (old.price - new.price)
            where i.dish_id = new.id
              and request.status_id = 0;
        end if;
    end if;
end;
// delimiter ;