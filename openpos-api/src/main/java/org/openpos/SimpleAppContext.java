package org.openpos;

import java.util.HashMap;
import java.util.Map;

import com.openbravo.pos.forms.AppProperties;

public class SimpleAppContext implements AppContext {

	private static final String DEFAULT_BEAN = "--DEFAULT_BEAN--";
	private Map<String, Object> classToObjectMap = new HashMap<String, Object>();
	private AppProperties appProperties;

	public SimpleAppContext(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	public SimpleAppContext(Object... objects) {
		for (Object object : objects) {
			addBean(object);
		}
	}

	public void addBean(Object bean) {
		addBean(bean, DEFAULT_BEAN);
	}

	public void addBean(Object bean, String beanName) {

		Class<?> beanClass = bean.getClass();
		do {
			classToObjectMap.put(beanClass.getName() + "/" + beanName, bean);
			beanClass = beanClass.getSuperclass();
		} while (beanClass != null);

		addInterfaces(bean.getClass().getInterfaces(), bean, beanName);
	}

	private void addInterfaces(Class<?>[] interfaces, Object bean, String beanName) {
		for (Class<?> interfaceClass : interfaces) {
			classToObjectMap.put(interfaceClass.getName() + "/" + beanName, bean);
			addInterfaces(interfaceClass.getInterfaces(), bean, beanName);
		}
	}

	@Override
	public <T> T getBean(Class<T> beanClass) {
		return getBean(beanClass, DEFAULT_BEAN);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> beanClass, String beanName) {

		T bean = (T)classToObjectMap.get(beanClass.getName() + "/" + beanName);
		if (bean == null)
			throw new RuntimeException("Class not in context: " + beanClass);

		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String propertyName, Class<T> knownAlternative) {
		try {
			if (appProperties != null && appProperties.getProperty(propertyName) != null) {
				String className = appProperties.getProperty(propertyName);
				return (T)Class.forName(className).newInstance();
			}
			else
				return knownAlternative.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
