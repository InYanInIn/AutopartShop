package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Represents a shipment of an order in the autopart shop system.
 * Contains details about the shipment date, carriers, tracking number, and status.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private LocalDateTime shipmentDate;

   @ElementCollection
   private Set<String> carriers;
   private String trackingNumber;
   @Enumerated(EnumType.STRING)
   private ShipmentStatus shipmentStatus;

   @ManyToOne
   @JoinColumn(name = "order_id", nullable = false, updatable = false)
   @NotNull
   private Order order;
}
