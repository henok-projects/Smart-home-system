# GUIDE
Later, you will connect to the Config Server with a Spring Boot 
application whose spring.application.name property identifies it as 
a-bootiful-client to the Config Server. 

This is how the Config Server knows which set of configuration to send to a specific client. It also 
sends all the values from any file named application.properties or 
application.yml in the Git repository. 

Property keys in more specifically 
named files (such as a-bootiful-client.properties) override those in 
application.properties or application.yml.
