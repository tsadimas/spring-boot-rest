package gr.hua.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
public interface EmployeeDao extends CrudRepository<Employee, Integer> {

    /**
     * This method will find an User instance in the database by its email.
     * Note that this method is not implemented and its working code will be
     * automagically generated from its signature by Spring Data JPA.
     */
    public Employee findById(Integer id);

    public List<Employee> findAll();

}