package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a supplier role in the system.
 * This class is used to manage supplier information and their associated person.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRole {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 100, message = "Company Name must be between 1 and 100 characters")
   private String companyName;
   @NotBlank
   @Size(min = 1, max = 10, message = "Supplier must be between 1 and 10 characters")
   private String supplierCode;


   @OneToOne
   @JoinColumn(name = "person_id")
   private Person person;

   /**
    * Associates a person with this employee role.
    * Used for dynamic assignment of a person entity to the supplier role.
    * @param person the person entity to associate.
    */
   public void setPerson(Person person) {
      this.person = person;
   }
}
