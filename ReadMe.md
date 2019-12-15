
## Chatty
WebChatServer based on Spring-boot
## Milestone
:white_check_mark: config Spring-boot environment <br>
:white_check_mark: open basic ws port <br>
:white_large_square: integrate with Redis <br>
:white_large_square: login authentication system <br>
:white_large_square: deploy to server
## Design
|URL|TO|
| -- | -- |
|/|Home page|
|/chat/XXX|Chat channel(ws)|

#### How to login
- Email + Password(Basic)
- Two step auth(If time permits)
### What can user do
- register
- login
- join a chat room
- etc
## Author
Ame-yu

### Config
HOST:redis-17610.c1.asia-northeast1-1.gce.cloud.redislabs.com

PORT:17610

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/gradle-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

