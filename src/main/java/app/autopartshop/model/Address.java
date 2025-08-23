package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a physical address associated with a person in the Auto Part Shop system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @NotBlank
   @Size(min = 1, max = 50, message = "Country must be between 1 and 50 characters")
   private String country;
   @NotBlank
   @Size(min = 1, max = 100, message = "City must be between 1 and 100 characters")
   private String city;
   @NotBlank
   @Size(min = 1, max = 50, message = "Country must be between 1 and 50 characters")
   private String street;
   @NotBlank
   @Size(min = 1, max = 10, message = "postalCode must be between 1 and 10 characters")
   private String postalCode;
   @NotBlank
   @Size(min = 1, max = 50, message = "houseNumber must be between 1 and 50 characters")
   private String houseNumber;
   @Size(min = 1, max = 50, message = "apartmentNumber must be between 1 and 50 characters")
   private String apartmentNumber;

   @ManyToOne
   @JoinColumn(name = "person_id", nullable = false, updatable = false)
   @NotNull
   private Person person;
}
