package ru.project.wtf.system;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
	public void start(final Stage primaryStage) {
		stageHolder.holdStage(primaryStage);
		primaryStage.setTitle(this.props.getProperty("fx.ui.title"));
		primaryStage.getIcons().add(new Image("img/kaf22.ico"));
		primaryStage.setScene(new Scene(view.getView()));
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(400);
		primaryStage.setMaxHeight(500);
		primaryStage.setMaxWidth(500);
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

		if (Objects.equals("mainView", beanName)) {
			stageHolder.getStage().setResizable(true);
			stageHolder.getStage().setMinHeight(700);
			stageHolder.getStage().setMinWidth(900);
			stageHolder.getStage().setMaximized(true);
			stageHolder.getStage().centerOnScreen();
		} else {
			stageHolder.getStage().centerOnScreen();
			stageHolder.getStage().setMinHeight(400);
			stageHolder.getStage().setMinWidth(400);
			stageHolder.getStage().setMaxHeight(500);
			stageHolder.getStage().setMaxWidth(500);
			stageHolder.getStage().setResizable(false);
		}
		
		view.getController().refresh();
	}
}
