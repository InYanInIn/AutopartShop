package app.autopartshop.model.DTO;

public class OrderItemDTO {
   private Long id;
   private String autopartName;
   private double price;
   private String description;
   private int quantity;
   private double discount;
   private double totalPrice;

   public OrderItemDTO(Long id, String autopartName, double price, String description, int quantity, double discount, double totalPrice) {
      this.id = id;
      this.autopartName = autopartName;
      this.price = price;
      this.description = description;
      this.quantity = quantity;
      this.discount = discount;
      this.totalPrice = totalPrice;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getAutopartName() {
      return autopartName;
   }

   public void setAutopartName(String autopartName) {
      this.autopartName = autopartName;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public int getQuantity() {
      return quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public double getDiscount() {
      return discount;
   }

   public void setDiscount(double discount) {
      this.discount = discount;
   }

   public double getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(double totalPrice) {
      this.totalPrice = totalPrice;
   }
}
