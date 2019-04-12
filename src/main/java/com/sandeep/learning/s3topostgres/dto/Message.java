package com.sandeep.learning.s3topostgres.dto;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	private String bucketName;
	private String fileName;

	public static Message toObject(String payload) {

		Message message = null;

		try{
			ObjectMapper mapper = new ObjectMapper();
			message = (Message) mapper.readValue(payload.getBytes(), Class.forName(Message.class.getName()));
		}
		catch (ClassNotFoundException | JsonParseException | JsonMappingException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return message;
	}
}
