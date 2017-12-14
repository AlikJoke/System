package ru.project.wtf.system.pdf;

import java.io.File;
import java.io.IOException;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;

/**
 * Интерфейс работы с содержанием приложения (в виде картинок). Используется для
 * сохранения и получения картинок.
 * 
 * @author Alimurad A. Ramazanov
 * @since 24.10.2017
 *
 */
public interface PdfHolder<T extends SystemObject<File>> {

	/**
	 * Возвращает список сохраненных ранее png.
	 * <p>
	 * 
	 * @see File
	 * @see Theory
	 * 
	 * @return не может быть {@code null}.
	 * @throws IllegalStateException
	 *             если сохраненного объекта нет.
	 */
	@NotNull
	T getPdf();

	/**
	 * Возвращает количество страниц pdf.
	 * 
	 * @return
	 */
	int size();

	/**
	 * Производит подмену файла теории и нарезает файл теории на страницы.
	 * 
	 * @throws IOException
	 * 
	 */
	void reload() throws IOException;
}
