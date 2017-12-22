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
	
	private Stage mainStage;
	private Stage authStage;

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
		primaryStage.setTitle(props.getProperty("fx.ui.title"));
		primaryStage.getIcons().add(new Image("img/kaf22.ico"));
		primaryStage.setScene(new Scene(view.getView()));
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(400);
		primaryStage.show();
	}

	public void gotoScene(final String beanName) {
		final SimpleView view = ctx.getBean(beanName, SimpleView.class);
		if (view == null) {
			throw new IllegalArgumentException("View can't be null! Check bean name!");
		}
		
		final Parent root = view.getView();

		if (Objects.equals("mainView", beanName)) {
			authStage = stageHolder.getStage();
			mainStage = new Stage();
			installScene(mainStage, root);
			stageHolder.holdStage(mainStage);
			stageHolder.getStage().setResizable(true);
			stageHolder.getStage().setMaximized(true);
			stageHolder.getStage().setTitle(props.getProperty("fx.ui.main.title"));
			authStage.hide();
			mainStage.show();
			
		} else {
			installScene(authStage, root);
			stageHolder.holdStage(authStage);
			stageHolder.getStage().centerOnScreen();
			stageHolder.getStage().setMaxHeight(431);
			stageHolder.getStage().setMaxWidth(410);
			stageHolder.getStage().setResizable(false);
			stageHolder.getStage().setTitle(props.getProperty("fx.ui.title"));
			mainStage.hide();
			authStage.show();
		}
		
		view.getController().refresh();
	}
	
	private void installScene(Stage stage, Parent root){
		if (root.getScene() == null) {
			Scene scene = new Scene(root);
			stage.setScene(scene);
		}
		else {
			stage.setScene(root.getScene());
		}
		stage.getIcons().add(new Image("img/kaf22.ico"));
	}
}
