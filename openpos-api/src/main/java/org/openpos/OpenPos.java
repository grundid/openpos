package org.openpos;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OpenPos {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void initApplicationContext(String... basePackages) {
		applicationContext = new AnnotationConfigApplicationContext(basePackages);
	}
}
