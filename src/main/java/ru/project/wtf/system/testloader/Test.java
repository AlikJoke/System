package ru.project.wtf.system.testloader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Объект, представляющий из себя тест, содержащий вопросы.
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public class Test {

	private final File sourceFile;
	private final List<Question> questions;

	@NotNull
	public File getSourceFile() {
		return sourceFile;
	}

	@NotNullOrEmpty
	public List<Question> getQuestions() {
		return questions;
	}

	public Test(@NotNull File sourceFile, @NotNullOrEmpty List<Question> questions) {
		super();
		this.sourceFile = sourceFile;
		this.questions = new LinkedList<>();
		this.questions.addAll(questions);
	}

}
