# Deployment Guide for Feeder API

This document provides the necessary steps to set up Google Cloud Platform (GCP) resources and deploy the `feeder-api` service to a Google Kubernetes Engine (GKE) cluster.

## 1. GCP Setup Requirements

### Project & CLI (ONE TIME SETUP)
- **Google Cloud Project**: Create or select a project (e.g., `feeder-project`).
- **Google Cloud SDK**: Install `gcloud` CLI and authenticate.
- **Enable APIs**:
  ```bash
  gcloud services enable container.googleapis.com \
                         artifactregistry.googleapis.com \
                         cloudbuild.googleapis.com
  ```

### Artifact Registry (ONE TIME SETUP)
Create a repository for storing Docker images:
```bash
gcloud artifacts repositories create feeder-repo \
    --repository-format=docker \
    --location=asia-south1 \
    --description="Docker repository for Feeder project"
```

### GKE Cluster (ONE TIME SETUP)
Create a GKE cluster:
```bash
gcloud container clusters create feeder-cluster \
    --region=asia-south1 \
    --num-nodes=2
```
Get credentials for the cluster:
```bash
gcloud container clusters get-credentials feeder-cluster --region asia-south1
```

## 2. Docker Image Preparation

### Build and Push Image
Replace `<PROJECT_ID>` with your actual GCP project ID.

1. **Authenticate Docker to GCP**:
   ```bash
   gcloud auth configure-docker asia-south1-docker.pkg.dev
   ```

2. **Build Image** (Using Spring Boot Buildpacks):
   The Spring Boot Gradle plugin provides a `bootBuildImage` task that creates an optimized image using Cloud Native Buildpacks. This task does not require a local Dockerfile.

   Run this from the project root:
   ```bash
   ./gradlew :api:bootBuildImage \
     --imageName=asia-south1-docker.pkg.dev/<PROJECT_ID>/feeder-application/backend-api:local-test \
     --imagePlatform=linux/amd64
   ```

   **Parameters Reference:**
   - `--imageName`: The full name and tag for the image (e.g., `.../api:latest` or `.../api:v1.0`).
   - `--imagePlatform`: The target OS/architecture (e.g., `linux/amd64` or `linux/arm64`).
   - `--tags`: (Optional) Comma-separated list of additional tags.
   - `--publishImage`: (Optional) If provided, the image will be automatically pushed to the registry after building.

3. **Push Image**:
   If you did not use the `--publishImage` flag in the previous step, push the image manually:
   ```bash
   docker push asia-south1-docker.pkg.dev/<PROJECT_ID>/feeder-application/backend-api:local-test
   ```

## 3. Deployment Steps

### Configure Kubernetes Secrets
Before deploying, update `api/k8s/base/secret.yaml` with your actual database credentials (base64 encoded).

Alternatively, use `kubectl` to create the secret directly:
```bash
kubectl create secret generic feeder-api-secrets \
  --from-literal=DATABASE_USERNAME=your-username \
  --from-literal=DATABASE_PASSWORD=your-password
```

### Deploy using Kustomize
1. **Update Image Name in Kustomize (Optional)**:
   You can use Kustomize to set the actual image name:
   ```bash
   cd api/k8s/base
   kustomize edit set image gcr.io/feeder-project/api:latest=asia-south1-docker.pkg.dev/<PROJECT_ID>/feeder-application/backend-api:local-test
   ```

2. **Apply Manifests** (Run from root directory):
   ```bash
   kubectl apply -k api/k8s/base
   ```

3. **Verify Deployment**:
   ```bash
   kubectl get pods -l app=feeder-api
   kubectl get services feeder-api
   ```

4. **Access the Application**:
   Wait for the `EXTERNAL-IP` to be assigned to the `feeder-api` service, then access it via that IP.

## 4. Environment Variables Reference

| Variable | Source | Description |
|----------|--------|-------------|
| `FEED_SYNC_ALLOWED_USERS` | ConfigMap | Comma-separated list of users allowed to sync feeds |
| `REPOSITORY_TYPE` | ConfigMap | `jdbc` or `memory` |
| `DATABASE_URL` | ConfigMap | JDBC URL for PostgreSQL |
| `DATABASE_USERNAME` | Secret | Database username |
| `DATABASE_PASSWORD` | Secret | Database password |
