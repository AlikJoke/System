package ru.project.wtf.system.pdf;

import javax.validation.constraints.NotNull;

import org.jpedal.PdfDecoder;

/**
 * Интерфейс работы с pdf-содержанием приложения. Используется для сохранения и
 * получения pdf.
 * 
 * @author Alimurad A. Ramazanov
 * @since 24.10.2017
 *
 */
public interface PdfHolder {

	/**
	 * Возвращает сохраненную ранее pdf.
	 * <p>
	 * 
	 * @see {@linkplain PdfDecoder}
	 * 
	 * @return не может быть {@code null}.
	 * @throws IllegalStateException
	 *             если сохраненного объекта нет.
	 */
	@NotNull
	PdfDecoder getPdf();
}
