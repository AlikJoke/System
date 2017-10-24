package ru.project.wtf.system.model;

import javax.validation.constraints.NotNull;

import javafx.stage.Stage;

/**
 * Интерфейс, представляющий методы для переключения сцен.
 * 
 * @author Alimurad A. Ramazanov
 * @since 22.10.2017
 * @version 1.0.0
 *
 */
public interface StageHolder {

	/**
	 * Сохраняет текущую сцену.
	 * <p>
	 * 
	 * @see Stage
	 * @param stage
	 *            - не может быть {@code null}.
	 */
	void holdStage(@NotNull Stage stage);

	/**
	 * Возвращает сохраненную сцену.
	 * <p>
	 * 
	 * @see Stage
	 * @return не может быть {@code null}.
	 */
	@NotNull
	Stage getStage();
}
