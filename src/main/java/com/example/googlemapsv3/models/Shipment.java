package com.example.googlemapsv3.models;

import java.io.Serializable;

public class Shipment implements Serializable {

    private int shipmentId;
    private int customerId;
    private String startTime;
    private String endTime;
    private String origin;
    private String destination;
    private String shipmentStatus;

    public int getShipmentId() {
        return shipmentId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getOrigin() {
        return origin;
    }
    public String getDestination() {
        return destination;
    }
    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
