package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a payment made for an order in the autopart shop system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @Min(value = 0, message = "Amount cannot be negative")
   private double amount;

   @NotBlank
   @Size(min = 1, max = 50, message = "Payment method must be between 1 and 50 characters")
   private String paymentMethod;

   private LocalDateTime paymentDate;

   @ManyToOne
   @JoinColumn(name = "order_id", nullable = false)
   @NotNull
   private Order order;
}
