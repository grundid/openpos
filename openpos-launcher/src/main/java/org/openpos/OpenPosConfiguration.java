package org.openpos;

import java.io.File;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;

@Configuration
public class OpenPosConfiguration {

	private Logger log = LoggerFactory.getLogger(getClass());
	private AppConfig config;

	public OpenPosConfiguration() {
		File propertiesFile = null;
		String locationProperty = System.getProperty("openpos.properties");
		if (locationProperty == null) {
			propertiesFile = new File(new File(System.getProperty("user.home")), AppLocal.APP_ID + ".properties");
		}
		else
			propertiesFile = new File(locationProperty);
		log.info("Loading properties from [" + propertiesFile.getAbsolutePath() + "]");
		config = new AppConfig(propertiesFile);
		config.load();
	}

	@Bean
	public AppConfig getAppConfig() {
		return config;
	}

	@Bean
	public MailSender mailSender(AppConfig appConfig) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		if (appConfig.getProperty("mail.sendreports").equalsIgnoreCase("true")) {
			mailSender.setHost(appConfig.getProperty("mail.smtp"));
			mailSender.setUsername(appConfig.getProperty("mail.user"));
			mailSender.setPassword(appConfig.getProperty("mail.password"));
		}
		return mailSender;
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(config.getProperty("db.driver"));
		basicDataSource.setUrl(config.getProperty("db.URL"));
		basicDataSource.setUsername(config.getProperty("db.user"));
		String password = LegacyUtils.decryptPassword(config.getProperty("db.user"), config.getProperty("db.password"));
		basicDataSource.setPassword(password);
		return basicDataSource;
	}
}
