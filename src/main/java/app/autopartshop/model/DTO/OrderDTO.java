package app.autopartshop.model.DTO;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderDTO {
   private Long id;
   private String name;
   private String description;
   private String orderStatus;
   private LocalDateTime createdAt;
   private double totalPrice;

   public OrderDTO(Long id, String name, String description, String orderStatus, LocalDateTime createdAt, double totalPrice) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.orderStatus = orderStatus;
      this.createdAt = createdAt;
      this.totalPrice = totalPrice;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getOrderStatus() {
      return orderStatus;
   }

   public void setOrderStatus(String orderStatus) {
      this.orderStatus = orderStatus;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public double getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(double totalPrice) {
      this.totalPrice = totalPrice;
   }
}
