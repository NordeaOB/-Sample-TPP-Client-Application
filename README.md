# Sample TPP Client Application
This is a Java centric sample "TPP client" application that will enable communication to the open API of Nordea Openbanking.
You will need to signup and create an application in order for making requests, as you need a clientid and a secret

### Building local:

```mvn clean install``` 

## Running local, Github build:
```mvn spring-boot:run -Drun.profiles=github -fpom.xml```

or

```java -jar -Dspring.profiles.active=github target/obi-api-client-{version}.jar```

#### Proxies
If you are running WebWallet from inside a secured network and you are using the developer portal/sandbox that is in the public network. 
Then you need to set the Company proxy for the jvm, otherwise the connection to external network does not work. 

```-Dhttp.useProxy=true -Dhttp.proxyHost=proxy.domain.net -Dhttps.proxyHost=proxy.domain.net -Dhttp.proxyPort=8080 -Dhttps.proxyPort=8080```

#### Browser 
Go to: [http://localhost:7000](http://localhost:7000)

# Short description

## Security

[https://spring.io/guides/gs/securing-web/#initial](Springboot websecurity)
Using OAuth authentication through Nordea

Your application hosting need to support SSL handshaking security 

## Disclaimer OAUTH 
When the OAuth authentication process returns to a localhost, a secured endpoint(https) is assumed.
You can just remove the 's' in the return url and proceed with the given 'code' 
