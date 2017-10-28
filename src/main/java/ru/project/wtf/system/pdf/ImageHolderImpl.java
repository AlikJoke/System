package ru.project.wtf.system.pdf;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ImageHolderImpl implements ImageHolder {

	private final List<File> images = new LinkedList<>();

	@Override
	public List<File> getImages() {
		return images;
	}

}
