package app.autopartshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a shopping cart associated with a customer.
 * Contains a collection of cart items, each linked to an autopart and quantity.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cart {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<CartItem> cartItems = new HashSet<>();

   @OneToOne
   @JoinColumn(name = "customer_id")
   private CustomerRole customer;


   /**
    * Adds an autopart to the cart. If it already exists, increments the quantity.
    *
    * @param autopart The autopart to add
    * @param quantity Quantity to add
    */
   public void addAutopart(Autopart autopart, int quantity) {
      if (autopart == null || quantity <= 0) return;

      for (CartItem item : cartItems) {
         if (item.getAutopart().getId().equals(autopart.getId())) {
            item.setQuantity(item.getQuantity() + quantity);
            return;
         }
      }

      CartItem newItem = CartItem.builder()
            .cart(this)
            .autopart(autopart)
            .quantity(quantity)
            .build();

      cartItems.add(newItem);
   }


   /**
    * Removes an autopart from the cart.
    *
    * @param autopart The autopart to remove
    */
   public void removeAutopart(Autopart autopart) {
      if (autopart == null) return;

      CartItem toRemove = null;

      for (CartItem item : cartItems) {
         if (item.getAutopart().getId().equals(autopart.getId())) {
            toRemove = item;
            break;
         }
      }

      if (toRemove != null) {
         toRemove.setCart(null);
         cartItems.remove(toRemove);
      }
   }

}
