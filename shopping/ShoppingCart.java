package shopping;


import model.CartItem;
import model.Product;
import exceptionhandling.ProductUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
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

