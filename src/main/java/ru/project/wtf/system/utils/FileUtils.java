package ru.project.wtf.system.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

public class FileUtils {

	public static final String DOT = ".";
	public static final String PDF_EXTENSION = "pdf";
	public static final String PNG_EXTENSION = "png";
	public static final String XML_EXTENSION = "xml";

	public static String getExtensionWithDot(@NotNullOrEmpty final String ext) {
		return DOT + ext;
	}

	public static File convertStreamToFile(@NotNullOrEmpty final String directory,
			@NotNullOrEmpty final String fileName) {
		if (StringUtils.isEmpty(directory) || StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException();
		}

		final InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(directory + "/" + fileName);
		if (is == null) {
			throw new RuntimeException();
		}

		final File file = new File(fileName);
		try {
			org.apache.commons.io.FileUtils.copyInputStreamToFile(is, file);
			file.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return file;
	}

	public static File convertStreamToFile(@NotNull final InputStream is, @NotNullOrEmpty final String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException();
		}

		final File file = new File(fileName);
		try {
			org.apache.commons.io.FileUtils.copyInputStreamToFile(is, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return file;
	}

	public static File copyFile(@NotNull final File source, @NotNull final File target) {
		try {
			org.apache.commons.io.FileUtils.copyFile(source, target);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return target;
	}
}
