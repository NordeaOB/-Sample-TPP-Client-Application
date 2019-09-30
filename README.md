# Sample TPP Client Application
This is the Java centric "Web wallet" application that will thrive with ["Dogfooding"](https://en.wikipedia.org/wiki/Eating_your_own_dog_food)

### Building local, Nordea premises:

```mvn clean install``` 

### Github build
```mvn clean install -fpom-github.xml```


## Running local, Nordea premises:
```mvn spring-boot:run -Drun.profiles=local```
## Running local, Github build:
```mvn spring-boot:run -Drun.profiles=github -fpom-github.xml```

or

```java -jar target/obi-api-client-{version}.jar```

The github copy


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

## Nordea > Github
A zip file for github is constructed in /target/obi-webwallet-{version}-github-distribution.zip
it contains all the src code.
