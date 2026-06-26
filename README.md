# spring-boot-toolkit-starter

A production-ready Spring Boot starter that gives any application standardized API responses, global exception handling, request logging, correlation IDs, execution timing, and auditing through one dependency.

## Features

- Standard JSON response envelope using `ResponseBodyAdvice`
- Global exception responses with `@RestControllerAdvice`
- Request logging with method, URL, status, IP, user, execution time, and optional headers
- `X-Correlation-ID` support backed by SLF4J MDC
- `@TrackExecution` method timing with Spring AOP
- `@Audit(action = "...")` event recording with a replaceable `AuditService`
- Spring Boot 3 auto-configuration using `AutoConfiguration.imports`
- Feature toggles through `toolkit.*` properties

## Installation

```xml
<dependency>
    <groupId>io.github.kushagrasinghga</groupId>
    <artifactId>spring-boot-toolkit-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

```yaml
toolkit:
  logging:
    enabled: true
    include-headers: false
  audit:
    enabled: true
  response:
    enabled: true
  timer:
    enabled: true
```

## Example Response

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Laptop"
  },
  "timestamp": "2026-06-26T10:30:00Z",
  "path": "/api/products",
  "correlationId": "cf98ab12-9938-4f35-bb5d-7fadcafc42cb"
}
```

## Example Usage

```java
@Audit(action = "CREATE_PRODUCT")
@TrackExecution
public Product create(Product product) {
    return repository.save(product);
}
```

## Run the Sample

```bash
mvn -pl toolkit-samples spring-boot:run
```

Then create a product:

```bash
curl -X POST http://localhost:8080/api/products \
  -H 'Content-Type: application/json' \
  -d '{"name":"Laptop","price":999.0}'
```

## Roadmap

- v1.1.0: richer logging body capture with redaction
- v1.2.0: Micrometer metrics integration
- v1.3.0: pluggable audit storage
- v2.0.0: OpenTelemetry and distributed tracing integrations
