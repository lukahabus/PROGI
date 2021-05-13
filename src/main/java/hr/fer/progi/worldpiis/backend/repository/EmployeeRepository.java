package hr.fer.progi.worldpiis.backend.repository;

import hr.fer.progi.worldpiis.backend.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.username = ?1")
    Optional<Employee> findByUsername(String username);

}
