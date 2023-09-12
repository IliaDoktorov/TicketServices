FROM postgres
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB tickets_db
COPY TicketService/tables.sql /docker-entrypoint-initdb.d/tables.sql