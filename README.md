# last-hotel
Simple booking hotel API

## Context
CHALLENGE 

Post-Covid scenario: 
People are now free to travel everywhere but because of the pandemic, a lot of hotels went  bankrupt. Some former famous travel places are left with only one hotel. You’ve been given the responsibility to develop a booking API for the very last hotel in Cancun. 
The requirements are: 
- API will be maintained by the hotel’s IT department. 
- As it’s the very last hotel, the quality of service must be 99.99 to 100% => no downtime - For the purpose of the test, we assume the hotel has only one room available - To give a chance to everyone to book the room, the stay can’t be longer than 3 days and  can’t be reserved more than 30 days in advance.  
- All reservations start at least the next day of booking, 
- To simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59. - Every end-user can check the room availability, place a reservation, cancel it or modify it. - To simplify the API is insecure. 


## Assumptions
- We don't need to keep track of the room because there's only one room.
- Price of the booking was left out for simplicity's sake

## Features
- Swagger doc api is available at http://localhost:8080/swagger-ui/index.html#/

![image](https://user-images.githubusercontent.com/5640204/170620298-74118e63-18df-4576-9d3f-354fe4874389.png)

- CRUD api for customers and bookings.
- Bookings also have an endpoint to list the available booking's dates. This endpoint only shows the next N days (30) that user can make a booking.
- We have 2 properties to set how long in advance can customers book a hotel room and how long can they stay 
```
app.maxDaysBeforeBooking=30
app.maxStay=3
```
## Development choices

### Database
I used H2 in memory database, but same results could be achieved with any other relational db.
Went with H2 to be faster, but if this API would be deployed in prod I would have chosen another one and left h2 just for tests.

## Further Improvements

Create a docker image.
Improve unit tests and integration tests, add more test cases.
Create env variables.
Expand the api features to include room price management.
Swagger documentation can be improved.



