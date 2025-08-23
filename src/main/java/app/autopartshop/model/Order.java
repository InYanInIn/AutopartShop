package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a customer's order, including items, status, payments, and shipment info.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Order {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 50, message = "Order Name must be between 1 and 50 characters")
   private String name;
   private String description;

   @Enumerated(EnumType.STRING)
   @Column(name = "order_status")
   private OrderStatus orderStatus;
   private LocalDateTime  createdAt;

   @Getter
   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<OrderItem> orderItems = new HashSet<>();

   @ManyToOne
   @JoinColumn(name = "customer_id")
   private CustomerRole customer;

   @OneToMany(mappedBy = "order")
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Payment> payments = new HashSet<>();

   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Shipment> shipments = new HashSet<>();

   /**
    * Adds a payment to the order.
    */
   public void addPayment(Payment payment) {
      if (payment == null) return;

      for (Payment existing : payments) {
         if (existing.getId() != null && existing.getId().equals(payment.getId())) {
            return;
         }
      }

      payment.setOrder(this);
      payments.add(payment);
   }

   /**
    * Removes a payment from the order.
    */
   public void removePayment(Payment payment) {
      if (payment == null) return;

      Payment toRemove = null;
      for (Payment existing : payments) {
         if (existing.getId() != null && existing.getId().equals(payment.getId())) {
            toRemove = existing;
            break;
         }
      }

      if (toRemove != null) {
         toRemove.setOrder(null);
         payments.remove(toRemove);
      }
   }

   /**
    * Adds a shipment to the order.
    */
   public void addShipment(Shipment shipment) {
      if (shipment == null) return;

      for (Shipment existing : shipments) {
         if (existing.getId() != null && existing.getId().equals(shipment.getId())) {
            return;
         }
      }

      shipment.setOrder(this);
      shipments.add(shipment);
   }

   /**
    * Removes a shipment from the order.
    */
   public void removeShipment(Shipment shipment) {
      if (shipment == null) return;

      Shipment toRemove = null;
      for (Shipment existing : shipments) {
         if (existing.getId() != null && existing.getId().equals(shipment.getId())) {
            toRemove = existing;
            break;
         }
      }

      if (toRemove != null) {
         toRemove.setOrder(null);
         shipments.remove(toRemove);
      }
   }

   /**
    * Adds an autopart to the order.
    *
    * @param autopart The part to be added.
    * @param quantity Quantity of the part.
    * @param discount Discount applied.
    */
   public void addAutopart(Autopart autopart, int quantity, double discount) {
      if (autopart == null || quantity <= 0 || discount < 0) return;

      for (OrderItem item : orderItems) {
         if (item.getAutopart().getId().equals(autopart.getId())) {
            item.setQuantity(item.getQuantity() + quantity);
            item.setDiscount(discount);
            return;
         }
      }

      OrderItem newItem = OrderItem.builder()
            .order(this)
            .autopart(autopart)
            .quantity(quantity)
            .discount(discount)
            .build();

      orderItems.add(newItem);
   }

   /**
    * Removes an autopart from the order.
    */
   public void removeAutopart(Autopart autopart) {
      if (autopart == null) return;

      OrderItem toRemove = null;

      for (OrderItem item : orderItems) {
         if (item.getAutopart().getId().equals(autopart.getId())) {
            toRemove = item;
            break;
         }
      }

      if (toRemove != null) {
         toRemove.setOrder(null);
         orderItems.remove(toRemove);
      }
   }

}
