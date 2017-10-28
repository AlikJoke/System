package ru.project.wtf.system.testloader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Объект, представляющий из себя один вопрос теста.
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public class Question {

	private final String question;
	private final List<String> variants;
	private final List<File> images;
	private final String aswer;

	@NotNullOrEmpty
	public String getQuestion() {
		return question;
	}

	@NotNullOrEmpty
	public List<String> getVariants() {
		return variants;
	}

	@NotNull
	public List<File> getImages() {
		return images;
	}

	@NotNullOrEmpty
	public String getAswer() {
		return aswer;
	}

	public Question(@NotNullOrEmpty final String question, @NotNullOrEmpty final String aswer,
			@NotNullOrEmpty final List<String> variants, final List<File> images) {
		super();
		this.question = question;
		this.aswer = aswer;
		this.variants = new LinkedList<>();
		this.variants.addAll(variants);
		this.images = new LinkedList<>();
		this.images.addAll(images);
	}

}
