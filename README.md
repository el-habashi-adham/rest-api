# Automated teller machine (ATM)

This application simulates a REST API for an ATM. 
It supports two operation: add cash and withdraw cash.

## Run the application

In the root directory of the project, type:

```bash
./mvnw spring-boot:run
```

## Run the unit tests

In the root directory of the project, type:

```bash
./mvnw clean test
```

## Description of the implementation

The application exposes two endpoints: one for cash withdrawal and one for adding cash.

Below is the description of the API:

```
GET /operations/all

Get all entries from the database
```

```
GET /operations/withdraw/{amount}

Withdraw cash from the ATM.
```
 
Request:

| Param  | Description | Type |
|--------|-------------|------|
| amount | The amount of cash to withdraw | Integer

Response:

```
The response returns a JSON with the following format:

For example,

{
    "success": true,
    "message": "{100=1}"
}

If the operation is successful, the ATM will return a message detailing the denomination and the bill count.
The format of the message is {<denomination>=<count>}
```
Response description:

| Field | Type | Description |
|-------|------| ------------|
|success| boolean | Request was successful or not
|message| string | Message detailing the denomination and bill count withdrawn from the ATM or an error message

Possible return codes:

|Status code | Description |
|------------| ------------|
|200         | Operation successful |
|400         | Bad request, the input is invalid |

```
POST /operations/add

Add cash to the ATM
```

Request:

```
The request is a JSON with the following format:

{
    <denomination>: <count>,
    ...
}

The request can have multiple entries at once.
```

| Field  | Description | Type |
|--------|-------------|------|
| denomination | Denomination to add in the ATM | String
| count | The number of bills of the specified denomination | Integer

Response:

```
The response returns a JSON with the following format:

For example,

{
    "success": true,
    "message": "The operation completed successfully"
}

```

Possible return codes:

|Status code | Description |
|------------| ------------|
|200         | Operation successful |
|400         | Bad request, the input is invalid |
|500         | The ATM reached the maximum capacity

## Notes

The application uses an in-memory database, H2.

The input validation is performed by the InputValidator class.
The constraints for the input values are defined in the application.properties file. The aim is to provide an easy way 
to change the values for the constraints.

The API was tested using Postman.

No integration tests were performed.


 