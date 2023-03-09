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

## Testing 
The following commands only works if you've completed the Get started section step 1-3.

Run the following commands in sequence

1. Test POST requests
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"Target","purchaseDate":"2022-01-01","purchaseTime":"13:01","items":[{"shortDescription":"Mountain Dew 12PK","price":"6.49"},{"shortDescription":"Emils Cheese Pizza","price":"12.25"},{"shortDescription":"Knorr Creamy Chicken","price":"1.26"},{"shortDescription":"Doritos Nacho Cheese","price":"3.35"},{"shortDescription":" Klarbrunn 12-PK 12 FL OZ ","price":"12.00"}],"total":"35.35"}'
```
Should return an id like `{"id": "0a2b15aa-af50-42cf-be23-6f14eaec0542"}`

2. Test GET requests
```
curl http://localhost:8080/receipts/0a2b15aa-af50-42cf-be23-6f14eaec0542/points
```
Should return `{"points": 28}`

3. Test award rules are configured correctly
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"}],"total":"9.00"}'
```
```
curl http://localhost:8080/receipts/all
```
the first cmd returns an id. The second cmd should return an array of two Receipts:
```
[
    {
        "total": "35.35",
        "purchaseDate": "2022-01-01",
        "retailer": "Target",
        "purchaseTime": "13:01",
        "id": "0a2b15aa-af50-42cf-be23-6f14eaec0542",
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
    },
    {
        "total": "9.00",
        "purchaseDate": "2022-03-20",
        "retailer": "M&M Corner Market",
        "purchaseTime": "14:33",
        "id": "43f85eb1-87b6-46a5-9acc-b84a776bcb58",
        "items": [
            {
                "price": "2.25",
                "shortDescription": "Gatorade"
            },
            {
                "price": "2.25",
                "shortDescription": "Gatorade"
            },
            {
                "price": "2.25",
                "shortDescription": "Gatorade"
            },
            {
                "price": "2.25",
                "shortDescription": "Gatorade"
            }
        ],
        "points": 109
    }
]
```

4. Test exceptions are thrown when appropriate
- This cmd returns `Invalid input provided. Property 'total' is required` because the required property `total` is missing from the receipt
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"}]}'
```

- This cmd returns `Invalid input provided. Property 'shortDescription' is required` because the required property `shortDescription` is missing from one of the items
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[{"price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"}],"total":"9.00"}'
```

- This cmd returns `Invalid input provided: Unparseable date: "2022/03/20". Property 'purchaseDate' must be of 'yyyy-MM-dd' pattern.`
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022/03/20","purchaseTime":"14:33","items":[{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"},{"shortDescription":"Gatorade","price":"2.25"}],"total":"9.00"}'
```

- A request with a bad JSON format returns something like `Invalid input provided: 10.a8. Property 'total' must be of '^\d+\.\d{2}$' pattern.`
```
 curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"Target","purchaseDate":"2022-01-01","purchaseTime":"13:01","items":[{"shortDescription":"Mountain Dew 12PK","price":"6.49"},{"shortDescription":"Emils Cheese Pizza","price":"12.25"},{"shortDescription":"Knorr Creamy Chicken","price":"1.26"},{"shortDescription":"Doritos Nacho Cheese","price":"3.35"},{"shortDescription":" Klarbrunn 12-PK 12 FL OZ ","price":"12.00"}],"total":"10.a8"}'
```

- This cmd returns `Empty 'items' not allowed.`
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"M&M Corner Market","purchaseDate":"2022-03-20","purchaseTime":"14:33","items":[],"total":"9.00"}'
```

- Run the following cmd twice. The first time it'll return an id, the second time it returns `Request failed idempotency check. Receipt already exist.`
```
curl http://localhost:8080/receipts/process -X POST -H "Content-Type:application/json" -d '{"retailer":"Target","purchaseDate":"2022-01-01","purchaseTime":"13:01","items":[{"shortDescription":"Mountain Dew 12PK","price":"6.49"},{"shortDescription":"Emils Cheese Pizza","price":"12.25"},{"shortDescription":"Knorr Creamy Chicken","price":"1.26"},{"shortDescription":"Doritos Nacho Cheese","price":"3.35"},{"shortDescription":" Klarbrunn 12-PK 12 FL OZ ","price":"12.00"}],"total":"10.18"}'
```

- A wrong id returns `No receipt found for id 0a2b15aa-af50-42cf-be23-6f14eaec0542.`
```
curl http://localhost:8080/receipts/0a2b15aa-af50-42cf-be23-6f14eaec0542/points
```
