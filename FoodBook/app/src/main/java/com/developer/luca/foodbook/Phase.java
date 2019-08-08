package com.developer.luca.foodbook;

// Classe che rappreseenta una fase di preparazione della ricetta
public class Phase {

    private int phaseNumber;
    private String phaseDescription;

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public String getPhaseDescription() {
        return phaseDescription;
    }

    public void setPhaseDescription(String phaseDescription) {
        this.phaseDescription = phaseDescription;
    }
}
