It is spring-boot-starter that provide new layer of abstraction around standart spring-kafka liblary for hadling msgs from kafka.<br>

To add to spring-boot project:<br>
1. Add to pom<br>
		<dependency>
			<groupId>com.hydroyura.springboot.kafka.msg.wrapper</groupId>
			<artifactId>consumer-starter</artifactId>
			<version>0-SNAPSHOT</version>
		</dependency>
2. Specify kafka server in application.yaml:<br>
    kafka-msg-wrapper:<br>
      producer:<br>
        url: localhost:9092<br>
   
3. Implement interface MsgWrapperProcessor<T> <br>
   T - type of msg dto

4. Create beans of implemented interfqaces from 3.<br>
5. Starter will invoke nessesary implemention of MsgWrapperProcessor<T> for correspond msg type.
