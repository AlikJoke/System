package ru.project.wtf.system.pdf;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;
import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Описывает из себя объект справки в системе.
 * 
 * @see SystemObject
 * 
 * @author Alimurad A. Ramazanov
 * @since 14.12.2017
 *
 */
public class Reference extends SystemObject<File> {

	public Reference(@NotNull File sourceFile, @NotNullOrEmpty List<File> files) {
		super(sourceFile, files);
	}
}
