package com.lui.prelims.controller;

import com.lui.prelims.dto.EmployeeDTO;
import com.lui.prelims.exception.ResourceNotFoundException;
import com.lui.prelims.model.Employee;
import com.lui.prelims.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("employee", new EmployeeDTO()); // bind DTO
        return "create"; // create.html
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("employee", employeeDTO);
            return "create";
        }

        Employee newEmployee = new Employee();
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setEmail(employeeDTO.getEmail());

        employeeRepository.save(newEmployee);

        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());   // ✅ include ID
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmail(employee.getEmail());

        model.addAttribute("employee", employeeDTO);
        return "edit"; // edit.html
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "edit";
        }

        // ✅ safer way: fetch existing entity and update
        Employee employee = employeeRepository.findById(employeeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", employeeDTO.getId()));

        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());

        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        employeeRepository.deleteById(id);
        return "redirect:/";
    }
}
