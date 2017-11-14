package ru.project.wtf.system.testloader;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;

/**
 * Объект, представляющий из себя тест, содержащий вопросы.
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public class Test extends SystemObject<Question> {

	private final Integer firstTestQuestionsCount;
	private final Integer secondTestQuestionsCount;
	private final Integer firstTestTime;
	private final Integer secondTestTime;

	public Test(@NotNull final File sourceFile, @NotNull final List<Question> objects,
			@NotNull final Integer firstTestQuestionsCount, @NotNull final Integer secondTestQuestionsCount,
			@NotNull final Integer firstTestTime, @NotNull final Integer secondTestTime) {
		super(sourceFile, objects);
		this.firstTestQuestionsCount = firstTestQuestionsCount;
		this.secondTestQuestionsCount = secondTestQuestionsCount;
		this.firstTestTime = firstTestTime;
		this.secondTestTime = secondTestTime;
	}

	@NotNull
	public Integer getFirstTestQuestionsCount() {
		return firstTestQuestionsCount;
	}

	@NotNull
	public Integer getSecondTestQuestionsCount() {
		return secondTestQuestionsCount;
	}

	@NotNull
	public Integer getFirstTestTime() {
		return firstTestTime;
	}

	@NotNull
	public Integer getSecondTestTime() {
		return secondTestTime;
	}

}
