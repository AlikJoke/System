package ru.project.wtf.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.project.wtf.system.model.SimpleView;
import ru.project.wtf.system.model.StageHolder;
import ru.project.wtf.system.properties.Properties;

@SpringBootApplication
public class SystemApplication extends JavaFxApplication {

	@Autowired
	private Properties props;

	@Autowired
	private SimpleView view;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private StageHolder stageHolder;

	private static SystemApplication instance;

	public SystemApplication() {
		instance = this;
	}

	public static SystemApplication getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		startApplication(SystemApplication.class, args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		stageHolder.holdStage(primaryStage);
		primaryStage.setTitle(this.props.getProperty("fx.ui.title"));
		primaryStage.setScene(new Scene(view.getView()));
		primaryStage.setResizable(true);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public void gotoScene(final String beanName) {
		final SimpleView view = ctx.getBean(beanName, SimpleView.class);
		if (view == null) {
			throw new IllegalArgumentException("View can't be null! Check bean name!");
		}

		final Parent page = view.getView();
		stageHolder.getStage().getScene().setRoot(page);
		stageHolder.getStage().sizeToScene();
	}
}
