package com.example.katabforbank;

public record PotResponse(int potValue, int passCount, PotAvailability potAvailability, String potOwner) {
}
