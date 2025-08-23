package app.autopartshop.repository;

import app.autopartshop.model.Autopart;
import app.autopartshop.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AutopartRepository extends CrudRepository<Autopart, Long> {
   @Query("SELECT a FROM Autopart a LEFT JOIN FETCH a.orderItems WHERE a.id = :id")
   Optional<Autopart> findByIdWithItems(@Param("id") Long id);
}
