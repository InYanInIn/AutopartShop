package app.autopartshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents an employee's role in the system, including their position,
 * hire date, and associated person entity.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRole {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   @NotBlank
   @Size(min = 1, max = 100, message = "Position must be between 1 and 100 characters")
   private String position;
   private LocalDateTime hireDate;


   @OneToOne
   @JoinColumn(name = "person_id")
   private Person person;

   /**
    * Associates a person with this employee role.
    * Used for dynamic assignment of a person entity to the employee role.
    * @param person the person entity to associate.
    */
   public void setPerson(Person person) {
      this.person = person;
   }
}
