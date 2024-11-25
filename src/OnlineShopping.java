import services.ShoppingService;

import java.sql.Connection;
import java.util.Scanner;

public class OnlineShopping {
    public static void main(String[] args) {
        try (Connection connection = ConnectorFactory.getDataSource().getConnection()) {
            Scanner scanner = new Scanner(System.in);
            ShoppingService shoppingService = new ShoppingService(connection);

            System.out.println("Welcome to Online Shopping, Select your Choice");


            while (true) {
                System.out.println("1. Add item to the Cart");
                System.out.println("2. Remove item from the Cart");
                System.out.println("3. View the Cart");
                System.out.println("4. Apply Promo Code");
                System.out.println("5. Checkout");
                System.out.println("6. View Order History");
                System.out.println("7. Exit");

                int choice = scanner.nextInt();
                int userId;
                int itemId;
                int quantity;
                String promoCodeInput = null;
                switch (choice) {
                    case 1: // Add Item to the Cart
                        System.out.println("Enter your userId:");
                        userId = scanner.nextInt();
                        System.out.println("Enter itemId:");
                        itemId = scanner.nextInt();
                        System.out.println("Enter Quantity: ");
                        quantity = scanner.nextInt();
                        shoppingService.addItemToCart(userId, itemId, quantity);
                        break;

                    case 2: // Remove Item From the Cart
                        System.out.println("Enter your userId:");
                        userId = scanner.nextInt();
                        System.out.println("Enter itemId:");
                        itemId = scanner.nextInt();
                        System.out.println("Enter Quantity: ");
                        quantity = scanner.nextInt();
                        shoppingService.removeItemFromCart(userId, itemId, quantity);
                        break;

                    case 3: // View the Cart
                        System.out.println("Enter your userId to view the cart:");
                        userId = scanner.nextInt();
                        shoppingService.viewTheCart(userId);
                        break;

                    case 4: // Apply Promo Code
                        System.out.println("Enter the Promo Code to apply:");
                        promoCodeInput = scanner.next();
                        System.out.println("Enter your userId:");
                        userId = scanner.nextInt();
                        shoppingService.applyPromoCode(userId, promoCodeInput);
                        break;

                    case 5: // Checkout
                        System.out.println("Enter your userId for checkout:");
                        userId = scanner.nextInt();
                        System.out.println("Do you have a promo code? (yes/no):");
                        String hasPromoCode = scanner.next().toLowerCase();

                        if ("yes".equals(hasPromoCode)) {
                            System.out.println("Enter the Promo Code:");
                            promoCodeInput = scanner.next();
                        }

                        shoppingService.checkout(userId, promoCodeInput);
                        break;

                    case 6: // View Order History
                        System.out.println("Enter your userId to view your order history:");
                        userId = scanner.nextInt();
                        shoppingService.viewOrderHistory(userId);
                        break;

                    case 7: // Exit the System
                        System.out.println("Are there any unsaved items in your cart? (yes/no):");
                        String response = scanner.next().toLowerCase();
                        if (response.equals("yes")) {
                            System.out.println("Enter your userId to save items to saved cart:");
                            userId = scanner.nextInt();
                            shoppingService.saveCartItems(userId);
                        }
                        if (response.equals("no")) {
                            System.out.println("Thank you for shopping!");
                        }
                        System.exit(0); // Exit the program
                        break;
                    default:
                        System.err.println("Invalid selection. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

