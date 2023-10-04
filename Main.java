import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class Product {
    private int id;
    private String name;
    private int price;
    private boolean availability;

    public Product(int id, String name, int price, boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: $" + price + ", Available: " + availability;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class ShoppingCart {
    private List<CartItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) throws ProductUnavailableException {
        if (product.isAvailable() && quantity > 0) {
            for (CartItem item : items) {
                if (item.getProduct().getId() == product.getId()) {
                    item.setQuantity(item.getQuantity() + quantity);
                    return;
                }
            }
            items.add(new CartItem(product, quantity));
        } else {
            throw new ProductUnavailableException("Product " + product.getName() + " is unavailable or quantity is invalid.");
        }
    }

    public void updateQuantity(Product product, int newQuantity) throws ProductUnavailableException {
        if (product.isAvailable() && newQuantity >= 0) {
            for (CartItem item : items) {
                if (item.getProduct().getId() == product.getId()) {
                    item.setQuantity(newQuantity);
                    return;
                }
            }
            throw new ProductUnavailableException("Product " + product.getName() + " not found in the cart.");
        } else {
            throw new ProductUnavailableException("Product " + product.getName() + " is unavailable or quantity is invalid.");
        }
    }

    public void removeItem(Product product) throws ProductUnavailableException {
        boolean removed = items.removeIf(item -> item.getProduct().getId() == product.getId());
        if (!removed) {
            throw new ProductUnavailableException("Product " + product.getName() + " not found in the cart.");
        }
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void displayCart() {
        System.out.println("Shopping Cart:");
        for (CartItem item : items) {
            Product product = item.getProduct();
            System.out.println(
                    "Product: " + product.getName() +
                    ", Quantity: " + item.getQuantity() +
                    ", Price: $" + product.getPrice()
            );
        }
        System.out.println("Total: $" + calculateTotal());
    }
}

class ProductUnavailableException extends Exception {
    public ProductUnavailableException(String message) {
        super(message);
    }
}

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        FileHandler fileHandler = null;
        try {
            // Logging to a file
            fileHandler = new FileHandler("cart.log");
            LOGGER.addHandler(fileHandler);

            // Create console handler and set level to ALL
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);

            LOGGER.setLevel(Level.ALL);

            // Define log format
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            Product product1 = new Product(1, "Laptop", 1000, true);
            Product product2 = new Product(2, "HeadPhone", 50, true);

            ShoppingCart cart = new ShoppingCart();
            cart.addItem(product1, 2);
            cart.addItem(product2, 1);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Display Products");
                System.out.println("2. Add Product to Cart");
                System.out.println("3. View Cart");
                System.out.println("4. Update Cart Item Quantity");
                System.out.println("5. Remove Item from Cart");
                System.out.println("6. Calculate Total Bill");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        System.out.println("Available Products:");
                        System.out.println(product1);
                        System.out.println(product2);
                        break;
                    case 2:
                        System.out.print("Enter the product ID: ");
                        int productId = scanner.nextInt();
                        System.out.print("Enter the quantity: ");
                        int quantity = scanner.nextInt();
                        try {
                            if (productId == 1) {
                                cart.addItem(product1, quantity);
                            } else if (productId == 2) {
                                cart.addItem(product2, quantity);
                            } else {
                                System.out.println("Invalid product ID.");
                            }
                        } catch (ProductUnavailableException e) {
                            LOGGER.log(Level.WARNING, "Failed to add product to cart: " + e.getMessage());
                            System.out.println("Failed to add product to cart: " + e.getMessage());
                        }
                        break;
                    case 3:
                        cart.displayCart();
                        break;
                    case 4:
                        System.out.print("Enter the product ID to update quantity: ");
                        int updateId = scanner.nextInt();
                        System.out.print("Enter the new quantity: ");
                        int newQuantity = scanner.nextInt();
                        try {
                            if (updateId == 1) {
                                cart.updateQuantity(product1, newQuantity);
                            } else if (updateId == 2) {
                                cart.updateQuantity(product2, newQuantity);
                            } else {
                                System.out.println("Invalid product ID.");
                            }
                        } catch (ProductUnavailableException e) {
                            LOGGER.log(Level.WARNING, "Failed to update cart item quantity: " + e.getMessage());
                            System.out.println("Failed to update cart item quantity: " + e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.print("Enter the product ID to remove from cart: ");
                        int removeId = scanner.nextInt();
                        try {
                            if (removeId == 1) {
                                cart.removeItem(product1);
                            } else if (removeId == 2) {
                                cart.removeItem(product2);
                            } else {
                                System.out.println("Invalid product ID.");
                            }
                        } catch (ProductUnavailableException e) {
                            LOGGER.log(Level.WARNING, "Failed to remove product from cart: " + e.getMessage());
                            System.out.println("Failed to remove product from cart: " + e.getMessage());
                        }
                        break;
                    case 6:
                        double total = cart.calculateTotal();
                        System.out.println("Total Bill: $" + total);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage());
            System.out.println("An unexpected error occurred. Please check the log for details.");
        } finally {
            if (fileHandler != null) {
                fileHandler.close();
            }
        }
    }
}
