# EmailTransaction

[![Build Status](https://travis-ci.org/anilvrs90/EmailTransaction.svg?branch=master)](https://travis-ci.org/anilvrs90/EmailTransaction)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ae86e1c55e8b4104a7df9f9ebb31348b)](https://www.codacy.com/app/anilvrs_90/EmailTransaction?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=anilvrs90/EmailTransaction&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://www.versioneye.com/user/projects/588a2958be496c0047e1ade5/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/588a2958be496c0047e1ade5)
<a href="https://scan.coverity.com/projects/anilvrs90-emailtransaction">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/11596/badge.svg"/>
</a>

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
