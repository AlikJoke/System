package ru.project.wtf.system.loader;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import ru.project.wtf.system.model.SimpleView;

/**
 * Интерфейс для загрузки представлений из файлов-моделей.
 * 
 * @author Alimurad A. Ramazanov
 * @since 22.10.2017
 * @version 1.0.0
 *
 */
public interface ViewLoader {

	/**
	 * Получение объекта {@linkplain SimpleView} по имени файла-модели.
	 * <p>
	 * 
	 * @param fileName
	 *            - имя файла; не может быть {@code null} или
	 *            {@code String#isEmpty()}.
	 * @return может быть {@code null}.
	 * @throws IOException
	 */
	SimpleView load(@NotNull @NotEmpty String fileName) throws IOException;
}

