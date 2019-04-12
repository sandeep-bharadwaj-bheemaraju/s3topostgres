package com.sandeep.learning.s3topostgres.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sandeep.learning.s3topostgres.dto.Message;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class S3ToPostgresService {

	private static final Logger LOG = LoggerFactory.getLogger(S3ToPostgresService.class);

	@Autowired
	AmazonS3Client amazonS3Client;

	@Value("${spring.datasource.url}")
	private String dbConnectionURL;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	public void processFile(Message notification){

		long rowsInserted = 0;

		S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(notification.getBucketName(), notification.getFileName()));

		try(Connection connection = DriverManager.getConnection(dbConnectionURL, dbUsername, dbPassword)) {

			InputStream inputStream = s3Object.getObjectContent();

			rowsInserted = new CopyManager((BaseConnection) connection).copyIn(
					"COPY employees (employee_id, first_name, last_name, title, email)"
					+"FROM STDIN (FORMAT CSV)", inputStream
			);

			LOG.info("Inserted records count {}", rowsInserted);

		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}

	}

}
