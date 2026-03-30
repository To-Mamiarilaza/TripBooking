# Docker Setup Guide

This project is configured to run with Docker and Docker Compose. Follow the instructions below to run the application in Docker.

## Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+

## Quick Start

### Option 1: Run with Docker Compose (Recommended)

This will start both the PostgreSQL database and the Spring Boot application:

```bash
# Navigate to the project directory
cd /path/to/restapi

# Build and start the containers
docker-compose up --build

# The API will be accessible at http://localhost:8080
# Swagger UI will be available at http://localhost:8080/swagger-ui.html
```

To stop the containers:
```bash
docker-compose down
```

To stop and remove volumes (delete database):
```bash
docker-compose down -v
```

### Option 2: Run Docker Images Separately

**Build the application image:**
```bash
docker build -t voyage-api:latest .
```

**Run the database container:**
```bash
docker run -d \
  --name voyage-db \
  -e POSTGRES_DB=apirest \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=@@SUPER16 \
  -p 5432:5432 \
  postgres:16-alpine
```

**Run the application container:**
```bash
docker run -d \
  --name voyage-api \
  --link voyage-db:postgres \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/apirest \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=@@SUPER16 \
  -p 8080:8080 \
  voyage-api:latest
```

## Accessing the Application

- **API Base URL:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI Docs:** http://localhost:8080/v3/api-docs

## Environment Variables

Default environment variables are configured in `docker-compose.yml`. To override them:

1. Create a `.env` file in the project root
2. Add your custom variables:
   ```
   SPRING_DATASOURCE_PASSWORD=your_new_password
   SPRING_JPA_HIBERNATE_DDL_AUTO=validate
   ```
3. Run: `docker-compose up --build`

## Troubleshooting

### Port Already in Use
If port 8080 or 5432 is already in use, modify the port mappings in `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Use 8081 instead of 8080
```

### Database Connection Issues
Ensure the `postgres` service is healthy before the `restapi` service starts. Docker Compose will automatically wait for this with the `healthcheck` and `depends_on` configurations.

Check service status:
```bash
docker-compose ps
docker-compose logs postgres
docker-compose logs restapi
```

### Rebuild Without Cache
```bash
docker-compose up --build --no-cache
```

## Production Notes

For production deployments:
1. Change `ddl-auto` to `validate` or `update`
2. Use strong database passwords
3. Store secrets in environment variables or Docker secrets
4. Use specific image tags instead of `latest`
5. Configure resource limits in docker-compose.yml
6. Use health checks (already configured)
7. Set up proper logging and monitoring

## Cleanup

Remove all containers and volumes:
```bash
docker-compose down -v
```

Remove the built image:
```bash
docker rmi voyage-api:latest
```
