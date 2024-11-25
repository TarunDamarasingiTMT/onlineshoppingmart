Online Shopping Cart System

Overview
This document is a model of an online shopping cart system, similar to everyday applications. 
The design implements the DAO (Data Access Object) pattern and the Service Layer pattern, 
applying OOP (Object-Oriented Programming) principles and coding standards.

Features
1. Adding Items to Cart
   Input: userId, itemId, quantity.
   Validations:
   If the entered itemId is invalid or not available, the output will be null.
   If the requested quantity exceeds available stock, an error message stating "Sufficient stock is not available" will be returned.
   Functionality:
   Upon successful addition, the itemId, quantity, and userId will be stored in the Cart table.

2. Removing Items from Cart
   Input: userId, itemId, quantity.
   Validations:
   If the entered userId / itemId is invalid or not available, the output will be null.
   Functionality:
   After entering the itemId, quantity, and userId, the specified items will be removed from the Cart table.

3. Viewing the Cart
   Input: userId.
   Functionality:
   If a valid userId is entered, the system will display the cart contents for that user in the following format:
   itemId | name | quantity | price | subtotal
   The total cost of all items will also be displayed.
   If an invalid userId is entered, the system will return an error message indicating null.

4. Applying Coupon Codes
   Input: UserId, promoCode.
   Validations:
   if we entered wrong userId or entered promo code is invalid, an error message stating "Invalid user Id" / "Invalid promo code" will be returned.
   Functionality:
   If valid, the coupon will be applied to the total cart cost, providing a discount.

5. Checkout Process
   Input: userId.
   Functionality:
   During checkout, items in the cart will be processed. Users can apply a promo code based on their preference.
   The total cost of the items in the cart will be calculated, and the cart will be cleared.
   All relevant data will be stored in the Order History table.

6. Order History
   Input: userId.
   Functionality:
   After checkout, the cart will be empty, and the order details will be stored in the order history.
   Users can retrieve their order history by entering their userId. 

7. Unsaved Items - Saved Cart
   Functionality:
   Before exiting the system, users will be prompted with the question: "Do you want to save the items in your saved cart?"
   If the user responds with "yes," the items will be saved in the saved cart.
   If the user responds with "no," the program will exit immediately.
   If the user enters other than yes/no, the system will return an error message indicating null.
