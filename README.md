# QuickSale - A Simple Spring Boot Application simulating flash sale

It is a spring boot, maven project.
To check the source code, download the application from github using the link https://github.com/AshishRoyJava/QuickSale.git

To run the application import the project as maven project, install and run.

Alternatively, The executable jar file is also provided at the root. 
It can be executed using "java -jar" command.

The application uses embedded h2 database. So, no need to configure any databases.

The application is now ready for testing.

# APIs 

GET - http://localhost:8080/users/send-registration-invite  - send registration invite email to all the users available in the database.

POST - http://localhost:8080/registration/for-sale  - user registers for a product using this api.
It takes a JSON as a body parameter. It is as follows - 
{
	"userId" : (user's id),
	"productId" : (product's id)
}

POST - http://localhost:8080/purchases/new  - the user places purchase order using this api. It also takes a JSON as a body parameter. It is the same body parameter as the registration.

GET - http://localhost:8080/purchases/all  - lists all the orders made, provides details of the users and the products.

# ER Diagram 

![ERD](https://user-images.githubusercontent.com/19311770/64603523-31032180-d3de-11e9-8b97-1c72d84833ec.png)


# Business Logic

For this flash-sale application concurrency is important as multiple request are expected in a short time. So, a thread-pool has been configured and most of the process are handled asynchronously.

When the application starts it initializes with 5 users and 2 products with quantity 2 per product in the inventory

At the start the number of items available in the inventory per product is fetched and added to ServletContext for atomic counting.

When the registration invite endpoint is executed it fetches all the users available and send emails to them asynchronously.

When the user registers for the sale, then also he receives a confirmation email.

NOTE: There is a time limit for registration before the sale starts. It can be configured in the application.properties file. The time of the start of the sale is also congiurable at the same place.

Only an user who has registered for a particular product can place a purchase order. There is no limit on how many products the user can purchase but can purchase only 1 unit per product. The user needs to register for each product he wishes to buy separately.

When the user places the purchase order, the inventory details are fetched for servlet context for faster processing. If there is at least 1 unit available then it proceeds to place the order, otherwise shows out of stock. At the same time the stock on the servlet context is also updated. This is an atomic operation. But processing the actual order takes place in a different thread. This is done in this way to make the user experince faster and to avoid any race condition, so that the products do not oversell or undersell.

When the order is successful the user will receive a confirmation email.

# Test Cases

JUnit test cases are also added and most of the scenarios are covered.

# Development Branch

Updated development branch to create pull request for travis CI validation



