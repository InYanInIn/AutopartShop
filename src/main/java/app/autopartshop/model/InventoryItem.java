package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents an inventory item which links a specific autopart to a warehouse location and quantity.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @Min(value = 0, message = "Quantity cannot be negative")
   private int quantity;
   @NotBlank
   @Size(min = 1, max = 50, message = "Location Code must be between 1 and 50 characters")
   private String locationCode;

   @ManyToOne
   @JoinColumn(name = "autopart_id", nullable = false)
   private Autopart autopart;
}
