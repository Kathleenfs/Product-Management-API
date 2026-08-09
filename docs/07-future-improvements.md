# Future Improvements

This section contains features and improvements planned for future versions of the project, focusing on production-ready practices, security, infrastructure, testing, and observability.

---

## Security

- [x] Externalize JWT secret using environment variables
- [x] Externalize JWT expiration configuration
- [ ] Store secrets using AWS Secrets Manager
- [ ] Implement Refresh Token flow
- [ ] Implement JWT token revocation (Blacklist)
- [ ] Implement password reset flow
- [ ] Implement email verification
- [ ] Implement account lock after multiple failed login attempts
- [ ] Implement rate limiting for authentication endpoints

---

## Infrastructure

- [ ] Dockerize the application
- [ ] Deploy to AWS
- [ ] Configure CI/CD pipeline using GitHub Actions
- [ ] Configure HTTPS
- [ ] Externalize environment-specific application configuration
- [ ] Implement centralized logging

---

## Observability

- [ ] Add Spring Boot Actuator
- [ ] Configure Prometheus metrics
- [ ] Create Grafana dashboards
- [ ] Implement distributed tracing using OpenTelemetry

---

## Documentation

- [ ] Implement API versioning
- [ ] Create sequence diagrams
- [ ] Create authentication and authorization flow diagrams
- [ ] Create deployment guide
- [ ] Add Architecture Decision Records (ADR)

---

## Testing

- [ ] Increase unit test coverage
- [ ] Add integration tests using Testcontainers
- [ ] Add performance tests
- [ ] Add security tests

---

## Security Notes

Sensitive configuration must not be stored directly in the source code or committed to the repository.

The JWT secret is currently provided through an environment variable:

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:3600000}