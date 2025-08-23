package app.autopartshop.service;

import app.autopartshop.model.Address;
import app.autopartshop.model.Person;
import app.autopartshop.repository.AddressRepository;
import app.autopartshop.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AddressService {

   private final PersonRepository personRepository;
   private final AddressRepository addressRepository;

   @Autowired
   public AddressService(PersonRepository personRepository,
                         AddressRepository addressRepository) {
      this.personRepository = personRepository;
      this.addressRepository = addressRepository;
   }

   /**
    * Add a new Address to the given Person.
    */
   @Transactional
   public Address addAddress(Long personId, Address address) {
      Person person = personRepository.findById(personId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
      person.addAddress(address);
      // because of CascadeType.ALL, saving person will persist the new address
      personRepository.save(person);
      return address;
   }

   /**
    * Remove an Address from the given Person.
    */
   @Transactional
   public void removeAddress(Long personId, Long addressId) {
      Person person = personRepository.findById(personId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
      Optional<Address> opt = person.getAddresses().stream()
            .filter(a -> a.getId().equals(addressId))
            .findFirst();
      if (opt.isEmpty()) {
         throw new IllegalArgumentException("Address not found for this person: " + addressId);
      }
      person.removeAddress(opt.get());
      personRepository.save(person);
      // address is orphanRemoved, no need to delete explicitly
   }

   /**
    * Update an existing Address of the given Person.
    */
   @Transactional
   public Address updateAddress(Long personId, Long addressId, Address newData) {
      Person person = personRepository.findById(personId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
      person.updateAddress(addressId, newData);
      personRepository.save(person);
      return addressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalStateException("Address just updated not found: " + addressId));
   }

   /**
    * List all addresses belonging to a person.
    */

   public Set<Address> listAddresses(Long personId) {
      Person person = personRepository.findById(personId)
            .orElseThrow(
                  () -> new IllegalArgumentException("Person not found: " + personId)
            );
      return Collections.unmodifiableSet(person.getAddresses());
   }

   /**
    * Retrieve a specific address by its ID.
    */
   public Address getAddress(Long addressId) {
      return addressRepository.findById(addressId)
            .orElseThrow(
                  () -> new IllegalArgumentException("Address not found: " + addressId)
            );
   }
}

