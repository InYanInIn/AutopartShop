package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a person in the autopart shop system.
 * A person can have roles such as Customer, Employee, or Supplier,
 * and multiple addresses.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Person {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
   private String name;
   @Email
   private String email;
   @Size(min = 1, max = 20, message = "Phone must be between 1 and 20 characters")
   private String phone;

   @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Address> addresses = new HashSet<>();

   @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
   private CustomerRole customer;

   @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
   private EmployeeRole employee;

   @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
   private SupplierRole supplier;

   /**
    * Assigns a customer role to this person.
    * The customer role is linked back to this person.
    *
    * @param customer the customer role to assign
    */
   public void assignCustomerRole(CustomerRole customer) {
      this.customer = customer;
      customer.setPerson(this);
   }

   /**
    * Assigns an employee role to this person.
    * The employee role is linked back to this person.
    *
    * @param employee the employee role to assign
    */
   public void assignEmployeeRole(EmployeeRole employee) {
      this.employee = employee;
      employee.setPerson(this);
   }

   /**
    * Assigns a supplier role to this person.
    * The supplier role is linked back to this person.
    *
    * @param supplier the supplier role to assign
    */
   public void assignSupplierRole(SupplierRole supplier) {
      this.supplier = supplier;
      supplier.setPerson(this);
   }

   /**
    * Removes the customer role from this person.
    * The customer role is unlinked from this person.
    */
   public void removeCustomerRole() {
      if (this.customer != null) {
         this.customer.setPerson(null);
         this.customer = null;
      }
   }

   /**
    * Removes the employee role from this person.
    * The employee role is unlinked from this person.
    */
   public void removeEmployeeRole() {
      if (this.employee != null) {
         this.employee.setPerson(null);
         this.employee = null;
      }
   }

   /**
    * Removes the supplier role from this person.
    * The supplier role is unlinked from this person.
    */
   public void removeSupplierRole() {
      if (this.supplier != null) {
         this.supplier.setPerson(null);
         this.supplier = null;
      }
   }

   /**
    * Adds an address to this person.
    * The address is linked back to this person.
    *
    * @param address the address to add
    */
   public void addAddress(Address address) {
      address.setPerson(this);
      this.addresses.add(address);
   }

   /**
    * Removes an address from this person.
    * The address is unlinked from this person.
    *
    * @param address the address to remove
    */
   public void removeAddress(Address address) {
      if (this.addresses.contains(address)) {
         address.setPerson(null);
         this.addresses.remove(address);
      }
   }

   /**
    * Updates an existing address for this person.
    * If the address with the given ID is found, it updates its fields.
    *
    * @param addressId the ID of the address to update
    * @param newData   the new data for the address
    */
   public void updateAddress(Long addressId, Address newData) {
      for (Address address : this.addresses) {
         if (address.getId().equals(addressId)) {
            address.setCountry(newData.getCountry());
            address.setCity(newData.getCity());
            address.setStreet(newData.getStreet());
            address.setPostalCode(newData.getPostalCode());
            address.setHouseNumber(newData.getHouseNumber());
            address.setApartmentNumber(newData.getApartmentNumber());
            return;
         }
      }
      throw new IllegalArgumentException("Address with id " + addressId + " not found for this person.");
   }

}
