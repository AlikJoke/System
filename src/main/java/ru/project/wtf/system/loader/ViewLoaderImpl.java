package ru.project.wtf.system.loader;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javafx.fxml.FXMLLoader;
import ru.project.wtf.system.model.SimpleView;

@Service
public class ViewLoaderImpl implements ViewLoader {

	@Override
	public SimpleView load(final String fileName) throws IOException {
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException();
		}

		InputStream stream = null;
		FXMLLoader loader = null;
		try {
			stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
			if (stream == null) {
				throw new RuntimeException();
			}

			loader = new FXMLLoader();
			loader.load(stream);
			return new SimpleView(loader.getController(), loader.getRoot());
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

}
