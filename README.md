
### Description

A Springboot microservices application which allows users to create a quiz based on multiple categories where questions will be provided by question-service, participate in a live quiz using quiz-service. This application demonstrates working of Eureka service discovery server, Spring cloud api gateway and openfeign to make declarative rest api calls from one microservice to another. 

![QuizApp Microservices Architecture](/Architecture.jpg) 

1. Create question-service microservice
2. Create quiz-service microservice
3. Setting Up Eureka Server `service-registry` Microservice & other microservice to register to eureka. <br>
   3.1 Create a new project with the following dependencies
   ```
   <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
   ```
   3.2 resources/application.properties file
   ```
    spring.application.name=service-registry
    server.port=8761
    eureka.instance.hostname=localhost
    eureka.client.fetch-registry=false
    eureka.client.register-with-eureka=false
   ```
   3.3 Annotate main class with `@EnableEurekaServer`
   3.4 Changes to be made on other microservices (question-service, quiz-service, api-gateway)
   ```
   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
   ```
4. Setting up Question-service as feign client in Quiz-service to make declaratve rest api calls (quiz->question) <br>
   4.1 Add dependency to pom.xml of Quiz-service
   ```
   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>
   ```
   4.2 Annotate main class with `@EnableFeignClients`
   4.3 set up an interface in Quiz-service which can be used as a proxy to make rest api calls to question service from quiz service
   ```
   @FeignClient("QUESTION-SERVICE")
    public interface QuizInterface {
        // generate
        //It will call to ->  http://localhost:8080/question/generate of question-service
        @GetMapping("question/generate")
        public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category,
                                                                 @RequestParam Integer numQuestions);
        // getQuestions(questionId)
        //It will call to ->  http://localhost:8080/question/getQuestions of question-service
        @PostMapping("question/getQuestions")
        public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds);
    
        // getScore
        //It will call to -> http://localhost:8080/question/getScore of question-service
        @PostMapping("question/getScore")
        public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
    
    }
   ```
   4.4 Sample API call `List<Integer> qustions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();`

5. Setting up `api-gateway` microservice for the application <br>
   5.1 Create a new project api-gateway with following dependencies
   ```
   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
   ```
   5.2 configure application.properties
   ```
    spring.application.name=api-gateway
    server.port=8765
    spring.cloud.gateway.discovery.locator.enabled=true
    spring.cloud.gateway.discovery.locator.lower-case-service-id=true
   ```
   
