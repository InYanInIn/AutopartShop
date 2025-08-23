package app.autopartshop.repository;

import app.autopartshop.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
   @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
   Optional<Order> findByIdWithItems(@Param("id") Long id);

}
