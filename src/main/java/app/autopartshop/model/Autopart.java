package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents an auto part in the catalog.
 * Includes relationships to categories, inventory, cart items, and order items.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Autopart {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 50, message = "Autopart Name must be between 1 and 50 characters")
   private String name;
   private String description;
   @Min(value = 0, message = "Price cannot be negative")
   private Double price;

   @OneToMany(mappedBy = "autopart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<InventoryItem> inventoryItems = new HashSet<>();

   @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
   @JoinTable(
         name = "category_autopart",
         joinColumns = @JoinColumn(name = "autopart_id"),
         inverseJoinColumns = @JoinColumn(name = "category_id")
   )
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Category> categories = new HashSet<>();

   @OneToMany(mappedBy = "autopart", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<CartItem> cartItems = new HashSet<>();

   @OneToMany(mappedBy = "autopart", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<OrderItem> orderItems = new HashSet<>();


   /**
    * Adds a category to this autopart and ensures bidirectional consistency.
    */
   public void addCategory(Category cat) {
      if (cat == null) return;

      if (!categories.contains(cat)) {
         categories.add(cat);
         cat.getAutoparts().add(this);
      }
   }


   /**
    * Removes a category from this autopart and ensures bidirectional consistency.
    */
   public void removeCategory(Category cat) {
      if (cat == null) return;

      if (categories.remove(cat)) {
         cat.getAutoparts().remove(this);
      }
   }


   /**
    * Adds an inventory item or increases quantity at a given location.
    *
    * @param locationCode the warehouse/location code
    * @param quantity     number of items to add
    */
   public void addInventoryItem(String locationCode, int quantity) {
      if (locationCode == null || locationCode.isBlank() || quantity < 0) return;
      // Check if an InventoryItem at this location already exists
      for (InventoryItem item : inventoryItems) {
         if (item.getLocationCode().equals(locationCode)) {
            item.setQuantity(item.getQuantity() + quantity);
            return;
         }
      }
      // Otherwise create new
      InventoryItem newItem = InventoryItem.builder()
            .autopart(this)
            .locationCode(locationCode)
            .quantity(quantity)
            .build();
      inventoryItems.add(newItem);
   }


   /**
    * Removes inventory from a given location.
    *
    * @param locationCode the warehouse/location code
    */
   public void removeInventoryItem(String locationCode) {
      if (locationCode == null) return;
      InventoryItem toRemove = null;
      for (InventoryItem item : inventoryItems) {
         if (item.getLocationCode().equals(locationCode)) {
            toRemove = item;
            break;
         }
      }
      if (toRemove != null) {
         inventoryItems.remove(toRemove);
         toRemove.setAutopart(null);
      }
   }


   /**
    * Updates the quantity of items at a specific inventory location.
    *
    * @param locationCode the warehouse/location code
    * @param newQuantity  the new quantity to set
    */
   public void updateInventoryQuantity(String locationCode, int newQuantity) {
      if (locationCode == null || newQuantity < 0) return;
      for (InventoryItem item : inventoryItems) {
         if (item.getLocationCode().equals(locationCode)) {
            item.setQuantity(newQuantity);
            return;
         }
      }
      throw new IllegalArgumentException("No inventory item at location: " + locationCode);
   }
}

