package ru.project.wtf.system.user;

import javax.validation.constraints.NotNull;

/**
 * Интерфейс для работы с авторизацией в приложении.
 * 
 * @see {@linkplain User}
 * 
 * @author Alimurad A. Ramazanov
 * @since 23.10.2017
 *
 */
public interface SecurityContext {

	/**
	 * Производит авторизацию в приложении для переданного пользователя.
	 * <p>
	 * 
	 * @see {@linkplain User}
	 * 
	 * @param user
	 *            - объект пользователя; не может быть {@code null}.
	 * @throws SecurityException,
	 *             если авторизация уже была выполнена или данные для
	 *             авторизации некорректны.
	 */
	void auth(@NotNull User user) throws SecurityException;

	/**
	 * Производит выход из системы текущего пользователя.
	 * 
	 */
	void logout();

	/**
	 * Производит проверку авторизованности в приложении для переданного
	 * пользователя.
	 * <p>
	 * 
	 * @see {@linkplain User}
	 * 
	 * @param user
	 *            - объект пользователя; не может быть {@code null}.
	 * @return {@code true} - если пользователь уже авторизован, {@code false} -
	 *         иначе.
	 */
	boolean isAuthenticated(@NotNull User user);

	/**
	 * Возвращает авторизованного в приложении пользователя.
	 * <p>
	 * 
	 * @see {@linkplain User}
	 * 
	 * @return может быть {@code null}.
	 */
	User getAuthUser();
}
