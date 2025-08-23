package app.autopartshop.service;

import app.autopartshop.model.Autopart;
import app.autopartshop.model.InventoryItem;
import app.autopartshop.repository.AutopartRepository;
import app.autopartshop.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

   private final AutopartRepository autopartRepository;
   private final InventoryItemRepository inventoryRepository;

   @Autowired
   public InventoryService(AutopartRepository autopartRepository,
                           InventoryItemRepository inventoryRepository) {
      this.autopartRepository = autopartRepository;
      this.inventoryRepository = inventoryRepository;
   }

   @Transactional
   public void addToInventory(Long autopartId, String locationCode, int quantity) {
      Autopart part = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found: " + autopartId));
      part.addInventoryItem(locationCode, quantity);
      autopartRepository.save(part);
   }

   @Transactional
   public void removeFromInventory(Long autopartId, String locationCode) {
      Autopart part = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found: " + autopartId));
      part.removeInventoryItem(locationCode);
      autopartRepository.save(part);
   }

   @Transactional
   public void updateInventory(Long autopartId, String locationCode, int newQuantity) {
      Autopart part = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found: " + autopartId));
      part.updateInventoryQuantity(locationCode, newQuantity);
      autopartRepository.save(part);
   }

   public List<InventoryItem> listInventory(Long autopartId) {
      Autopart part = autopartRepository.findById(autopartId)
            .orElseThrow(() -> new IllegalArgumentException("Autopart not found: " + autopartId));
      return new ArrayList<>(part.getInventoryItems());
   }
}

