package com.JIMS.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TAB_DEPARTMENTS")
public class Department {

    @Id
    @Column(name = "Dept_id")
    private Integer deptId;

    @Column(name = "Department_name")
    private String departmentName;

    // Constructors
    public Department() {}

    public Department(Integer deptId, String departmentName) {
        this.deptId = deptId;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
