package com.example.medrest.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PrescriptionDto {
    @NotNull
    @NotBlank
    private String medicamentName;

    private Integer price;

    private Integer amountToTake;

    public PrescriptionDto() {

    }

    public PrescriptionDto(String medicamentName, Integer price, Integer amountToTake) {
        this.medicamentName = medicamentName;
        this.price = price;
        this.amountToTake = amountToTake;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getAmountToTake() {
        return amountToTake;
    }

    public void setAmountToTake(Integer amountToTake) {
        this.amountToTake = amountToTake;
    }
}
