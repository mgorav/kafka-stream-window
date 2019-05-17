package com.gonnect.kafka.stream.window;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.shipment.tracker")
public class ShipmentTrackingProperties {

    private String shipmentIds;

    public String getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(String shipmentIds) {
        this.shipmentIds = shipmentIds;
    }
}
