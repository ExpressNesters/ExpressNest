package edu.sjsu.expressnest.postservice.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ValidationMessageService {

	@Autowired
	private MessageSource messageSource;
	
	public String getValidationMessage(String messageKey, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(messageKey, params, locale);
	}
	
}
