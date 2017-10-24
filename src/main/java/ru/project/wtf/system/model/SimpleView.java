package ru.project.wtf.system.model;

import javafx.scene.Parent;
import ru.project.wtf.system.controllers.BaseController;

/**
 * Связка представление - контроллер.
 * 
 * @author Alimurad A. Ramazanov
 * @since 22.10.2017
 * @version 1.0.0
 *
 */
public class SimpleView {

	private final BaseController controller;
	private final Parent view;

	public BaseController getController() {
		return controller;
	}

	public Parent getView() {
		return view;
	}

	public SimpleView(final BaseController controller, final Parent view) {
		super();
		this.controller = controller;
		this.view = view;
	}
}
