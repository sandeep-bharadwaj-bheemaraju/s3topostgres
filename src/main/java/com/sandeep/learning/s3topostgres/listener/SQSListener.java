package com.sandeep.learning.s3topostgres.listener;

import com.sandeep.learning.s3topostgres.dto.Message;
import com.sandeep.learning.s3topostgres.service.S3ToPostgresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SQSListener {

	private static final Logger LOG = LoggerFactory.getLogger(SQSListener.class);

	@Autowired
	S3ToPostgresService s3ToPostgresService;

	@SqsListener(value = "${s3topostgres.sqs.notification.queue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	private void onMessage(String message) {

		LOG.info("Received notification is {}", message);

		s3ToPostgresService.processFile(Message.toObject(message));

	}

}
