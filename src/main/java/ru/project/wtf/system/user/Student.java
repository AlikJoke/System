package ru.project.wtf.system.user;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

public class Student extends User {

	public Student(@NotNullOrEmpty String id, @NotNull LocalDateTime authDateTime, @NotNull StudentInfo studentInfo) {
		super(id, authDateTime, true, studentInfo, null);
	}

}
