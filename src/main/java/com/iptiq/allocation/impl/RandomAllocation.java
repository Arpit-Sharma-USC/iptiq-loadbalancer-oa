package com.iptiq.allocation.impl;

import com.iptiq.allocation.LoadBalancerAllocation;

import java.util.Random;

public class RandomAllocation extends LoadBalancerAllocation {

    @Override
    public int nextProviderIndex(int registeredProvidersSize) {
        Random r = new Random();
        return r.nextInt(registeredProvidersSize);
    }
}
