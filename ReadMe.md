Online Shopping cart System

A Featured Model of Online Shopping cart similar to daily life based apps.

While designing, we implement DAO Pattern & Service layer pattern with applying oops principles & coding standards.


features :- 

Adding items to Cart
input (main) - itemId, quantity

validations : - 
if entered itemId is wrong/not available then it throws error as null
if we entered more quantity then stock, then it throws out with message sufficient stock is not avaiable

functionality:-

after adding itemid, quantity, userid the data will store in Cart table

Remove Items from Cart 

validations : -
if entered itemId is wrong/not available then it throws error as null
functionality:-

after entering itemId, quantity, userid then from data will we can remove items in Cart table

View the Cart

if we enter userId, then Data of Cart of that user will be shown as 
item Id | name | quantity | price | subtotal 
along with that, total items cost will be displayed as total cost.
if we entered wrong id, then we will get error message as null

Apply the CouponCode

we have some coupon codes in database,based on that we will get discount.
if we entered wrong input as promo code, we will get error message as invalid promo code

CheckOut of Cart
In checkout process we have to enter userId, then the items of cart presented cart were goes under checkout process, again there will be promo code applied based on user interest
later, we will get total cost of items in cart & cart will cleared, later data posted to order history.
(clearing the Items in cart)

Order History  

after completion of checkout, the cart will be empty & the data will stored in order history table 
we can retrive the Order history of user by entering userId

unsaved items - SavedCart
before exit the system, it will ask are you want to save the items in saved cart if yes, the items will save into saved cart & exit,if we enter no it will exit the program immediately.



