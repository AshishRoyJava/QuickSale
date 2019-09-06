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


