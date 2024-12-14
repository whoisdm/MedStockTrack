package com.medstocktrack.medstockapp.model;

public class Producer {
    private final String producerName;
    private final String producerCountry;

    public Producer(String producerName, String producerCountry) {
        this.producerName = producerName;
        this.producerCountry = producerCountry;
    }

    public String getProducerName() {
        return producerName;
    }

    public String getProducerCountry() {
        return producerCountry;
    }
}
