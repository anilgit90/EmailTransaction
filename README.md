# EmailTransaction

Used to send Transaction Emails using Mailgun API.
To get started on using webservice fill in the proper values in the config.yml file.

How to start the EmailTransaction application
---

1. Run `mvn clean package` to build your application
2. Start application with `java -jar target/EmailTransaction-1.0.jar server config.yml`
3. To check that your application is running enter url `http://localhost:<application-port-in-config>`

Health Check
---

To see your applications health enter url `http://localhost:<admin-port-in-config>/healthcheck`
By default deadlock healthcheck has already been implemented.
Added Mailgun status API configured through UptimeRobot.

Deploy
---
Windows Server:

1. Please make sure you download the server JRE from Oracle Website.
2. Create A Batch file to call the DropWizard Application using JVM.
3. Create A Task Scheduler to Call the Batch file when the system starts.
   Tested On Windows Server 2012 R2.
