package app.autopartshop.repository;

import app.autopartshop.model.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
}
