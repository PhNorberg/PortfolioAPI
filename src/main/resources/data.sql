insert into user_details(id, email, username) values(1001, 'crypto@gmail.com', 'crypto guy');
insert into user_details(id, email, username) values(1002, 'stonks@gmail.com', 'stonk guy');

insert into portfolio(id, user_id, name) values(2001, 1001, 'Crypto portfolio');
insert into portfolio(id, user_id, name) values(2002, 1002, 'Stock portfolio');

insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, purchase_date, ticker_symbol) values(500, 2001, 20000, 5, 0, DATEADD('YEAR', -1, CURRENT_DATE()), 'BTC');
insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, purchase_date, ticker_symbol) values(501, 2001, 1000, 10, 0, DATEADD('YEAR', -1, CURRENT_DATE()), 'ETH');

insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, purchase_date, ticker_symbol) values(700, 2002, 150, 100, 30, DATEADD('YEAR', -2, CURRENT_DATE()), 'AAPL');
insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, purchase_date, ticker_symbol) values(701, 2002, 100, 200, 0, DATEADD('YEAR', -3, CURRENT_DATE()), 'META');


