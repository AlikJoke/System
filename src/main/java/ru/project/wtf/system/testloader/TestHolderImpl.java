package ru.project.wtf.system.testloader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ru.project.wtf.system.properties.Properties;

@Component
public class TestHolderImpl implements TestHolder {

	private Test test;

	@Autowired
	private TestLoader loader;

	@Autowired
	private Properties props;

	@Override
	public Test get() {
		return test;
	}

	@PostConstruct
	public void init() {
		final String directory;
		if (StringUtils.isEmpty(directory = props.getProperty("test.file.directory"))) {
			throw new RuntimeException();
		}

		final String fileName;
		if (StringUtils.isEmpty(fileName = props.getProperty("test.file.name"))) {
			throw new RuntimeException();
		}
		test = loader.load(directory, fileName);
	}

}
