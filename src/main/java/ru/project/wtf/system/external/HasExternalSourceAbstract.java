package ru.project.wtf.system.external;

import java.io.File;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import ru.project.wtf.system.properties.Properties;

public abstract class HasExternalSourceAbstract implements HasExternalSource {

	@Autowired
	protected Properties props;
	
	@Override
	public File download() {
		// TODO
		return null;
	}

	@Override
	public void upload(@NotNull final File file) {
		// TODO
	}

}
