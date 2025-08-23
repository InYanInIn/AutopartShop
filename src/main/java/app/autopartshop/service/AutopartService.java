package app.autopartshop.service;

import app.autopartshop.model.Autopart;
import app.autopartshop.model.Order;
import app.autopartshop.repository.AutopartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutopartService {

   private final AutopartRepository autopartRepository;

   public AutopartService(AutopartRepository autopartRepository) {
      this.autopartRepository = autopartRepository;
   }

   public Optional<Autopart> findByIdWithItems(Long id) {
      return autopartRepository.findByIdWithItems(id);
   }
}
