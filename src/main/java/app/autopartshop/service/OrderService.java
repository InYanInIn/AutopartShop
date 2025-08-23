package app.autopartshop.service;

import app.autopartshop.model.*;
import app.autopartshop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

   private final OrderRepository orderRepository;
   private final OrderItemRepository orderItemRepository;
   private final AutopartRepository autopartRepository;

   @Autowired
   public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, AutopartRepository autopartRepository) {
      this.orderRepository = orderRepository;
      this.orderItemRepository = orderItemRepository;
      this.autopartRepository = autopartRepository;
   }

   @Transactional
   public void updateOrderItemDiscount(Long orderItemId, double discount) {
      Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
      if (orderItemOptional.isPresent()) {
         OrderItem orderItem = (OrderItem) orderItemOptional.get();
         orderItem.setDiscount(discount);
         orderItemRepository.save(orderItem);
      } else {
         throw new IllegalArgumentException("Order item not found with ID: " + orderItemId);
      }

   }

   public double calculateItemTotal(OrderItem item) {
      return item.getAutopart().getPrice() * item.getQuantity() * (1 - item.getDiscount() / 100.0);
   }

   public double calculateOrderTotal(Long orderId) {
      Optional<Order> orderOptional = orderRepository.findByIdWithItems(orderId);
      if (!orderOptional.isPresent()) {
         throw new IllegalArgumentException("Order not found with ID: " + orderId);
      }
      Order order = orderOptional.get();
      double total = 0.0;
      Set<OrderItem> orderItems= order.getOrderItems();
      if (orderItems == null || orderItems.isEmpty()) {
         return total;
      }
      for (OrderItem item : orderItems) {
         double itemTotal = item.getAutopart().getPrice() * item.getQuantity() * (1 - item.getDiscount() / 100.0);
         total += itemTotal;
      }
      return total;
   }
   public Optional<Order> findByIdWithItems(Long id) {
      return orderRepository.findByIdWithItems(id);
   }

   public Optional<OrderItem> findOrderItemById(Long id) {
      return orderItemRepository.findById(id);
   }

   @Transactional
   public void addAutopartToOrder(Long orderId, Long autopartId, int quantity, double discount) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
      Autopart autopart = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found"));

      order.addAutopart(autopart, quantity, discount);
      orderRepository.save(order);
   }

   @Transactional
   public void removeAutopartFromOrder(Long orderId, Autopart autopart) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

      order.removeAutopart(autopart);
      orderRepository.save(order);
   }

   @Transactional
   public void addPaymentToOrder(Long orderId, Payment payment) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

      order.addPayment(payment);
      orderRepository.save(order);
   }

   @Transactional
   public void removePaymentFromOrder(Long orderId, Payment payment) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

      order.removePayment(payment);
      orderRepository.save(order);
   }

   @Transactional
   public void addShipmentToOrder(Long orderId, Shipment shipment) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

      order.addShipment(shipment);
      orderRepository.save(order);
   }

   @Transactional
   public void removeShipmentFromOrder(Long orderId, Shipment shipment) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

      order.removeShipment(shipment);
      orderRepository.save(order);
   }
}
