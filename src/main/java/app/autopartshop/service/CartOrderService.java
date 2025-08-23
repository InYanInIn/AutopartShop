package app.autopartshop.service;

import app.autopartshop.model.*;
import app.autopartshop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class CartOrderService {

   private final CartRepository cartRepository;
   private final OrderRepository orderRepository;

   @Autowired
   public CartOrderService(CartRepository cartRepository,
                           OrderRepository orderRepository) {
      this.cartRepository = cartRepository;
      this.orderRepository = orderRepository;
   }

   /**
    * Creates a new Order from the contents of the given Cart,
    * clears the cart, and returns the saved Order.
    */
   @Transactional
   public Order placeOrderFromCart(Long cartId, String orderName, String orderDescription) {
      // 1. Load the cart (with items)
      Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));

      // 2. Create a new Order and attach to the same CustomerRole
      Order order = Order.builder()
            .name(orderName)
            .description(orderDescription)
            .orderStatus(OrderStatus.Created)
            .createdAt(LocalDateTime.now())
            .customer(cart.getCustomer())
            .build();

      // 3. Move each CartItem into the Order as an OrderItem
      for (CartItem cartItem : new HashSet<>(cart.getCartItems())) {
         Autopart part = cartItem.getAutopart();
         int qty = cartItem.getQuantity();

         // use the addAutopart method on Order
         order.addAutopart(part, qty, 0.0);

         // remove from cart
         cart.removeAutopart(part);
      }

      // 4. Save the new order (cascades its OrderItems)
      Order saved = orderRepository.save(order);

      // 5. Persist the cleaned-up cart
      cartRepository.save(cart);

      return saved;
   }
}

