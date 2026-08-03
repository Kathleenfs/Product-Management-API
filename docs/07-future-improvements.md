# Future Improvements

This section contains features and improvements planned for future versions of the project, focusing on production-ready practices, security, infrastructure, and observability.

---

## Security

- [ ] Move JWT secret to environment variables
- [ ] Store secrets using AWS Secrets Manager
- [ ] Implement Refresh Token flow
- [ ] Implement JWT token revocation (Blacklist)
- [ ] Password reset flow
- [ ] Email verification
- [ ] Account lock after multiple failed login attempts
- [ ] Rate limiting for authentication endpoints

---

## Infrastructure

- [ ] Dockerize the application
- [ ] Deploy to AWS
- [ ] Configure CI/CD pipeline (GitHub Actions)
- [ ] Configure HTTPS
- [ ] Externalize application configuration
- [ ] Centralized logging

---

## Observability

- [ ] Spring Boot Actuator
- [ ] Prometheus metrics
- [ ] Grafana dashboards
- [ ] OpenTelemetry distributed tracing

---

## Documentation

- [ ] API versioning
- [ ] Sequence diagrams
- [ ] Authentication flow diagram
- [ ] Deployment guide
- [ ] Architecture Decision Records (ADR)

---

## Testing

- [ ] Increase unit test coverage
- [ ] Integration tests with Testcontainers
- [ ] Performance tests
- [ ] Security tests

---

## Notes

Some implementation choices in this project were intentionally simplified to facilitate learning.

For example:

- The JWT secret is currently stored in the application configuration.
- In a production environment, secrets should be stored securely using environment variables or a dedicated secrets management service such as AWS Secrets Manager.