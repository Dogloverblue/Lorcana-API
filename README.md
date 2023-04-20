![Photo by Tengyart on Unsplash](https://user-images.githubusercontent.com/2342458/202706389-501decaf-810e-4876-a749-7b9e593e67b1.png)

# Kinsta - Hello World - Java
An example of how to set your Java application up to enable deployment on Kinsta App Hosting services.

---
Kinsta is a developer-centric cloud host / PaaS. We’re striving to make it easier for you to share your web projects with your users. Focus on coding and building, and we’ll take care of deployment and provide fast, scalable hosting. + 24/7 expert-only support.

Get started for free, the first $20 is on us!

[Application Hosting](https://kinsta.com/application-hosting)

[Database Hosting](https://kinsta.com/database-hosting)

## Dependency Management

During the deployment process Kinsta will automatically install dependencies defined in your pom.xml file.

## Web Server Setup

### Port
Kinsta automatically sets the `PORT` environment variable. You should **not** define it yourself and you should **not** hard-code it into the application. Use 
`System.getenv("PORT")` in your code when referring to the server port. 

### Start Command
When deploying an application Kinsta will automatically create processes based on the Procfile in the root of the repository. Make sure to use this command to run your 
server.

```
web: java -jar target/kinsta-java-hello-world-1.0-SNAPSHOT.jar
```

Make sure that your main class is defined in the pom.xml file.
