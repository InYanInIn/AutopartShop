package app.autopartshop.repository;

import app.autopartshop.model.InventoryItem;
import org.springframework.data.repository.CrudRepository;

public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long> {
}
