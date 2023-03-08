# Receipt Reward API Server
This project is written in Java using spring boot. This project fulfils the documented API in [api.yml](/src/main/java/com/jingnu/receipt/processor/api.yml).

## Get started
We'll use docker to this project. The following instruction assumes you have docker installed.
1. Pull an image from Docker Hub
```
docker image pull jingnu/receipt-server
```
2. Create and run a new container from the image we just pulled
```
docker run --name test -d -p8080:8080 jingnu/receipt-server:latest 
```
3. POST to endpoint `/receipts/process`
```
curl -X POST \
-H "Content-Type:application/json" \
-d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"9.00"}],"total":"9.00"}' \
http://localhost:8080/receipts/process 
```
4. GET to endpoint `receipts/{id}/points`
```
curl  http://localhost:8080/receipts/b7bc79f7-04d2-4cb1-a59b-55927044/points
```
