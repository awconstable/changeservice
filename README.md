# changeservice
A change repository to measure team performance

[![CircleCI](https://circleci.com/gh/awconstable/changeservice.svg?style=shield)](https://circleci.com/gh/awconstable/changeservice)
![CodeQL](https://github.com/awconstable/changeservice/workflows/CodeQL/badge.svg)
[![codecov](https://codecov.io/gh/awconstable/changeservice/branch/main/graph/badge.svg)](https://codecov.io/gh/awconstable/changeservice)
[![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/awconstable/changeservice.svg)](https://libraries.io/github/awconstable/changeservice)
[![dockerhub](https://img.shields.io/docker/pulls/awconstable/changeservice.svg)](https://cloud.docker.com/repository/docker/awconstable/changeservice)

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* No security whatsoever - anonymous users can easily delete or alter all data

### Run app as a Docker container

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop changeservice
docker rm changeservice
docker pull awconstable/changeservice
docker run --name changeservice -d -p 8080:8080 --network <mongo network> -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mondo db> -e server_ssl_key-store-type=<keystore type - PKCS12> -e server_ssl_key-store=/changeservice.p12 -e server_ssl_key-store-password=<password> -e server_ssl_key-alias=<alias> -e spring_cloud_consul_host=<consul host> -e spring_cloud_consul_port=<consul port> -v <cert path>:/changeservice.p12 awconstable/changeservice:latest
```

### Example deployment

```
  POST http://localhost:8088/api/v1/change
  Accept: application/json
  Cache-Control: no-cache
  Content-Type: application/json
  
  [{
    "changeRequestId": "c1",  
    "description": "change c1",
    "applicationId": "a1",  
    "created": "2020-11-30T13:00:00.000+00:00", 
    "started": "2020-11-30T13:00:00.000+00:00",    
    "finished": "2020-11-30T13:00:00.000+00:00", 
    "closed": "2020-11-30T13:00:00.000+00:00", 
    "failed": false,
    "source": "test"
  }]
```
