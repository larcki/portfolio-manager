# portfolio-manager

[![Build Status](https://travis-ci.org/larcki/portfolio-manager.svg?branch=master)](https://travis-ci.org/larcki/portfolio-manager) 

Single page application for visualising and tracking financial portfolio performance.

See a dummy portfolio in action:
http://portfolio-manager-preview.eba-wpv8qkmm.eu-west-1.elasticbeanstalk.com

### Structure
```
portfolio-manager
├─┬ frontend      → frontend module with Vue.js code
│ ├── src
│ └── build.gradle
├── src           → backend Java code
└── build.gradle  → main build.gradle
```

### Commands
#### Run frontend
In `frontend` folder, run `yarn serve`

#### Run backend
In root folder run `./gradlew runBoot`. You need to provide datasource credentials referenced in [application.properties](src/main/resources/application.properties)

#### Create distributable JAR
Run `./gradlew clean test build -Pfrontend`. Creates a standalone JAR including all the static assets inside `/build/distributions/portfolio-manager-1.0-SNAPSHOT.zip`
