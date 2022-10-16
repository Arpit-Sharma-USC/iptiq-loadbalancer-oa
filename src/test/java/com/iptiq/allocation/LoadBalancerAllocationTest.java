package com.iptiq.allocation;

import com.iptiq.allocation.impl.RandomAllocation;
import com.iptiq.allocation.impl.RoundRobinAllocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class LoadBalancerAllocationTest {

    private LoadBalancerAllocation loadBalancerAllocation;
    private int expectedNextProviderIndex = 1;

    @Mock
    private RoundRobinAllocation roundRobinAllocation;
    @Mock
    private RandomAllocation randomAllocation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void nextProviderIndexRoundRobin() {
        Mockito.when(roundRobinAllocation.nextProviderIndex(any(Integer.TYPE))).thenReturn(expectedNextProviderIndex);
        loadBalancerAllocation = roundRobinAllocation;
        assertEquals(loadBalancerAllocation.nextProviderIndex(1), expectedNextProviderIndex);
    }

    @Test
    void nextProviderIndexRandom() {
        Mockito.when(randomAllocation.nextProviderIndex(any(Integer.TYPE))).thenReturn(expectedNextProviderIndex);
        loadBalancerAllocation = randomAllocation;
        assertEquals(loadBalancerAllocation.nextProviderIndex(1), expectedNextProviderIndex);
    }
}