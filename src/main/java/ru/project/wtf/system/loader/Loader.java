package ru.project.wtf.system.loader;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;
import ru.project.wtf.system.pdf.Theory;
import ru.project.wtf.system.testloader.Test;
import ru.project.wtf.system.utils.NotNullOrEmpty;

public interface Loader<T> {

	/**
	 * Производит загрузку файла с указанным путем и именем в виде объекта
	 * нужного класса класса.
	 * <p>
	 * 
	 * @see Test
	 * @see Theory
	 * @see SystemObject
	 * 
	 * @param directory
	 *            - путь до директории с тестом; может быть {@code null}.
	 * @param fileName
	 *            - имя файла с тестом; не может быть {@code null}/
	 * @return не может быть {@code null}.
	 */
	@NotNull
	T load(@NotNullOrEmpty String directory, @NotNullOrEmpty String fileName);

	/**
	 * Производит выгрузку объекта.
	 * <p>
	 * 
	 * @see SystemObject
	 * 
	 * @param object - объект для выгрузки; не может быть {@code null}.
	 */
	void upload(@NotNull T object);
}
