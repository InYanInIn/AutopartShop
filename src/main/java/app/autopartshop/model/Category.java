package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a category of auto parts. Categories can be hierarchical (with parent/child relationships)
 * and can be assigned to multiple auto parts.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Category {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Column(unique = true)
   @Size(min = 1, max = 50, message = "Category Name must be between 1 and 50 characters")
   private String name;
   private String description;

   @ManyToOne
   @JoinColumn(name = "parent_category_id")
   private Category parentCategory;

   @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Category> childCategories = new HashSet<>();

   @ManyToMany(mappedBy = "categories")
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Autopart> autoparts = new HashSet<>();


   /**
    * Adds a child category and sets this as its parent.
    * @param child the category to be added as a child.
    */
   public void addChild(Category child) {
      if (child == null) return;
      if (!childCategories.contains(child)) {
         childCategories.add(child);
         child.setParentCategory(this);
      }
   }

   /**
    * Removes a child category and clears its parent reference.
    * @param child the category to be removed from children.
    */
   public void removeChild(Category child) {
      if (child == null) return;
      if (childCategories.remove(child)) {
         child.setParentCategory(null);
      }
   }
}
