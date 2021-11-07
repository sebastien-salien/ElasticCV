# elasticCV

## Launch ElasticSearch in docker
In elasticCV :
Run `docker-compose up`

## Launch Backend
In elasticCV\elasticCV_Backend : 
Run `mvn spring-boot:run`

Lanch in http://localhost:8080/


## API
POST http://127.0.0.1:8080/api/cv 			Add CV
Body : application/json
{
  "title" : "my great CV",
  "skill" : ["C", "java", "php" ]
}

POST http://127.0.0.1:8080/api/cv/upload
Body : multipart/form-data
File : .txt/.pdf/.docx
1st Line : title
Then skill on each line :
cv title
skill1
skill2
skill3
skill4


GET http://127.0.0.1:8080/api/cv/search?q=<skill>		Search skill in cv


GET http://127.0.0.1:8080/api/cv/search?q=<skill1>,<skill2>	Search multiple skill in cv

All the application log are in elasticCV\elasticCV_Backend\log\elasticcv.log

## Launch Frontend
In elasticCV\elasticCV_Frontend : 
Run `npm install` then `ionic serve`

Lanch in http://localhost:8100/
