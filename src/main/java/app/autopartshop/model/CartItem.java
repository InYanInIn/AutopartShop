package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a single item in a customer's cart.
 * Each item links a specific autopart with a quantity and belongs to a specific cart.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
      name = "cart_item",
      uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "autopart_id"}
))
@Builder
public class CartItem {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @Min(value = 1, message = "Quantity must be at least 1")
   private int quantity;

   @ManyToOne
   @JoinColumn(name = "cart_id", nullable = false, updatable = false)
   @NotNull
   private Cart cart;

   @ManyToOne
   @JoinColumn(name = "autopart_id", nullable = false, updatable = false)
   @NotNull
   private Autopart autopart;
}
