**Restaurant.**

There are two roles: _Manager_ and _Customer_.

_Customer_ (authorized user) can order dishes from a menu (dishes catalogue). 
They can also view catalogue sorted by:

* dish name;

* dish price;

* dish category;

Also, the customer is able to filter dishes by their category. Customer can
order the same dish more than once.

_Manager_ administrates the orders: after receiving a new order, manager sends
it for cooking. After cooking, the order is send to delivery. After delivery
and receiving payment, manager updates order status to 'done'.
