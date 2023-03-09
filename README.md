# Receipt Reward API Server
This project is written in Java using spring boot. This project fulfils the documented API in [api.yml](/api.yml).

## Get started
We'll use docker to run this project. The following instruction assumes you have docker installed. If you do not have docker setup, check out the [docker docs](https://docs.docker.com/desktop/install/mac-install/).

1. **Pull an image from Docker Hub**
```
docker image pull jingnu/receipt-server:latest
```

2. **Create and run a new container from the image we just pulled**
```
docker run --name test-jingnu -d -p8080:8080 jingnu/receipt-server:latest 
```
This command returns an id. Please take a note of the returned id for later debug purpose.

3. **Check if the container is running**,
```
docker ps | grep jingnu/receipt-server:latest
```
you should see an output like:
```
53e876405431   jingnu/receipt-server:latest   "java -jar receipt-s…"   4 minutes ago   Up 4 minutes   0.0.0.0:8080->8080/tcp   test-jingnu
```
In case you do not see the above output, use `docker logs <id-returned-from-step-2>` to debug.

4. **Start sending request to the server**
- POST to `http://localhost:8080/receipts/process` on Mac
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"9.00"}],"total":"9.00"}'
```
- POST to `http://localhost:8080/receipts/process` on Windows
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type: application/json" -d "{\"retailer\":\"M&M Corner Market\",\"purchaseDate\":\"2022-03-20\",\"purchaseTime\":\"14:33\",\"items\":[{\"shortDescription\":\"Gatorade\",\"price\":\"2.25\"},{\"shortDescription\":\"Gatorade\",\"price\":\"2.25\"},{\"shortDescription\":\"Gatorade\",\"price\":\"2.25\"},{\"shortDescription\":\"Gatorade\",\"price\":\"9.00\"}],\"total\":\"9.00\"}"
```

The POST request returns an id. Note down the id returned and use it for the GET request below
- GET to `http://localhost:8080/receipts/{id}/points`
```
curl  http://localhost:8080/receipts/<id-returned-from-step-4-POST>/points
```
The GET request returns the points the receipt was awarded.

5. **Stop docker container**. Once you're down interacting with the server, stop the running docker container:
```
docker stop <container-id-returned-from-step-2>
```

## Sameple requests and returns (on Mac)
Command 1
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"Target","purchaseDate":"2022-01-01","purchaseTime":"13:01","items":[{"shortDescription":"Mountain Dew 12PK","price":"6.49"},{"shortDescription":"Emils Cheese Pizza","price":"12.25"},{"shortDescription":"Knorr Creamy Chicken","price":"1.26"},{"shortDescription":"Doritos Nacho Cheese","price":"3.35"},{"shortDescription":" Klarbrunn 12-PK 12 FL OZ ","price":"12.00"}],"total":"35.35"}'
```
Output 1:
```
{"id": "5350ed3c-dfbe-4976-b00c-dbe0bb3e9bbd"}
```
Command 2
```
jane$ curl http://localhost:8080/receipts/5350ed3c-dfbe-4976-b00c-dbe0bb3e9bbd/points
```
Output 2
```
{"points": 28}
```
Command 3
```
jane$ curl http://localhost:8080/receipts/all
```
Output 3
```
[{
    "purchaseDate": "2022-01-01",
    "total": "35.35",
    "retailer": "Target",
    "purchaseTime": "13:01",
    "id": "0c98ef64-ccbf-413f-b02f-67ce46503e9f",
    "items": [
        {
            "price": "6.49",
            "shortDescription": "Mountain Dew 12PK"
        },
        {
            "price": "12.25",
            "shortDescription": "Emils Cheese Pizza"
        },
        {
            "price": "1.26",
            "shortDescription": "Knorr Creamy Chicken"
        },
        {
            "price": "3.35",
            "shortDescription": "Doritos Nacho Cheese"
        },
        {
            "price": "12.00",
            "shortDescription": " Klarbrunn 12-PK 12 FL OZ "
        }
    ],
    "points": 28
}]
```

