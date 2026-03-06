# LCOMHS + Takt Time + Lead Time + Grafana + Jenkins
## Overview
This project computes:
1. LCOMHS (Henderson–Sellers cohesion) per Java class
2. Lead Time from Taiga
3. Takt Time from Taiga
4. The metrics are exported to Prometheus and visualized using Grafana.
5. Jenkins is used for continuous integration by running unit tests and generating code coverage reports.

## Execution of the Project
### Metrics Exceution with Docker
### Stage 1: Prerequisites
1. Install Docker Desktop (or Docker Engine).
2. Set Taiga credentials in the docker-compose.yml file.
    Update the following fields with your Taiga username and password:
    TAIGA_USERNAME: username
    TAIGA_PASSWORD: password

### Stage 2: Run using Docker Compose
Run the following command:
docker compose up --build

This will start:
The metrics service
Prometheus
Grafana

## ----------------------------

### Stage 3: Grafana Dashboard for Metrics
(Metrics: LCOMHS, Takt Time, Lead Time)
After running Docker compose,
### Prometheus
Promethemus will run at: http://localhost:9090/
To validate that metrics are available, run the following queries separately:
1. lcomhs 
2. takt_time_days_per_story
3. lead_time
You should see results for each query.

### Grafana
Grafana will run at: http://localhost:3000/
login credientials: 
Username: admin
Password: admin
Step 1: Configure Data Source 
1. Go to Connections -> Data Sources
2. Add a new data source
3. Select Prometheus
4. Set:
    Name: prometheus
    Server URL: http://host.docker.internal:9090

Step 2: Save and Test
Scroll down and click:
    "Save & Test"
You should see a success message.

Step 3: Verify Data Source
1. Go to Explore
2. Select prometheus as the data source
3. Run the following queries:
    lcomhs/ takt_time_days_per_story/ lead_time
You should see results returned.

Step 4: Import Dashboard
1. Go to Dashboards -> New -> Import
2. Import the JSON file located at the root(Github repo): LCOMHS_Metric_Dashboard.json
The dashboard will display three panels for LCOMHS, Takt Time, Lead Time.
## ----------------------------

### Stage 4: Jenkins – View Unit Test and Coverage Reports
Pipeline Name: unit-tests-group4

Configurations(Already configured-no changes needed): 
Poll SCM: * * * * * (polls every minute)
SCM: Git
Git Repo: https://github.com/adecost4/Ser516Group4
Branch Specifier: */main 
(For development, change the branch name accordingly. In the future, this can be configured for any branch.).
Script Path: Jenkinsfile

Coverage Threshold
Current Threshold: 10% (To be improved in future)

Viewing Coverage Reports in Jenkins:
1. Go to the Jenkins job.
2. Open a build (click on build No.).
3. Navigate to: Build Artifacts.
4. Download all artifacts as a ZIP file.
5. Open target/site/jacoco/index.html
This file contains the detailed coverage report.


#### Optional - If unit testing locally
To run tests and validate coverage locally:
    mvn clean verify
The console will display: Unit test results, Coverage validation, Build success/failure.

The coverage report will be generated at:
    target/site/jacoco/index.html
## ----------------------------