# Inventory Management — VPS Deployment Guide

Deploy the Spring Boot backend with PostgreSQL and pgAdmin using Docker Compose, and redeploy automatically on every push via Jenkins.

---

## Architecture

```
Git push → Jenkins (build + test + docker build) → SSH to VPS → git pull → docker compose up -d
                                                              ├── postgres (data volume)
                                                              ├── pgadmin (web UI)
                                                              └── app (Spring Boot 4.1 / Java 21)
```

---

## 1. VPS prerequisites

On your VPS (Ubuntu 22.04+ recommended):

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker $USER
newgrp docker

# Install Docker Compose plugin (included with modern Docker)
docker compose version

# Install Git
sudo apt install -y git

# Create deploy directory
sudo mkdir -p /opt/inventory-management
sudo chown $USER:$USER /opt/inventory-management
```

Open firewall ports (adjust if using a reverse proxy):

```bash
sudo ufw allow 22/tcp
sudo ufw allow 8080/tcp   # API (or 443 if behind nginx)
sudo ufw allow 5050/tcp   # pgAdmin (restrict to your IP in production)
sudo ufw enable
```

---

## 2. First manual deploy on VPS

```bash
cd /opt/inventory-management

# Clone your repository
git clone https://github.com/YOUR_USER/InventoryManagement.git .

# Create environment file from template
cp .env.example .env
nano .env   # set POSTGRES_PASSWORD, PGADMIN_DEFAULT_PASSWORD, JWT_SECRET_KEY
```

Generate a secure JWT secret (Base64, 256+ bits):

```bash
openssl rand -base64 32
```

Paste the output into `JWT_SECRET_KEY` in `.env`.

Start all services:

```bash
docker compose --env-file .env up -d --build
```

Verify:

```bash
docker compose ps
curl http://localhost:8080/api/v1/actuator/health
```

Expected response: `{"status":"UP"}`

---

## 3. pgAdmin setup

1. Open `http://YOUR_VPS_IP:5050`
2. Log in with `PGADMIN_DEFAULT_EMAIL` / `PGADMIN_DEFAULT_PASSWORD` from `.env`
3. Add server:
   - **Name:** Inventory DB
   - **Host:** `postgres` (Docker service name, not localhost)
   - **Port:** `5432`
   - **Username:** value of `POSTGRES_USER` (default `postgres`)
   - **Password:** value of `POSTGRES_PASSWORD`

---

## 4. Jenkins setup

### Install Jenkins on VPS (or a separate CI server)

```bash
# Java 21 required for building this project
sudo apt install -y openjdk-21-jdk

# Jenkins (see https://www.jenkins.io/doc/book/installing/linux/)
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | \
  sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | \
  sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update && sudo apt install -y jenkins
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### Jenkins credentials

In **Manage Jenkins → Credentials**, add:

| ID | Type | Value |
|----|------|-------|
| `vps-ssh-credentials` | SSH Username with private key | VPS deploy user + private key |

### Jenkins global environment (optional)

**Manage Jenkins → System → Global properties → Environment variables:**

| Name | Example |
|------|---------|
| `VPS_HOST` | `203.0.113.10` |
| `VPS_USER` | `ubuntu` |

Or define them in the Jenkinsfile `environment` block.

### Create pipeline job

1. **New Item** → **Pipeline** → name: `inventory-management`
2. **Pipeline → Definition:** Pipeline script from SCM
3. **SCM:** Git → your repo URL + branch `main`
4. **Script Path:** `Jenkinsfile`
5. Save → **Build Now**

---

## 5. Auto-deploy on git push

### Option A — GitHub webhook (recommended)

1. Install **GitHub plugin** in Jenkins
2. In the job: enable **GitHub hook trigger for GITScm polling**
3. In GitHub repo → **Settings → Webhooks → Add webhook**
   - Payload URL: `http://YOUR_JENKINS:8080/github-webhook/`
   - Content type: `application/json`
   - Events: **Just the push event**

Every push to `main` triggers: checkout → test → bootJar → docker build → SSH deploy.

### Option B — SCM polling (already in Jenkinsfile)

The Jenkinsfile polls Git every 5 minutes (`pollSCM('H/5 * * * *')`). No webhook needed, but deploys are delayed.

---

## 6. What happens on each deploy

1. Jenkins checks out latest code
2. Runs `./gradlew test`
3. Builds JAR and Docker image tagged with build number
4. SSH into VPS at `/opt/inventory-management`
5. `git pull` latest `main`
6. `docker compose build app && docker compose up -d`
7. Flyway runs public schema migrations on app startup
8. Tenant schemas are created when new tenants register

---

## 7. Useful commands on VPS

```bash
cd /opt/inventory-management

# View logs
docker compose logs -f app
docker compose logs -f postgres

# Restart app only
docker compose up -d --build app

# Stop everything
docker compose down

# Stop and remove volumes (DESTROYS DATA)
docker compose down -v
```

### Production hardening (recommended)

Bind services to localhost only (use nginx as reverse proxy):

```bash
docker compose -f docker-compose.yml -f deployment/docker-compose-prod.yml up -d
```

---

## 8. Environment variables reference

| Variable | Required | Description |
|----------|----------|-------------|
| `POSTGRES_PASSWORD` | Yes | PostgreSQL password |
| `PGADMIN_DEFAULT_PASSWORD` | Yes | pgAdmin login password |
| `JWT_SECRET_KEY` | Yes | Base64 JWT signing key |
| `POSTGRES_DB` | No | Database name (default: `inventory_db`) |
| `POSTGRES_USER` | No | DB user (default: `postgres`) |
| `APP_PORT` | No | Host port for API (default: `8080`) |
| `PGADMIN_PORT` | No | Host port for pgAdmin (default: `5050`) |
| `JWT_EXPIRATION` | No | Token TTL in ms (default: `86400000`) |
| `IMAGE_TAG` | No | Docker image tag (Jenkins sets this) |

---

## 9. API endpoints after deploy

| URL | Description |
|-----|-------------|
| `http://VPS:8080/api/v1/actuator/health` | Health check |
| `http://VPS:8080/api/v1/auth/login` | Login |
| `http://VPS:8080/api/v1/tenants` | Register tenant |
| `http://VPS:8080/swagger-ui.html` | API docs |
| `http://VPS:5050` | pgAdmin |

---

## 10. Troubleshooting

| Problem | Fix |
|---------|-----|
| App exits on startup | Check `docker compose logs app` — usually missing `.env` values |
| DB connection refused | Wait for postgres healthcheck; verify `SPRING_DATASOURCE_URL` uses host `postgres` |
| Jenkins SSH fails | Verify `vps-ssh-credentials` and passwordless SSH from Jenkins user |
| Old code still running | Run `docker compose up -d --build app` manually on VPS |
| Flyway migration error | Check logs; never use `clean-on-validation-error` in prod (disabled in `application-prod.yml`) |
