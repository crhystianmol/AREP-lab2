package com.masiv.callCenter.beans;

public class Agent {
    private String name;
    private String cedula;
    private String priority;
    public Agent(String name, String cedula, String priority){
        this.name=name;
        this.cedula=cedula;
        this.priority=priority;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCedula() {

        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public String getPriority() {

        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }



}
