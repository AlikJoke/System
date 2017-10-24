package ru.project.wtf.system.controllers;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;

/**
 * Базовый контроллер.
 * 
 * @author Alimurad A. Ramazanov
 * @since 17.06.2017
 * @version 1.0.0
 *
 */
public abstract class BaseController {

	/**
	 * Метод инициализации бинов, наследующий данный класс.
	 * <p>
	 * 
	 * @see PostConstruct
	 */
	public abstract void init();

	/**
	 * Метод инициализации объекта-контроллера.
	 */
	@FXML
	public void initialize() {

	}

	/**
	 * Метод выхода из программы.
	 * <p>
	 * 
	 * @see System
	 */
	@FXML
	public void exit() {
		System.runFinalization();
		System.exit(0);
	}
}
