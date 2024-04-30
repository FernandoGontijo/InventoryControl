# InventoryControl

This project was developed for the QikServe selection process. To run it, you need to run the start.sh script inside the wiremock folder in the root of the project and then run the project.

When initializing the project, it will consume data from the wiremocks to populate the database with product and promotion information. From there, the API endpoints will be available to perform CRUD (Create, Read, Update and Delete) actions on products, promotions, customers and cart. Through the endpoint *POST - /cart/{cart_id}/addProduct* it will be possible to add products to the cart, and if the product has promotions, they will be applied.


# Follow up questions
1. How long did you spend on the test? What would you add if you had more time?

   
   I spent approximately 10 hours working on this solution. If I had more time, I would have added more unit tests and implemented more efficient error handling.


2. What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that shows how you've used it.

A powerful feature that was added was Records. With it, I can create simple immutable data objects concisely. Here's an example of how I used records in the project.

<img width="817" alt="Screenshot 2024-04-30 at 13 04 10" src="https://github.com/FernandoGontijo/InventoryControl/assets/16126146/a5c3151b-5086-42d1-b768-1f06e5f9c7fd">

<img width="619" alt="Screenshot 2024-04-30 at 13 05 03" src="https://github.com/FernandoGontijo/InventoryControl/assets/16126146/fbaa6811-be8d-45eb-b069-5507e9f20e75">



3. What did you find most difficult?

The solution itself wasn't difficult, the biggest problem was adapting the "rules" of promotions to a more generic context. For example, in the QTY_BASED_PRICE_OVERRIDE promotion, two pizzas would have a value of 1799 and not 2198, which would be the value of two pizzas without the promotion. But if the customer purchased 3 or more pizzas, what would be the value of the pizzas with the promotion?

Well, in this case I chose to use the 36.30% discount value for the QTY_BASED_PRICE_OVERRIDE promotion. It's not a rule that is explicit in the wiremock object but it was what made the most sense.


4. What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you
   could do.

I used the Log4j2 logging framework to have visibility into the main application flows. Furthermore, in a production context I would use monitoring tools like Grafana and Spring Boot Actuator to have information about the performance and health of the application.


5. The Wiremock represents one source of information. We should be prepared to integrate with more sources. List the steps that we would need to take to add more sources of items with diferent formats and promotions.
   To add a new data source, you first need to define the data model to represent the products and promotions. If the current models meet the needs of the new data source, this step will not be necessary.

Next, specific code must be implemented to fetch the data from the new source, convert it to the model that will be used and finally save it in the database.

It is also possible to add new data through the endpoints provided by the API.
