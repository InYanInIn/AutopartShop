package app.autopartshop.service;

import app.autopartshop.model.*;
import app.autopartshop.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {
   private final CategoryRepository catRepo;
   private final AutopartRepository partRepo;

   @Autowired
   public CategoryService(CategoryRepository catRepo, AutopartRepository partRepo) {
      this.catRepo = catRepo;
      this.partRepo = partRepo;
   }

   @Transactional
   public void assignCategoryToAutopart(Long partId, Long catId) {
      Autopart part = partRepo.findById(partId)
            .orElseThrow(() -> new IllegalArgumentException("Part not found: " + partId));
      Category cat = catRepo.findById(catId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));

      part.addCategory(cat);
      partRepo.save(part);
   }

   @Transactional
   public void unassignCategoryFromAutopart(Long partId, Long catId) {
      Autopart part = partRepo.findById(partId)
            .orElseThrow(() -> new IllegalArgumentException("Part not found: " + partId));
      Category cat = catRepo.findById(catId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));

      part.removeCategory(cat);
      partRepo.save(part);
   }

   @Transactional
   public void addChildCategory(Long parentId, Long childId) {
      Category parent = catRepo.findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + parentId));
      Category child = catRepo.findById(childId)
            .orElseThrow(() -> new IllegalArgumentException("Child not found: " + childId));

      parent.addChild(child);
      catRepo.save(parent);
   }

   @Transactional
   public void removeChildCategory(Long parentId, Long childId) {
      Category parent = catRepo.findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + parentId));
      Category child = catRepo.findById(childId)
            .orElseThrow(() -> new IllegalArgumentException("Child not found: " + childId));

      parent.removeChild(child);
      catRepo.save(parent);
   }

   @Transactional
   public String getAutopartCategorySeq(Long partId) {
      Autopart part = partRepo.findById(partId)
            .orElseThrow(() -> new IllegalArgumentException("Part not found: " + partId));

      Set<Category> categories = part.getCategories();
      if (categories.isEmpty()) {
         return "No categories assigned to this autopart.";
      }

      StringBuilder seq = new StringBuilder();
      for (Category cat : categories) {
         seq.append(cat.getName()).append(" > ");
         Category parent = cat.getParentCategory();
         while (parent != null) {
            seq.append(parent.getName()).append(" > ");
            parent = parent.getParentCategory();
         }
      }
      return seq.toString().replaceAll(" > $", "");

   }
}

