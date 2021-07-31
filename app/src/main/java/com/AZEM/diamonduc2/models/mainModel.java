package com.AZEM.diamonduc2.models;

public class mainModel {
    private String modelName;
    private String costString;
    private Integer cost;
    private String type;

    public mainModel(String modelNAme, String costString, Integer cost, String type) {
        this.modelName = modelNAme;
        this.costString = costString;
        this.cost = cost;
        this.type = type;
    }


    public mainModel() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelNAme) {
        this.modelName = modelNAme;
    }

    public String getCostString() {
        return costString;
    }

    public void setCostString(String costString) {
        this.costString = costString;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
