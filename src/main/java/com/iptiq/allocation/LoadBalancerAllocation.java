package com.iptiq.allocation;

public abstract class LoadBalancerAllocation {

    public abstract int nextProviderIndex(int registeredProvidersSize);
}
