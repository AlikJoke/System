package ru.project.wtf.system.testloader;

import java.io.File;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.model.SystemObject;
import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Объект, представляющий из себя тест, содержащий вопросы.
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public class Test extends SystemObject<Question> {

	public Test(@NotNull File sourceFile, @NotNullOrEmpty List<Question> questions) {
		super(sourceFile, questions);
	}

}
