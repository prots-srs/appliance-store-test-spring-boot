package com.epam.rd.autocode.assessment.appliances.security;

import java.util.ArrayList;
import java.util.List;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements UserDetailsService {

  private PasswordEncoder passwordEncoder;
  private ClientRepository clientRepository;
  private EmployeeRepository employeeRepository;

  public UserLoginService(PasswordEncoder passwordEncoder, ClientRepository clientRepository,
      EmployeeRepository employeeRepository) {
    this.passwordEncoder = passwordEncoder;
    this.clientRepository = clientRepository;
    this.employeeRepository = employeeRepository;
  }

  // warning DEMO date password unencrypted !!!
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    if (email == null || email.isBlank()) {
      throw new UsernameNotFoundException(email);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();

    List<Employee> employee = employeeRepository.findByEmailIgnoreCase(email);

    employee.forEach(e -> System.out.println("employee:" + e.getEmail() + "-" + e.getName()));
    if (employee.size() > 0) {

      if (employee.get(0).getDepartment().equals("salle")) {

        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        return new User(email, new BCryptPasswordEncoder().encode(employee.get(0).getPassword()), authorities);
      }

      throw new UsernameNotFoundException(email);

      // for normal saved password
      // return new User(email, employee.get(0).getPassword(), authorities);
    }

    List<Client> client = clientRepository.findByEmailIgnoreCase(email);
    if (client.size() > 0) {

      authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
      return new User(email, new BCryptPasswordEncoder().encode(client.get(0).getPassword()), authorities);
    }

    throw new UsernameNotFoundException(email);
  }
}
