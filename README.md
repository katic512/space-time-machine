# space-time-machine
Machine for space time travel.
# Problem
Your application should expose an API to submit your travel details:  
• Personal galactic identifier (PGI) (alphanumeric, always starts with a letter, between 5-10 characters)  
• Place  
• Date  
For a journey to be successful you need to make sure the same person (identified by the PGI) cannot travel to the same place and date twice. You don't want to create a paradox by someone meeting themselves!  
# Assumptions:
•	A person can have only one PGI(Personal galactic identifier) and there won’t be any duplicate registration.  
•	At the start of the application there can be multiple versions of a same person may be present at any given time, at different places.This can be explained by below diagram.  

Person K’s time line(space not represented here):  
 ![alt time loop image](https://github.com/katic512/space-time-machine/blob/main/src/main/resources/static/img/time_loop.png)
  
Kp – Person K from the past  
KF – Person K from the future  
Tp – Represents a past time in the time line  
TF – Represents a future time in the time line

As you can see between TP and TF there are two versions of K(KP and KF) present.

•	In order to avoid paradox we prevent same person from travelling to the same place at the same time twice.  
•	Also this make sure that a person can travel to different places at the same time because each travel can be done by different versions of the same person.  
# Technologies
• Java 1.8  
• Spring boot 2.6.0  
• H2 database  
• Spring JPA    
• Restful webservice  
• Junit 5  
# Links
•	[Swagger](https://github.com/katic512/space-time-machine/blob/main/docs/swagger.yaml)  
•	[postman collection](https://github.com/katic512/space-time-machine/blob/main/docs/postman_collection.json)
# Application execution Flow
![alt flow diagram](https://github.com/katic512/space-time-machine/blob/main/src/main/resources/static/img/app_flow.png)
# Out of scope:
•	Authentication and Authorization  
•	User management  	


