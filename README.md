## What is done:
- Basic task
- Additional task 1
- Additional task 2
- Additional task 3
- Additional task 4

## And moreover:
- [docker-compose.yaml](https://github.com/IliaDoktorov/TicketServices/blob/main/docker-compose.yml) which launches ticketservice, ticketreplicator, ticketdatabase, zookeeper, kafka1 brocker, kafka2 brocker, kafkaui, redis.
- [Dockerfile](https://github.com/IliaDoktorov/TicketServices/blob/main/TicketService/Dockerfile) for TicketService
- [Dockerfile](https://github.com/IliaDoktorov/TicketServices/blob/main/TicketReplicator/Dockerfile) for TicketReplicator.
- [Dockerfile](https://github.com/IliaDoktorov/TicketServices/blob/main/TicketService/db.Dockerfile) for PostgreSQL database. Tables are created automatically using tables.sql as well as some entities for tickets, routes and transporters.
- [Dockerfile](https://github.com/IliaDoktorov/TicketServices/blob/main/TicketReplicator/redis.Dockerfile) for Redis.
- [Jenkinsfile](https://github.com/IliaDoktorov/TicketServices/blob/main/Jenkinsfile) to automate builds, steps are:
	1. Clone repo
	2. Build TicketService and TicketReplicator
	3. Cleanup docker images for ticketservice, ticketreplicator, ticketdatabase from locally launched Docker
	4. Cleanup respective contriners as well
	5. Create new images
	6. Create and launch new contriners

## Instructions for Jenkins Pipeline:
- Create a Pipeline with any title you want and go to Configure
- Tick GitHub project and paste https://github.com/IliaDoktorov/TicketServices.git/
- Scroll down to Pipeline Definition
- Select Pipeline scriot from SCM
- Select SCM as Git
- Since GitHub currently doesn't support username\password login for Jenkins, you will need to generate Fine-drained token in GitHub(could be provided by author)
- Paste Repository URL in format
  ```
  https://<Fine-drained_token>github.com/IliaDoktorov/TicketServices.git/
  ```
#### Optionally
- You could enable "GitHub hook trigger for GITScm polling" for triggering build on commit. In that case you will need to contact with author and request to create a Webhook for your Jenkins server.

## Ideas how to improve solution
- Add interfaces for DAO and Service classes to meet DI and OOP principles.
- Create separate tables for DeparturePoint and DestinationPoint.
- Create DTO objects for Route, User, Transporter and Ticket to use in JSON REST communication(it might be useful if read-only requests, where we want to get human-frandly view of our enities, i.e. with names and titles instead of ids)
- Use Lombock to reduce boilerplate code.
- Allow to create tickets without route(need to make sure that such tickets are not available for reservation) and route without tranporter(need to make sure such routes are not available for ticket creation).
- Add support for different timezones.
- Solution has almost identical @ExceptionHandler methods which looks like a code duplication. Reason for that is we might want different behaviour for different errors, so these methods basically are templates.
- As a consequence, we might have own list of error codes for different errors to let our frontend know what exactly went wrong(for example add an errorCode for DB concurrent issues that frontend could re-sent failed request).
- Define a constant instead of duplicating messages like "Ticket with provided id not found", or get rid of them completelly and send only ErrorCode.
- Create indexes in DB for most used rows(e.g. reserved_by in ticket table).
- Responce codes are only 200, 400, 401 and 403 so it would be good idea to add 404 for example for cases when we cannot find specified entity. It could be done by adding HttpStatus to RequestException and propagate it in @ExceptionHandler.
- DAO for available tickets has huge ugly sql query. It could be fixed by using Spring DataJPA with ExampleMatchers.
- Allows to use ticket filters separatelly.
- Create separate table for roles.
