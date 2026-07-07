# Inventory Management — Docker Hub CI/CD

Jenkins builds and pushes the app image to **Docker Hub** on every push to `main`.

```
Git push (main) → Jenkins → ./gradlew test → docker build → docker push
                                                              ↓
                                    dikshanta07/inventory-management:latest
                                    dikshanta07/inventory-management:BUILD_NUMBER
```

---

## 1. Docker Hub

1. Create repository: **`dikshanta07/inventory-management`**
2. Create an **Access Token** (Account Settings → Security → Read & Write)

---

## 2. Jenkins

### Prerequisites on Jenkins server

- Java 21, Git, Docker
- Jenkins user in `docker` group: `sudo usermod -aG docker jenkins`

### Credential (required)

**Manage Jenkins → Credentials → Add:**

| Field | Value |
|-------|-------|
| Kind | Username with password |
| ID | `docker-hub-credentials` |
| Username | `dikshanta07` |
| Password | Docker Hub access token |

### Pipeline job

1. **New Item** → **Pipeline** → `inventory-management`
2. **Pipeline script from SCM** → your Git repo → branch `main`
3. **Script Path:** `Jenkinsfile`
4. Save → **Build Now**

### Auto-trigger on push

- **GitHub webhook:** Job → **GitHub hook trigger for GITScm polling** → webhook URL `http://JENKINS:8080/github-webhook/`
- **Or polling:** already enabled in `Jenkinsfile` (every 5 minutes)

---

## 3. What Jenkins does

On each push to `main` / `master`:

1. Checkout
2. `./gradlew test`
3. `docker build` → tags `dikshanta07/inventory-management:BUILD_NUMBER` and `:latest`
4. `docker push` both tags to Docker Hub

No VPS or SSH deploy — image only.

---

## 4. Run the published image locally (optional)

```bash
cp .env.example .env
# set POSTGRES_PASSWORD, PGADMIN_DEFAULT_PASSWORD, JWT_SECRET_KEY

docker compose -f deployment/docker-compose.hub.yml pull
docker compose -f deployment/docker-compose.hub.yml up -d
curl http://localhost:8080/api/v1/actuator/health
```

Or pull the app image alone:

```bash
docker pull dikshanta07/inventory-management:latest
```

---

## 5. Troubleshooting

| Problem | Fix |
|---------|-----|
| Push fails: unauthorized | Check credential ID `docker-hub-credentials`, username `dikshanta07`, valid token |
| Push fails: docker not found | Install Docker on Jenkins agent; add `jenkins` to `docker` group |
| Tests fail | Fix failing tests before push — push stage only runs after test passes |
| Wrong image name | Image is always `dikshanta07/inventory-management` (set in `Jenkinsfile`) |
