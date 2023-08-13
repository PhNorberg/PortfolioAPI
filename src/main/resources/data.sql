SELECT 1; -- Dummy Query, else data.sql empty and app cannot run

-- insert into user_details(id, email, username) values(1001, 'crypto@gmail.com', 'crypto guy');
-- insert into user_details(id, email, username) values(1002, 'stonks@gmail.com', 'stonk guy');
--
-- insert into portfolio(id, user_id, name) values(2001, 1001, 'Crypto portfolio');
-- insert into portfolio(id, user_id, name) values(2002, 1002, 'Stock portfolio');
--
-- insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, ticker_symbol, asset_type) values(500, 2001, 20000, 5, 0,  'BTC', 'crypto');
-- insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, ticker_symbol, asset_type) values(501, 2001, 1000, 10, 0,  'ETH', 'crypto');
--
-- insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, ticker_symbol, asset_type) values(700, 2002, 150, 100, 30,  'AAPL', 'stocks');
-- insert into asset(id, portfolio_id, purchase_price, quantity, tax_rate, ticker_symbol, asset_type) values(701, 2002, 100, 200, 0,  'META', 'stocks');
--
--
-- insert into transaction(id, transaction_type, quantity, purchase_date, purchase_price, asset_id) values(50, 'buy', 5, DATEADD('YEAR', -1, CURRENT_DATE()), 20000, 500);
-- insert into transaction(id, transaction_type, quantity, purchase_date, purchase_price, asset_id) values(51, 'buy', 10, DATEADD('YEAR', -1, CURRENT_DATE()), 1000, 501);
--
-- insert into transaction(id, transaction_type, quantity, purchase_date, purchase_price, asset_id) values(70, 'buy', 100, DATEADD('YEAR', -1, CURRENT_DATE()), 150, 700);
-- insert into transaction(id, transaction_type, quantity, purchase_date, purchase_price, asset_id) values(71, 'buy', 200, DATEADD('YEAR', -1, CURRENT_DATE()), 100, 701);
