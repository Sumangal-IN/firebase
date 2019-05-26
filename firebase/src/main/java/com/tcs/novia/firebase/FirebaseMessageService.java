package com.tcs.novia.firebase;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseMessageService {

	@Value("${app.firebase.db.name}")
	private String dbName;

	FirebaseMessageService() throws CustomException {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(
					new ClassPathResource("novia2019-e3063-firebase-adminsdk-hy20m-f4f7cb974d.json").getInputStream()))
					.setDatabaseUrl(dbName).build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException("Problem during connection establishment with FireBase");
		}
	}

	public String sendMessageToTopic(String titleText, String bodyText) throws CustomException {

		try {
			String topicIOS = "NoviaEventIOS";
			String topicAndroid = "NoviaEvent";

			// ios (data + notification)
			Message messageIOS = Message.builder().putData("title", titleText).putData("body", bodyText)
					.setNotification(new Notification(titleText, bodyText))
					.setApnsConfig(ApnsConfig.builder().setAps(Aps.builder().setSound("default").build()).build())

					.setTopic(topicIOS).build();

			// android (data only)
			Message messageAndroid = Message.builder().putData("title", titleText).putData("body", bodyText)
					.setTopic(topicAndroid).build();

			String returnIOS = FirebaseMessaging.getInstance().send(messageIOS);
			String returnAndroid = FirebaseMessaging.getInstance().send(messageAndroid);

			return "{\"android\":\"" + returnAndroid + "\",\"ios\":\"" + returnIOS + "\"}";
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			throw new CustomException("Problem during connection establishment with FireBase");
		}
	}

}
