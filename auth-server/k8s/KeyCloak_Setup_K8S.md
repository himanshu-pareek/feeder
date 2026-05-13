# Keycloak Setup Guide on GKE

This guide provides a concise, step-by-step walk-through to deploy Keycloak (v26.6.1) on Google Kubernetes Engine (GKE) behind a Gateway API over HTTP, serving traffic at the `/auth` path.

This setup utilizes standard Kubernetes ConfigMaps and Secrets for environment variables, a Gateway `HTTPRoute` with URL rewriting, and a `HealthCheckPolicy` to ensure the Google Cloud Load Balancer can correctly probe Keycloak's management port.

## Step 1: Define Database & Bootstrap Secrets

First, create a Kubernetes Secret named `keycloak-secret` containing your database credentials and initial Keycloak admin credentials.

The following keys **must** be present in the `keycloak-secret` secret:

- `KC_BOOTSTRAP_ADMIN_PASSWORD`
- `KC_BOOTSTRAP_ADMIN_USERNAME`
- `KC_DB`
- `KC_DB_PASSWORD`
- `KC_DB_URL_DATABASE`
- `KC_DB_URL_HOST`
- `KC_DB_USERNAME`

*Example command to create the secret:*

```bash
kubectl create secret generic keycloak-secret \\
  --from-literal=KC_BOOTSTRAP_ADMIN_USERNAME=admin \\
  --from-literal=KC_BOOTSTRAP_ADMIN_PASSWORD=your-secure-password \\
  --from-literal=KC_DB=postgres \\
  --from-literal=KC_DB_PASSWORD=db-password \\
  --from-literal=KC_DB_URL_DATABASE=keycloak \\
  --from-literal=KC_DB_URL_HOST=postgres-service \\
  --from-literal=KC_DB_USERNAME=keycloak_user
```

## Step 2: Apply the Keycloak Configurations

Save the following YAML configuration to a file (e.g., `keycloak-setup.yaml`) and apply it using `kubectl apply -f keycloak-setup.yaml`.

This complete configuration includes:

1. **ConfigMap**: Stores non-sensitive Keycloak environment variables.
2. **HealthCheckPolicy**: Instructs the GKE Gateway controller to use Keycloak's management port (`9000`) for Google Cloud Load Balancer health checks.
3. **HTTPRoute**: Routes traffic hitting the `/auth` path on the Gateway to the Keycloak service, and uses a `URLRewrite` filter to strip the prefix so Keycloak processes it natively.
4. **Service & Deployment**: Provisions the Keycloak Pods and exposes them internally.

```yaml

-- NOTE: Look at the files available for each type of resource

apiVersion: v1
kind: ConfigMap
metadata:
  labels:
    app: auth-server
  name: keycloak-configmap
data:
  KC_CACHE: ispn
  KC_HEALTH_ENABLED: "true"
  KC_HOSTNAME: <Gateway-URL>/auth
  KC_HOSTNAME_STRICT: "false"
  KC_HTTP_ENABLED: "true"
  KC_PROXY_HEADERS: xforwarded

---
apiVersion: networking.gke.io/v1
kind: HealthCheckPolicy
metadata:
  name: keycloak-health-check
  namespace: default
spec:
  default:
    config:
      type: HTTP
      httpHealthCheck:
        port: 9000
        requestPath: /health/ready
  targetRef:
    group: ""
    kind: Service
    name: service-auth-keycloak

---
apiVersion: gateway.networking.k8s.io/v1beta1
kind: HTTPRoute
metadata:
  name: http-route-auth-keycloak
spec:
  parentRefs:
    - name: gateway-shared
  rules:
    - matches:
      - path:
          type: PathPrefix
          value: /auth
      filters:
      - type: URLRewrite
        urlRewrite:
          path:
            type: ReplacePrefixMatch
            replacePrefixMatch: ""
      backendRefs:
      - name: service-auth-keycloak
        port: 8080

---
apiVersion: v1
kind: Service
metadata:
  name: service-auth-keycloak
  labels:
    app: keycloak
spec:
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      name: http
  selector:
    app: keycloak
  type: ClusterIP

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: keycloak
  labels:
    app: keycloak
spec:
  replicas: 1
  selector:
    matchLabels:
      app: keycloak
  template:
    metadata:
      labels:
        app: keycloak
    spec:
      containers:
        - name: keycloak
          image: quay.io/keycloak/keycloak:26.6.1
          args: ["start"]
          envFrom:
            - secretRef:
                name: keycloak-secret
            - configMapRef:
                name: keycloak-configmap
          env:
            - name: POD_IP
              valueFrom:
                fieldRef:
                  fieldPath: status.podIP
            # Instruct JGroups which DNS hostname to use to discover other Keycloak nodes
            # Needs to be unique for each Keycloak cluster
            - name: KC_CACHE_EMBEDDED_NETWORK_BIND_ADDRESS
              value: '$(POD_IP)'
          ports:
            - name: http
              containerPort: 8080
            - name: jgroups
              containerPort: 7800
            - name: jgroups-fd
              containerPort: 57800
            - name: health
              containerPort: 9000
          startupProbe:
            httpGet:
              path: /health/started
              port: 9000
            periodSeconds: 60
            failureThreshold: 600            
          readinessProbe:
            httpGet:
              path: /health/ready
              port: 9000
            periodSeconds: 60
            failureThreshold: 3            
          livenessProbe:
            httpGet:
              path: /health/live
              port: 9000
            periodSeconds: 60
            failureThreshold: 3            
          resources:
            limits:
              cpu: 2000m
              memory: 2000Mi
            requests:
              cpu: 500m
              memory: 1700Mi
```

## Step 3: Disable HTTPS Requirement for Admin Console

Because Keycloak defaults to requiring HTTPS for external connections to the `master` realm, and this setup explicitly utilizes HTTP for now, you must disable the `sslRequired` check.

1. Exec into the running Keycloak pod:
    
    ```bash
    # Find the pod name
    kubectl get pods -l app=keycloak
    
    # Exec into the pod
    kubectl exec -it <keycloak-pod-name> -- /bin/bash
    ```
    
2. Authenticate the Keycloak CLI tool against the local server
    
    ```bash
    /opt/keycloak/bin/kcadm.sh config credentials \
    	--server http://localhost:8080 \
    	--realm master
    	--user $KC_BOOTSTRAP_ADMIN_USERNAME \
    	--password $KC_BOOTSTRAP_ADMIN_PASSWORD
    ```
    
3. Disable the HTTPS requirement:
    
    ```bash
    /opt/keycloak/bin/acadm.sh update realms/master -s sslRequired=NONE
    ```
    
4. Exit the pod:
    
    ```bash
    exit
    ```
    

## Final Verification

Navigate to `<GATEWAY-URL>auth` in your browser. You should now be successfully routed to the Keycloak login screen, and you can log in using your bootstrap admin credentials.
