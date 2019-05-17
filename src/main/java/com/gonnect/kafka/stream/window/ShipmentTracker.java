package com.gonnect.kafka.stream.window;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@EnableConfigurationProperties(ShipmentTrackingProperties.class)
@EnableBinding(KafkaStreamsProcessor.class)
public class ShipmentTracker {

    private final ShipmentTrackingProperties shipmentTrackingProperties;
    private final TimeWindows timeWindows;

    public ShipmentTracker(ShipmentTrackingProperties shipmentTrackingProperties, TimeWindows timeWindows) {
        this.shipmentTrackingProperties = shipmentTrackingProperties;
        this.timeWindows = timeWindows;
    }

    @StreamListener("input")
    @SendTo("output")
    public KStream<Integer, ShipmentStatus> process(KStream<Object, Shipment> input) {
        return input
                .filter((key, shipment) -> shipmentIds().contains(shipment.getId()))
                .map((key, value) -> new KeyValue<>(value, value))
                .groupByKey(Serialized.with(new JsonSerde<>(Shipment.class), new JsonSerde<>(Shipment.class)))
                .windowedBy(timeWindows)
                .count(Materialized.as("shipment-counts"))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key().getId(), new ShipmentStatus(key.key().getId(),
                        value, Instant.ofEpochMilli(key.window().start()).atZone(ZoneId.systemDefault()).toLocalTime(),
                        Instant.ofEpochMilli(key.window().end()).atZone(ZoneId.systemDefault()).toLocalTime())));
    }

    private Set<Integer> shipmentIds() {
        return StringUtils.commaDelimitedListToSet(shipmentTrackingProperties.getShipmentIds())
                .stream().map(Integer::parseInt).collect(Collectors.toSet());
    }
}
