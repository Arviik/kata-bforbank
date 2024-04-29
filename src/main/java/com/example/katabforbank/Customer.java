package com.example.katabforbank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Customer {
    private @Id @GeneratedValue Long id;
    private String fullName;
    private int passCount;
    private int pot;

    public Customer() {
    }

    public Customer(String fullName) {
        this.fullName = fullName;
        this.passCount = 0;
        this.pot = 0;
    }

    public Customer(String fullName, int passCount, int pot) {
        this.fullName = fullName;
        this.passCount = passCount;
        this.pot = pot;
    }

    public PotAvailability getPotAvailability() {
        if (passCount < 3 && pot < 10) return PotAvailability.NOT_ENOUGH_PASS_AND_POT_VALUE;
        if (passCount < 3) return PotAvailability.NOT_ENOUGH_PASS;
        if (pot < 10) return PotAvailability.NOT_ENOUGH_POT_VALUE;
        return PotAvailability.POT_AVAILABLE;
    }

    public void addPot(int amount) {
        pot += amount;
        passCount++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return passCount == customer.passCount &&
                pot == customer.pot &&
                Objects.equals(id, customer.id) &&
                Objects.equals(fullName, customer.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, passCount, pot);
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", fullName='" + fullName + '\'' + ", passCount=" + passCount + ", pot=" + pot + '}';
    }
}
