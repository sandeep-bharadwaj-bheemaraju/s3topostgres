package com.sandeep.learning.s3tolambda.handler;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.sandeep.learning.s3tolambda.dto.Message;

public class NotificationHandler implements RequestHandler<S3Event, String> {

	private AmazonSQS sqsClient;

	private String queueUrl = "https://sqs.ap-southeast-2.amazonaws.com/924564549370/S3-Notifier-Lambda-Queue";

	public NotificationHandler() {

		sqsClient = AmazonSQSClientBuilder.standard()
				.withRegion(Regions.AP_SOUTHEAST_2)
				.build();

	}


	@Override
	public String handleRequest(S3Event event, Context context) {

		// Get the object from the event and fetch bucketName and fileName
		String bucketName   = event.getRecords().get(0).getS3().getBucket().getName();
		String fileName     = event.getRecords().get(0).getS3().getObject().getKey();

		context.getLogger().log("Bucket name is : " +bucketName);
		context.getLogger().log("File name is   : " +fileName);

		Message message = new Message();
		message.setBucketName(bucketName);
		message.setFileName(fileName);

		String notification = message.toString();

		sqsClient.sendMessage(queueUrl, notification);

		context.getLogger().log("Message sent to SQS queue is   : " +notification);

		return notification;
	}

}
