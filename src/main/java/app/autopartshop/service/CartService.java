package app.autopartshop.service;

import app.autopartshop.model.Autopart;
import app.autopartshop.model.Cart;
import app.autopartshop.model.CartItem;
import app.autopartshop.repository.AutopartRepository;
import app.autopartshop.repository.CartItemRepository;
import app.autopartshop.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

   private final CartRepository cartRepository;
   private final CartItemRepository cartItemRepository;
   private final AutopartRepository autopartRepository;

   @Autowired
   public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, AutopartRepository autopartRepository) {
      this.cartRepository = cartRepository;
      this.cartItemRepository = cartItemRepository;
      this.autopartRepository = autopartRepository;
   }

   @Transactional
   public void addAutopartToCart(Long cartId, Long autopartId, int quantity) {
      if (quantity <= 0) {
         throw new IllegalArgumentException("Quantity must be greater than zero");
      }

      Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
      Autopart autopart = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found"));

      for (CartItem item : cart.getCartItems()) {
         if (item.getAutopart().getId().equals(autopartId)) {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            cartRepository.save(cart);
            return;
         }
      }

      CartItem newItem = CartItem.builder()
            .cart(cart)
            .autopart(autopart)
            .quantity(quantity)
            .build();
      cart.getCartItems().add(newItem);
      cartItemRepository.save(newItem);
      cartRepository.save(cart);
   }

   @Transactional
   public void removeAutopartFromCart(Long cartId, Long autopartId) {
      Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

      CartItem toRemove = null;
      for (CartItem item : cart.getCartItems()) {
         if (item.getAutopart().getId().equals(autopartId)) {
            toRemove = item;
            break;
         }
      }

      if (toRemove != null) {
         cart.getCartItems().remove(toRemove);
         cartItemRepository.delete(toRemove);
         cartRepository.save(cart);
      }
   }

}
