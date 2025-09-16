package com.lui.prelims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class EmployeeDTO {

    private int id;

    @NotBlank(message = "Name is required.")
    private String name;

    @Email
    @NotBlank(message = "Email is required.")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
