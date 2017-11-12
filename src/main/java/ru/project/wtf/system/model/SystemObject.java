package ru.project.wtf.system.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Описывает некоторый объект системы, который выгружается в некоторый объект и
 * имеет исходный файл.
 * 
 * @author Alimurad A. Ramazanov
 * @since 12.11.2017
 *
 * @param <T>
 *            - тип объекта.
 */
public abstract class SystemObject<T> {

	private final File sourceFile;
	private final List<T> objects;

	@NotNull
	public File getSourceFile() {
		return sourceFile;
	}

	@NotNullOrEmpty
	public List<T> getObjects() {
		return objects;
	}

	public SystemObject(@NotNull File sourceFile, @NotNullOrEmpty List<T> objects) {
		super();
		this.sourceFile = sourceFile;
		this.objects = new LinkedList<>();
		this.objects.addAll(objects);
	}
}
