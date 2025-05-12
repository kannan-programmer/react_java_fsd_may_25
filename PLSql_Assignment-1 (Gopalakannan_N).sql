create database Bookstore;
use Bookstore;

create table book (book_id int auto_increment primary key, title varchar(255) not null,price double not null,author varchar(255) not null,
publication_house varchar(255) not null, category varchar(100) not null ,book_count int,status varchar(50) not null);
    
insert into book (title, price, author, publication_house, category, book_count, status) values
('the last war', 350.00, 'john smith', 'mcgraw hill', 'war', 10, 'in stock'),
('funny bones', 200.00, 'amy adams', 'dreamfolks', 'comedy', 5, 'in stock'),
('sports science', 450.00, 'mike tyson', 'warner bros', 'sports', 7, 'out_of_stock'),
('fictional minds', 300.00, 'lara croft', 'mcgraw hill', 'fiction', 12, 'in stock'),
('epic battles', 500.00, 'bruce wayne', 'dreamfolks', 'war', 3, 'out_of_stock'),
('laugh out loud', 180.00, 'emma stone', 'warner bros', 'comedy', 6, 'in stock'),
('sports trivia', 250.00, 'tony hawk', 'mcgraw hill', 'sports', 8, 'in stock'),
('future fiction', 400.00, 'isaac clarke', 'dreamfolks', 'fiction', 4, 'out_of_stock');

/* procedure 1 to fetch all books in_stock */
delimiter $$
create procedure proc_fetch_instock_books(in max_price double)
begin
    select * from book where status = 'in stock' and price < max_price;
end ;
call proc_fetch_instock_books(300);

/* procedure 2 to delete the book from particular publication */
delimiter $$
create procedure proc_delete_by_publication(in pub_house varchar(255))
begin
    delete from book where publication_house = pub_house;
end;
call proc_delete_by_publication('dreamfolks');

select * from book where publication_house ='dreamfolks';

/* procedure 3 update the price of book based on category */
delimiter $$
create procedure proc_update_price_by_category(in cat varchar(100),in percent_change double)
begin
    update book 
    set price = price + (price * percent_change / 100) where category = cat;
end;
call proc_update_price_by_category('fiction', 10);

select price as increased_price from book where category='fiction';


