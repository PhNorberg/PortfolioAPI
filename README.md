# Portfolio Tracker API
A portfolio tracker API written in Java with the Spring Framework, deployed on AWS.
## Introduction

This portfolio tracker lets you track your stock and/or crypto portfolio performance in real-time. Users can create several portfolios for themselves and add their favourite assets to them.

## How to get started 

First off, you need to register an account and log in.

Head over to http://portfolioapi-prod.eba-fapp3vct.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html#/

### Register

<img width="1026" alt="Screenshot 2023-08-22 at 14 32 10" src="https://github.com/PhNorberg/PortfolioAPI/assets/94229361/1f1794ce-4f10-44aa-89f8-08481d1e3a78">

When you've clicked the link, you're met with this UI. This is Swagger UI, which lets you fire requests to all the available endpoints for the API.
To register, click on the `POST /auth/register` endpoint.



<img width="1008" alt="Screenshot 2023-08-22 at 14 40 10" src="https://github.com/PhNorberg/PortfolioAPI/assets/94229361/b227db74-8a90-4b19-8da0-c5514c91fcbc">

When you've clicked, the endpoint information box is expanded and you're met with the next view.
This is where you fire your requests. Before firing, this view gives you information about what the endpoint wants and what it will give you.

**Parameters** - This tells you what parameter from the endpoint URL the request expects from you.

**Request Body** - This tells you what it expects your request body to look like. In this instance it expects a username and a password, both being of type String. If you click
the "Schema" button, detailed information about the expected **Request Body** is shown. Here are things like size and pattern constraints shown.

**Responses** - If all goes well, this is what you can expect back from the server. In the best case, a code 200 and a message.

Now, click **Try it out**, write your first request in the new view and then click **Execute**.

If all went well, you should've been met with the message '*User registered successfully*'.

Great, now you can master the Swagger UI since the interaction process is the same across all endpoints.

### Login

When logging in, we use JWT authentication.

To login:

* Click the `POST /auth/login` endpoint
* Click the **Try it out** button
* Fill in your user credentials in the request body and hit **Execute**
* Copy the token value (without quotations) from the **Response**
* Scroll up to the green **Authorization** button on the right hand side
* Click it, paste your token into the **Value** field and click **Authorize**

Congratulations, you're in!

The token expires after 1 hour, so remember to log in and provide the response token to the **Authorization** button when you come back the next time.

### Create your first portfolio, add an asset to it and add a transaction to the asset

* Click the `POST /users/{username}/portfolios` endpoint under the **portfolio-controller**
* Make a request with your username as **Parameters** and your new portfolios name as **Request body**
* Click the `POST /users/{username}/portfolios/{portfolioname}/assets` endpoint under the **asset-controller**
* Make a request with your username and portfolio name as **Parameters** and the asset details in **Request body**. ***Make sure to input a valid ticker symbol,
  and asset type should either be "crypto" or "stock".***
* Click the `POST /users/{username}/portfolios/{portfolioname}/{tickersymbol}/transactions` endpoint under the **transaction-controller**
* Make a request with your username, portfolioname and ticker symbol as **Parameters** and the transaction details in **Request body**

Now you've made your first portfolio that you can track!


## Endpoints

Following text will explain the various endpoints that you can navigate through the API. Note that this is still a work in progress and more endpoints and functionality will pop up over time. Stay tuned:)

### portfolio-controller (Authentication required)

| METHOD| ENDPOINT| DESCRIPTION                |
|-------|--------|----------------------------|
|`GET`| `users/{username}/portfolios` | Gets a list of all portfolios owned by a specific user, showing only portfolio name and total value |
|`POST`| `user/{username}/portfolios` | Creates a new portfolio owned by you |
| `GET`      | `users/{username}/portfolios/{portfolioname}` | Gets a detailed view of a specific portfolio owned by a user   |
| `DELETE` |`users/{username}/portfolios/{portfolioname}` | Deletes a specific portfolio that you own |

### transaction-controller (Authentication required)

| METHOD| ENDPOINT | DESCRIPTION                |
|-------|--------|----------------------------|
|`POST` | `users/{username}/portfolios/{portfolioname}/{tickersymbol}/transactions` | Creates a transaction for the specific asset you own |

### asset-controller (Authentication required)

| METHOD| ENDPOINT| DESCRIPTION      |
|-------|--------|----------------------------|
| `POST` | `/users/{username}/portfolios/{portfolioname}/assets` | Creates and adds a new asset to the specific portfolio you own |
| `DELETE` | `/users/{username}/portfolios/{portfolioname}/{tickersymbol}` | Deletes an asset from a specific portfolio you own |

### auth-controller (No authentication required)

| METHOD| ENDPOINT| DESCRIPTION      |
|-------|--------|----------------------------|
| `POST` | `/auth/register` | Registers a new user to the service |
| `POST` | `/auth/login` | Where a user logs in to the service |

## user-controller (Authentication required)

| METHOD| ENDPOINT| DESCRIPTION      |
|-------|--------|----------------------------|
| `GET` | `/users` | Gets a list of all users and the names of their portfolios |
| `GET` | `/users/{username}` | Gets user info and a list of all portfolios owned by a specific user, showing only portfolio name and total value |
| `DELETE`| `/users/{username}` | Deletes your user from the application |


