# iptiQ LoadBalancer
The main goal of this project is to demonstrate how a load balancer would work in practice. There are two allocation techniques available within this load balancer namely: 
* Random allocation 
* Round-robin allocation

There is a basic framework present to import, instantiate and use the `LoadBalancer.` 

## About the environment
* This is a maven project built on Java 11. The internal dependencies are pulled via the `pom.xml`
* The project showcases basic Unit tests necessary for the scope of the assignment. 
    > Note: There aren't comprehensive Unit tests as a fully functional peoduction application must have but enough to exhibit TDD.

## Assumptions
* How the health of a Provider is determined was assumed to be beyond the scope of this assignment, thus I used a random Boolean value each time a call to `Provider::check()` is made.
    


 
