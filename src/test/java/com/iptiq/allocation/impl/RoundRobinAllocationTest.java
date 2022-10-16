package com.iptiq.allocation.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundRobinAllocationTest {

    private RoundRobinAllocation roundRobinAllocation;
    private int registeredProvidersSize = 0;

    @BeforeEach
    void setUp() {
        roundRobinAllocation = new RoundRobinAllocation();
    }

    @Test
    void nextProviderIndexWithZeroProviderSize() {
        assertEquals(-1, roundRobinAllocation.nextProviderIndex(registeredProvidersSize));
    }

    @Test
    void nextProviderIndexWithAvailableProviderSize() {
        registeredProvidersSize = 2;
        assertEquals(0, roundRobinAllocation.nextProviderIndex(registeredProvidersSize));
        assertEquals(1, roundRobinAllocation.nextProviderIndex(registeredProvidersSize));
    }
}