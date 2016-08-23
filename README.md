# Overview

CAPI stands for Cartographer API. It is intended to be an extension of the Halo API that allows Users to analyze data on custom generated content. The normal Halo API puts the Player and Games at the center of the resources available in the API. CAPI will extend this by allowing developers to find data based on Maps, Gametypes, and Forgers. In order to do this, every custom game ever played is cached and as a custom game is cached it is run through the system in order to analyze it for details like what Map was this custom game played on.

This extension of the Halo API will add the ability to access User Generated Content statistics for whatever reason a developer would need it. For example, community sites that allow Users to post their Maps, can tap into CAPI in order to get testing statistics like the most recent Game played on a Map, how popular the Map is, whether or not a Reviewer has played on a Map, etc. Or a Map tracking mobile app could be created that allows a Forger to keep track of every game being played on their Maps.

# Current System Architecture

This image represents the current systems in place that make CAPI work. Anything in RED is currently not implemented.

![CAPI Architecture](http://i.imgur.com/FIFwgYv.jpg "CAPI Architecture")

# Architecture Goals

We want to provide data quickly, reliably, and securely. Decisions on the architecture are made to improve access times, allow rapid access to data, and scale to high loads. The architecture also promotes extremely low coupling and extendability. Nanoservices ensure small completable units in the system, allowing features to be added in a matter of hours to a few days. Serverless architecture is pushed to reduce the amount of overhead spent on managing the systems that code runs on and allows developers to focus on what matters, the business logic.

The SOLID principles are paid attention to throughout the system emphasizing Single Responsibility and Open/Closed Principles especially. This ensures that when components of the system are in place there is very little reason for them to be changed. This promotes growth in the system and rarely taking any steps backwards and reworking old code, which also prevents bugs from being introduced into the system.

To reduce compute time, end user questions are predicted, pre-computed, and stored in materialized views. API Endpoints provide access to the materialized views for the end user. Materialized views and caching the raw data are also key to reduce any hurdle that may slow the time to access the data. A RESTful API is provided to also ensure that data access is intuitive for end users.

# Technology Overview

The CAPI repository is an Eclipse Workspace that holds every AWS Lambda Function project as well as a central CAPI Domain project that hosts the shared code for the functions. Eclipse is used because of its AWS Toolkit plugin which eases the management of AWS services straight from Eclipse.

There is currently no centralized testing environment for CAPI development and this README will help guide developers that want to contribute to building a local environment that will replicate how CAPI is setup in production. In the future there will be devops in place that will ease this environment setup by tapping into the AWS API and creating all of the necessary services required.

The following AWS Services are used by CAPI:

* __API Gateway__

   _This is used to provide the public interface for CAPI. API Gateway provides all of the HTTP endpoints that are hit that trigger AWS Lambda Functions. API Gateway also provides services like caching, throttling, API keys, client SDKs, etc._

* __Lambda__

   _Lambda functions are stateless code that are run in response to events. This is the bulk of the system work that needs to be written. This handles moving data to the DynamoDB tables, or publishing messsages to an SNS topic, or fetching raw data from the Halo API, etc. Any actual work that needs to get done is handled through AWS Lambda functions._

* __DynamoDB__

   _A NoSQL database solution provided by AWS. The main reason to choose DynamoDB over other NoSQL solutions like Mongo or Cassandra is that it ties into the AWS ecosystem more seamlessly. For example DynamoDB streams are tapped into to provide essentially database triggers by sending change log information to a Lambda function. We use this to send out any new, updated, or deleted events into CAPI for other services to tap into._

* __SNS__ (Simple Notification Service)

   _A message bus and command bus system from AWS. SNS provides an AWS service that allows us to setup a pub/sub system for our services. Using a pub/sub system allows us to reduce the coupling between services. From an article I read on Microservices from Droplet, the suggestion is to publish EVERYTHING. This is how we do that in CAPI, and everything that is interested will subscribe to the topic that we are publishing to._

* __SQS__ (Simple Queue Service)

   _A queue solution that is a part of the AWS ecosystem. While SQS does not tap into Lambda like SNS does, it still provides some really nice features for CAPI. For example, a message that is not processed within the visibility timeout gets re-added back into the queue. This is important for our Lambda architecture as a Lambda function could take too long to process and may hit a timeout and we want to ensure that if the message from the queue is not processed it is made to be available again for another instance of the Lambda function to handle._

* __Cloudwatch__

   _A monitoring and logging system provided by AWS. Not only do most AWS services already utilize Cloudwatch, but there are features like setting up alarms when certain events happen. When alarms are triggered, emails and stuff can be sent, but it can also trigger Lambda functions. This means that we can setup an autoscaling and autohealing system for CAPI with Cloudwatch as well as doing all of our performance and error logging and monitoring._

# Developer Setup

***This will come in the future when time is found. For now feel free to explore the code and feel free to email me if you have any particular questions.***