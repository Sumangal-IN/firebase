package com.tcs.novia.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TopicMessageController {

	@Autowired
	private FirebaseMessageService firebaseMessageService;

	@RequestMapping(value = "/postTopicMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String postTopicMessage(@RequestParam(value = "title", required = true) final String titleText,
			@RequestParam(value = "body", required = true) final String bodyText) throws CustomException {
		return firebaseMessageService.sendMessageToTopic(titleText, bodyText);
	}

}