package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a customer's role in the system, including payment methods,
 * their associated person entity, a shopping cart, and their orders.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerRole {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @ElementCollection
   @CollectionTable(name = "customer_role_payment_methods", joinColumns = @JoinColumn(name = "customer_role_id"))
   @Column(name = "payment_method")
   private List<String> paymentMethods;

   @OneToOne
   @JoinColumn(name = "person_id")
   @NotNull
   private Person person;

   @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
   private Cart cart;

   @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   @ToString.Exclude
   @EqualsAndHashCode.Exclude
   private Set<Order> orders = new HashSet<>();


   /**
    * Assigns a person to this customer role.
    * Used for dynamic assignment of a person entity to the customer role.
    * @param person the person entity to associate.
    */
   public void setPerson(Person person) {
      this.person = person;
   }
}
