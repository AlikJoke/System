package ru.project.wtf.system.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import ru.project.wtf.system.controllers.AuthController;
import ru.project.wtf.system.controllers.MainController;
import ru.project.wtf.system.loader.ViewLoader;
import ru.project.wtf.system.model.SimpleView;
import ru.project.wtf.system.properties.Properties;

@Configuration
public class FxConfiguration {

	@Autowired
	private ViewLoader loader;

	@Autowired
	private Properties props;

	@Bean("authView")
	@Primary
	public SimpleView getAuthView() throws IOException {
		return loader.load(props.getProperty("auth.view.location"));
	}

	@Bean
	public AuthController getAuthController() throws IOException {
		return (AuthController) getAuthView().getController();
	}

	@Bean("mainView")
	public SimpleView getMainView() throws IOException {
		return loader.load(props.getProperty("main.view.location"));
	}

	@Bean
	public MainController getMainController() throws IOException {
		return (MainController) getMainView().getController();
	}
}
