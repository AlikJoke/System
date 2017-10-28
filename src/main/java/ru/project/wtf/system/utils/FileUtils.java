package ru.project.wtf.system.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.StringUtils;

public class FileUtils {

	public static File convertStreamToFile(@NotNullOrEmpty final String directory,
			@NotNullOrEmpty final String fileName) {
		if (StringUtils.isEmpty(directory) || StringUtils.isEmpty(fileName)) {
			throw new RuntimeException();
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
}
