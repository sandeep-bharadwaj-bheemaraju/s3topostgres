
# S3toPostgres - A Data migration solution from onprem to AWS RDS 

*S3toPostgres* provides an easy-and-fastway to migrate your data to AWS Cloud applications.

Currently, the focus is primarily on migrating data using CSV or Delimited file.

# Overview

*S3toPostgres* Migrate data from legacy system to AWS

![S3toPostgres](https://github.com/sandeep-bharadwaj-bheemaraju/s3topostgres/dashboard/web/img/s3topostgres.png)

# Solution Design

To migrate the data from legacy system to AWS database, *S3toPostgres* follows below steps.

* **Legacy system shall export the data to S3 bucket in agreed CSV format.**
* **Upon successful upload, S3 shall trigger a notification message to AWS Lambda (s3tolambda module).**
* **AWS Lambda (s3tolambda) shall read the queue configuration from Dynamo DB and push the message to SQS queue.**
* **s3topostgres microservice will listen to SQS queue and consumes the message.**
* **Consumed message, shall give info about the file uploaded to the bucket.**
* **This info enables the microservice to stream the data from csv file to postgres database table using Postgres COPY Manager.**  

*S3toPostgres* allows you to configure the Lambda, provide source data file in the custome delimiter format.

## Why *S3toPostgres*?

*S3toPostgres* builds on existing Postgres COPY Manager implementation.
It consumes less memory and performs with high throughput allowing you to migrate millions of records in seconds.

* **Performance:** *S3toPostgres* allows to migrate millions of records in seconds.

* **Atomic**: Migration of data is atomic.

* **Decoupled Architecture**

* **Configurable**

* **Production Ready Solution**


## Requirements

* `java`/`javac` (Java 8 runtime environment and compiler)
* `gradle` (The build system for Java)

## Installation

To run the s3-to-postgres application, below resources are to be created in AWS account.

* **1)	Create S3 bucket**
* **2)	Create SQS queue**
* **3)	Create EC2 instance**
* **4)	Create RDS Postgres database**
* **5)	Lambda configuration in DynamoDB**
* **6)	Deploying Lambda**

Step by Step installation procedure is clearly explained in the S3ToPostgresHelp.pdf file in the repository.