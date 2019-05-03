package com.sandeep.learning.s3tolambda.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.sandeep.learning.s3tolambda.dto.Message;

public class NotificationHandler implements RequestHandler<S3Event, String> {

	private AmazonSQS sqsClient;

	private AmazonDynamoDB amazonDynamoDB;

	private DynamoDB dynamoDB;


	public NotificationHandler() {

		sqsClient = AmazonSQSClientBuilder.standard().build();

		amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();

		dynamoDB = new DynamoDB(amazonDynamoDB);

	}


	@Override
	public String handleRequest(S3Event event, Context context) {

		Table table = dynamoDB.getTable("configuration");
		Item item = table.getItem("KEY", "notification.queue");
		String queueName = item.getString("VALUE");

		context.getLogger().log("Sending message to queue : " +queueName);

		// Get the object from the event and fetch bucketName and fileName
		String bucketName   = event.getRecords().get(0).getS3().getBucket().getName();
		String fileName     = event.getRecords().get(0).getS3().getObject().getKey();

		context.getLogger().log("Bucket name is : " +bucketName);
		context.getLogger().log("File name is   : " +fileName);

		Message message = new Message();
		message.setBucketName(bucketName);
		message.setFileName(fileName);

		String notification = message.toString();

		final GetQueueUrlResult queueUrl = sqsClient.getQueueUrl(queueName);

		sqsClient.sendMessage(queueUrl.getQueueUrl(), notification);

		context.getLogger().log("Message sent to SQS queue is   : " +notification);

		return notification;
	}

}
