package com.iptiq.allocation.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomAllocationTest {

    @Mock
    private RandomAllocation randomAllocation;


    @BeforeEach
    void setUp() {
        randomAllocation = new RandomAllocation();
    }


    @Test
    void nextProviderIndexWithSingleProvider() {
        //when there is only 1 provider always use that if its available
        assertEquals(0, randomAllocation.nextProviderIndex(1));
    }

}