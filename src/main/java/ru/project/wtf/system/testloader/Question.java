package ru.project.wtf.system.testloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private final Integer id;
	private final String question;
	private final Map<Integer, Variant> variants;
	private final List<File> images;
	private final Integer complexity;
	private final List<String> answer;

	@NotNullOrEmpty
	public String getQuestion() {
		return question;
	}

	public List<String> getAnswer() {
		return answer;
	}

	@NotNullOrEmpty
	public List<Variant> getVariants() {
		return new ArrayList<>(variants.values());
	}

	@NotNullOrEmpty
	public String getQuestionTitle() {
		return id + ". " + question;
	}

	@NotNull
	public Integer getComplexity() {
		return complexity;
	}

	@NotNull
	public Map<Integer, Variant> getVariantsMap() {
		return variants;
	}

	@NotNull
	public Integer getId() {
		return id;
	}

	@NotNull
	public List<File> getImages() {
		return images;
	}

	public List<Integer> getRightVariants() {
		return variants.isEmpty() ? Collections.emptyList()
				: answer.stream().map(answer -> Integer.parseInt(answer)).collect(Collectors.toList());
	}

	public Question(@NotNull final Integer id, @NotNullOrEmpty final String question,
			@NotNull final Map<Integer, Variant> variants, @NotNull final List<File> images,
			@NotNull final Integer complexity, @NotNull final List<String> answer) {
		super();
		this.id = id;
		this.question = question;
		this.variants = new LinkedHashMap<>();
		this.variants.putAll(variants);
		this.images = new LinkedList<>();
		this.images.addAll(images);
		this.complexity = complexity;
		this.answer = answer;
	}

}
