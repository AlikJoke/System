package ru.project.wtf.system.pdf;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Интерфейс работы с содержанием приложения (в виде картинок). Используется для
 * сохранения и получения картинок.
 * 
 * @author Alimurad A. Ramazanov
 * @since 24.10.2017
 *
 */
public interface ImageHolder {

	/**
	 * Возвращает список сохраненных ранее png.
	 * <p>
	 * 
	 * @see File
	 * 
	 * @return не может быть {@code null}.
	 * @throws IllegalStateException
	 *             если сохраненного объекта нет.
	 */
	@NotNull
	List<File> getImages();
}
