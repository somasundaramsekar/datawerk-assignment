Implementing Microservices
--------------------------
To implement the solution for a simple file upload and processing service, springboot along with spring cloud toolset has been used(Ref: [start.spring.io](http://start.spring.io))

Spring boot provides a opinionated framework on top of Spring 4.x(and above) to quickly implement a standalone applications with embedded runtime, shipped as a fat jar.

Along with Springboot, spring cloud tools(Netflix OSS) are used to create a true microservice, that can be scaled linearly on-demand, monitored and managed.

The tools used are 
*Spring cloud config*: Externalizing configuration from the application and enabling propagating the changes in from the configuration repository to the application. The subscribed application can be refreshed with /refresh endpoint.

*Spring Cloud bus*: Invoking /refresh in a large microservice deployments is quite impractical, will impact the scalability. Cloud bus(when used with messaging services like AMQP or Kafka) enables automatic propagation of changes to all the instances of the microservice. Spring cloud bus here is configured with RabbitMQ to propagate the changes to all the services.

*Eureka*: Microservices are expected to be location transparent, any number of instances should be identifiable in a transparent way to client. Eureka is a discovery service, that a microservice can register with a name, and Eureka will provide the clients with the list of instances registered with a given name, that can client can use to intelligently route the request.

*Ribbon*: Ribbon is a client side load balancer that can intelligently route request to the services. Spring `RestTemplate`automatically uses Eureka and Ribbon to perform client side load balancing.

*Hystrix*: Microservices architecture usually involves multiple services working in tandem, and failure must be treated as a first class citizen and appropriate failover must be provided. Hytrix implements the circuit breaker pa
![Implementation Architecture](http://i.imgur.com/u4Ld3bC.png)

###Assumptions & Approach
1. CloudAMQP is a hosted RabbitMQ service, it is being used as a cloud bus backbone and as a direct exchange for Hystrix fallback
2. H2 is used as datastore for dataprocessing service
3. File processing is submitted as a task to a `ExecutorService` that is executed on a separate thread.
4. Java 8 streams employed where possible
5. java.io streams are used to enable processing large files
6. Spring data JPA is used for persistence

file-upload-service can be accessed at port 9091