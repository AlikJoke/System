package ru.project.wtf.system.testloader;

import javax.validation.constraints.NotNull;

/**
 * Интерфейс, с помощью которого можно получить доступ к тесту.
 * 
 * @see Test
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public interface TestHolder {

	/**
	 * Возвращает тест, загруженный в систему.
	 * <p>
	 * 
	 * @see Test
	 * @see TestLoader
	 * 
	 * @return не может быть {@code null}.
	 */
	@NotNull
	Test get();
}
