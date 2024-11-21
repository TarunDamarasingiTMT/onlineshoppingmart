import inventory.InventoryDAO;
import orderhistory.OrderHistoryDAO;
import promocodes.PromoCodes;
import promocodes.PromoCodeDAO;
import savedcarts.SavedCartsDAO;
import cart.CartDAO;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        DataSource dataSource = ConnectorFactory.getDataSource();

       try (Connection connection = dataSource.getConnection()) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to Online Shopping, Select your choice");

            while (true) {
                System.out.println("1. Add item to the Cart");
                System.out.println("2. Remove item from the Cart");
                System.out.println("3. View the Cart");
                System.out.println("4. Apply Promo Code");
                System.out.println("5. Checkout");
                System.out.println("6. View Order History");
                System.out.println("7. Exit");

                int choice = scanner.nextInt();

                switch (choice) {

                    //Add Item to the Cart
                    case 1->{
                        System.out.println("Enter itemId and quantity to add:");
                        int itemId = scanner.nextInt();
                        int quantity = scanner.nextInt();

                        System.out.println("Enter your userId:");
                        int userId = scanner.nextInt();

                        CartDAO cartDAO = new CartDAO(connection);
                        cartDAO.addItemToCart(userId, itemId, quantity);
                    }

                    //Remove Item From the Cart
                    case 2->{
                        System.out.println("Enter itemId and quantity to remove:");
                        int itemId = scanner.nextInt();
                        int quantity = scanner.nextInt();

                        System.out.println("Enter your userId:");
                        int userId = scanner.nextInt();

                        CartDAO cartDAO = new CartDAO(connection);
                        cartDAO.removeItemFromCart(userId, itemId, quantity);
                    }

                    //View the Cart
                    case 3 ->{
                        System.out.println("Enter your userId to view the cart:");
                        int userId = scanner.nextInt();

                        CartDAO cartDAO = new CartDAO(connection);
                        cartDAO.viewTheCart(userId);
                    }

                    //Apply the Promo Code
                    case 4 ->{

                        System.out.println("Enter the Promo Code to apply:");

                        String promoCodeInput = scanner.next();

                        System.out.println("Enter your userId:");
                        int userId = scanner.nextInt();

                        CartDAO cartDAO = new CartDAO(connection);
                        double cartTotal = cartDAO.getCartTotal(userId);


                        PromoCodeDAO promoCodeDAO = new PromoCodeDAO(connection);
                        PromoCodes promoCode = promoCodeDAO.getPromoCodeByCode(promoCodeInput);

                        if (promoCode != null) {

                            double discountAmount = (promoCode.getDiscountPercentage() / 100) * cartTotal;
                            double amountAfterDiscount = cartTotal - discountAmount;

                            // Output discount
                            System.out.println("Promo Code Applied: " + promoCodeInput);
                            System.out.println("Discount Amount: $" + discountAmount);
                            System.out.println("Amount After Discount: $" + amountAfterDiscount);
                        } else {
                            System.out.println("Invalid or expired promo code.");
                        }

                    }

                    // Checkout case 5
                    case 5 -> {
                        System.out.println("Enter your userId for checkout:");
                        int userId = scanner.nextInt();

                        CartDAO cartDAO = new CartDAO(connection);
                        double cartTotal = cartDAO.getCartTotal(userId);

                        // Step 1: Update inventory after checkout
                        InventoryDAO inventoryItems = new InventoryDAO(connection);
                        inventoryItems.updateInventoryAfterCheckout(userId);

                        // Step 2: Add the order to the order history
                        OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO(connection);
                        int orderId = orderHistoryDAO.addOrderToHistory(userId, (float) cartTotal);

                        if (orderId != -1) {
                            // Step 3: View cart items (with orderId)
                            cartDAO.viewCartItems(userId, orderId);

                            // Step 4: Clear the cart after checkout process
                            cartDAO.clearCart(userId);

                            System.out.println("Total Cost of Cart: $" + cartTotal);
                            System.out.println("Delivery Date: " + java.time.LocalDate.now().plusDays(3));
                        }
                    }

                    case 6 -> {
                        System.out.println("Enter your userId to view your order history:");
                        int userId = scanner.nextInt();

                        OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO(connection);
                        orderHistoryDAO.getOrderHistory(userId);
                    }
                    case 7 ->{
                        System.out.println("Are there any unsaved items in your cart? (yes/no):");
                        String response = scanner.next().toLowerCase();

                        if (response.equals("yes")) {
                            System.out.println("Enter your userId to save items to saved cart:");
                            int userId = scanner.nextInt();

                            // Save items to SavedCarts

                            SavedCartsDAO savedCartsDAO = new SavedCartsDAO(connection);
                            savedCartsDAO.saveCartItems(userId);

                            System.out.println("Items are saved to saved cart. Thank you for shopping!");
                        } else {
                            System.out.println("Thank you for shopping!");
                        }

                        // Exit the program
                        return;
                    }

                }
          }
        } catch (Exception e) {
            System.out.println(e.getMessage());
       }
    }
}
