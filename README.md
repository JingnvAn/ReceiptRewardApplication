# Receipt Reward API Server
This project is written in Java using spring boot. This project fulfils the documented API in [api.yml](/src/main/java/com/jingnu/receipt/processor/api.yml).

## Get started
We'll use docker to run this project. The following instruction assumes you have docker installed. If you do not have docker setup, check out the [docker docs](https://docs.docker.com/desktop/install/mac-install/).
1. Pull an image from Docker Hub
```
docker image pull jingnu/receipt-server:latest
```
2. Create and run a new container from the image we just pulled
```
docker run --name test-jingnu -d -p8080:8080 jingnu/receipt-server:latest 
```
This command returns an id. Please take a note of the returned id for later debug purpose.
3. Check if the container is running,
```
docker ps | grep jingnu/receipt-server:latest
```
you should see an output like:
```
53e876405431   jingnu/receipt-server:latest   "java -jar receipt-s…"   4 minutes ago   Up 4 minutes   0.0.0.0:8080->8080/tcp   test-jingnu
```
In case you do not see the above output, use `docker logs <id-returned-from-step-2>` to debug.
4. Start sending request to the server
- POST to `http://localhost:8080/receipts/process`
```
curl -X POST \
-H "Content-Type:application/json" \
-d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"9.00"}],"total":"9.00"}' \
http://localhost:8080/receipts/process 
```
The POST request returns an id. Note down the id returned and use it for the GET request below
- GET to `http://localhost:8080/receipts/{id}/points`
```
curl  http://localhost:8080/receipts/<id-returned-from-step-4-POST>/points
```
The GET request returns the points the receipt was awarded.
