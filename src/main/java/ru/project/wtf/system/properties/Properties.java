package ru.project.wtf.system.properties;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.env.Environment;

/**
 * Интерфейс для получения свойств из файлов конфигурации.
 * 
 * @author Alimurad A. Ramazanov
 * @since 22.10.2017
 * @version 1.0.0
 *
 */
public interface Properties {

	/**
	 * Получение некоторого свойства по имени.
	 * <p>
	 * 
	 * @see Environment
	 * @param name
	 *            - имя свойства, не может быть {@code null}.
	 * @return свойство, может быть {@code null}.
	 */
	String getProperty(@NotNull @NotEmpty String name);
}
