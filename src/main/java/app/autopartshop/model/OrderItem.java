package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Represents a specific item (autopart) in an order.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @Min(value = 1, message = "Quantity must be at least 1")
   private int quantity;
   @Min(value = 0, message = "Discount cannot be negative")
   @Max(value = 100, message = "Discount cannot exceed 100")
   private double discount;

   @ManyToOne
   @JoinColumn(name = "order_id", nullable = false, updatable = false)
   @NotNull
   private Order order;

   @ManyToOne
   @JoinColumn(name = "autopart_id", nullable = false, updatable = false)
   @NotNull
   private Autopart autopart;


}
