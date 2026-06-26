# Usage

Add the starter:

```xml
<dependency>
    <groupId>io.github.kushagrasinghga</groupId>
    <artifactId>spring-boot-toolkit-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Configure features:

```yaml
toolkit:
  logging:
    enabled: true
  audit:
    enabled: true
  response:
    enabled: true
  timer:
    enabled: true
```

Use annotations where needed:

```java
@Audit(action = "CREATE_PRODUCT")
@TrackExecution
public Product create(Product product) {
    return repository.save(product);
}
```
