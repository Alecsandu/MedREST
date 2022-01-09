package com.example.medrest.model;

import javax.persistence.*;

@Entity
@Table(name = "specialisations")
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialisation_name")
    private String name;

    @Column(name = "min_salary")
    private Integer minSalary;

    @Column(name = "max_salary")
    private Integer maxSalary;

    public Specialisation() {
    }

    public Specialisation(String name, Integer minSalary, Integer maxSalary) {
        this.name = name;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    public void patch (Specialisation specialisation) {
        if (specialisation != null) {
            if (specialisation.getName() != null) {
                name = specialisation.getName();
            }
            if (specialisation.getMinSalary() != null) {
                minSalary = specialisation.getMinSalary();
            }
            if (specialisation.getMaxSalary() != null) {
                maxSalary = specialisation.getMaxSalary();
            }
        }
    }
}
