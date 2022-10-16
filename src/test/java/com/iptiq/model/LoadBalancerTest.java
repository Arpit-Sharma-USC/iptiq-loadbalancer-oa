package com.iptiq.model;

import com.iptiq.allocation.impl.RoundRobinAllocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadBalancerTest {

    private LoadBalancer loadBalancer;
    private String firstProviderIdentifier = "firstProvider";
    private String secondProviderIdentifier = "secondProvider";

    @BeforeEach
    void setUp() {
        loadBalancer = new LoadBalancer(new RoundRobinAllocation());
        loadBalancer.register(new Provider(firstProviderIdentifier));
        loadBalancer.register(new Provider(secondProviderIdentifier));
    }

    @Test
    void registerTwoProviders() {
        List<Provider> providerList = loadBalancer.getRegisteredProviders();
        assertEquals(firstProviderIdentifier, providerList.get(0).get());
        assertEquals(secondProviderIdentifier, providerList.get(1).get());
    }

    @Test
    void unregisterBothProviders() {
        List<Provider> providerList = loadBalancer.getRegisteredProviders();
        loadBalancer.unregister(firstProviderIdentifier);
        assertEquals(1, loadBalancer.getRegisteredProviders().size());
        loadBalancer.unregister(secondProviderIdentifier);
        assertEquals(0, loadBalancer.getRegisteredProviders().size());
    }

}