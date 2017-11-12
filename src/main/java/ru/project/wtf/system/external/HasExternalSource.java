package ru.project.wtf.system.external;

import java.io.File;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.pdf.PdfLoader;
import ru.project.wtf.system.properties.Properties;
import ru.project.wtf.system.testloader.TestLoader;
import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Интерфейс-маркер, обозначающий сущности, которые могут быть загружены из
 * внешнего источника данных и выгружены во внешний источник.
 * 
 * @author Alimurad A. Ramazanov
 * @since 12.11.2017
 *
 */
public interface HasExternalSource {

	/**
	 * Производит операцию выкачивания данных из внешнего источника. Ресурс, по
	 * которому находится внешний источник - указывается в системной переменной
	 * {@literal external.source.<type>}.
	 * <p>
	 * 
	 * @see File
	 * @see TestLoader
	 * @see PdfLoader
	 * 
	 * @return может быть {@code null}, если источник данных не содержит данных
	 *         или отсутствует соединение с ним.
	 */
	File download();

	/**
	 * Производит операцию загрузки данных во внешний источник. Ресурс, по
	 * которому находится внешний источник - указывается в системной переменной
	 * {@literal external.source.<type>}.
	 * <p>
	 * 
	 * @see File
	 * @see TestLoader
	 * @see PdfLoader
	 * 
	 * @param file
	 *            - файл для выгрузки; не может быть {@code null}.
	 */
	void upload(@NotNull File file);

	/**
	 * Возвращает имя системной переменной для конкретного сервиса, выполняющего
	 * загрузку / выгрузку данных во внешний источник.
	 * <p>
	 * 
	 * @see Properties
	 * 
	 * @return не может быть {@code null}.
	 */
	@NotNullOrEmpty
	String getSourceKey();
}
