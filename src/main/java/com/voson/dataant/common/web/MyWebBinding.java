/**
 * This file created at 2013-6-18.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.web;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * <code>{@link MyWebBinding}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class MyWebBinding implements WebBindingInitializer {

	public void initBinder(WebDataBinder binder, WebRequest request) {
		//1. 使用spring自带的CustomDateEditor
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		//2. 自定义的PropertyEditorSupport
		binder.registerCustomEditor(Date.class, new DateConvertEditor());

	}
}
