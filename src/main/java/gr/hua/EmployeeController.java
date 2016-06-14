package gr.hua;

import gr.hua.models.Employee;
import gr.hua.models.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.List;


@RestController
public class EmployeeController {

    /**
     * GET /create  --> Create a new user and save it in the database.
     */


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
       // return authentication.getDetails().toString();

        String principal = authentication.getPrincipal().toString();

        RetrieveUserAttributes retrieveUserAttributes = new RetrieveUserAttributes();
        retrieveUserAttributes.getUserBasicAttributes(authentication.getName(), retrieveUserAttributes.getLdapContext());

        return principal + "///" +  authentication.getAuthorities().toString();


    }




    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = "application/json")    @ResponseBody
    public List<Employee> all(@AuthenticationPrincipal final UserDetails user) {
        List<Employee> empls= employeeDao.findAll();
        return empls;

    }



    @RequestMapping("/create")
    @ResponseBody
    public String create(String name, String role) {
        String employeeId = "";
        try {
            Employee emp = new Employee(name, role);
            employeeDao.save(emp);
            employeeId = String.valueOf(emp.getId());
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created with id = " + employeeId;
    }

    /**
     * GET /delete  --> Delete the user having the passed id.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(int id) {
        try {
            Employee emp = new Employee(id);
            employeeDao.delete(emp);
        }
        catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User succesfully deleted!";
    }

    /**
     * GET /get-by-email  --> Return the id for the user having the passed
     * email.
     */
    @RequestMapping("/get-by-email")
    @ResponseBody
    public String getByEmail(int id) {
        String employeeId = "";
        try {
            Employee emp = employeeDao.findById(id);
            employeeId = String.valueOf(emp.getId());
        }
        catch (Exception ex) {
            return "Employee not found";
        }
        return "The employee id is: " + employeeId;
    }

    /**
     * GET /update  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(int id, String name, String role) {
        try {
            Employee emp = employeeDao.findOne(id);
            emp.setRole(role);
            emp.setName(name);
            employeeDao.save(emp);
        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }

    // Private fields

    @Autowired
    private EmployeeDao employeeDao;

}