package com.example.medrest.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "specialisations")
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "specialisation_name", nullable = false)
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Specialisation specialisation = (Specialisation) obj;
        return Objects.equals(id, specialisation.getId()) &&
                Objects.equals(name, specialisation.getName()) &&
                Objects.equals(minSalary, specialisation.getMinSalary()) &&
                Objects.equals(maxSalary, specialisation.getMaxSalary());
    }
}
