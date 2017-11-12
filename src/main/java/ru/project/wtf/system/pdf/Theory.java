package ru.project.wtf.system.pdf;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;
import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Описывает из себя объект теории в системе.
 * 
 * @see SystemObject
 * 
 * @author Alimurad A. Ramazanov
 * @since 12.11.2017
 *
 */
public class Theory extends SystemObject<File> {

	public Theory(@NotNull File sourceFile, @NotNullOrEmpty List<File> files) {
		super(sourceFile, files);
	}
}
