package ru.project.wtf.system;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;

/**
 * Абстрактный класс для интеграции Spring Boot и JavaFX.
 * 
 * @author Alimurad A. Ramazanov
 * @since 22.10.2017
 * @version 1.0.0
 *
 */
public abstract class JavaFxApplication extends Application {

	private static String[] args;

	protected ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {
		context = SpringApplication.run(getClass(), args);
		context.getAutowireCapableBeanFactory().autowireBean(this);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		context.close();
	}

	protected static void startApplication(Class<? extends JavaFxApplication> clazz, String[] args) {
		JavaFxApplication.args = args;
		Application.launch(clazz, args);
	}
}
