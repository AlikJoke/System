package ru.project.wtf.system.user;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

public class Professor extends User {

	public Professor(@NotNullOrEmpty String id, @NotNull LocalDateTime authDateTime, @NotNullOrEmpty String password) {
		super(id, authDateTime, false, null, password);
	}

}
