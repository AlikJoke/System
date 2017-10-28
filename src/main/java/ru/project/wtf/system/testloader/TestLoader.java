package ru.project.wtf.system.testloader;

import java.io.File;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Интерфейс, с помощью которого производится загрузка теста в виде объекта из
 * файла и загрузка нового теста.
 * 
 * @see Test
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public interface TestLoader {

	/**
	 * Производит загрузку файла с указанным путем и именем в виде объекта
	 * класса {@link Test}.
	 * <p>
	 * 
	 * @see Test
	 * 
	 * @param directory
	 *            - путь до директории с тестом; не может быть {@code null}.
	 * @param fileName
	 *            - имя файла с тестом; не может быть {@code null}/
	 * @return не может быть {@code null}.
	 */
	@NotNull
	Test load(@NotNullOrEmpty String directory, @NotNullOrEmpty String fileName);

	/**
	 * Производит замену файла теста на новый файл.
	 * <p>
	 * 
	 * @param file
	 *            - файл для замены; не может быть {@code null}.
	 */
	void replace(@NotNull File file);
}
