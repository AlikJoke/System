package ru.project.wtf.system.user;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

/**
 * Класс, представляющий из себя описание пользователя системы.
 * 
 * @author Alimurad A. Ramazanov
 * @since 23.10.2017
 *
 */
public class User {

	private final String id;
	private final LocalDateTime authDateTime;
	private final boolean isStudent;
	private final StudentInfo studentInfo;
	private final String pass;

	public User(@NotNullOrEmpty final String id, @NotNull final LocalDateTime authDateTime, final boolean isStudent,
			final @NotNull StudentInfo studentInfo, @NotNullOrEmpty final String password) {
		super();
		this.id = id;
		this.authDateTime = authDateTime;
		this.isStudent = isStudent;
		this.studentInfo = studentInfo;
		this.pass = password;
	}

	@NotNullOrEmpty
	public String getPassword() {
		if (isStudent()) {
			throw new IllegalStateException();
		}
		return pass;
	}

	@NotNullOrEmpty
	public String getId() {
		return id;
	}

	@NotNullOrEmpty
	public LocalDateTime getAuthDateTime() {
		return authDateTime;
	}

	public boolean isStudent() {
		return isStudent;
	}

	@NotNullOrEmpty
	public StudentInfo getStudentInfo() {
		if (!isStudent()) {
			throw new IllegalStateException();
		}
		return studentInfo;
	}

	/**
	 * Информация о студенте: имя, фамилия, группа.
	 * 
	 * @author Alimurad A. Ramazanov
	 * @since 23.10.2017
	 *
	 */
	public static class StudentInfo {

		private final String firstName;
		private final String lastName;
		private final String group;

		@NotNullOrEmpty
		public String getFirstName() {
			return firstName;
		}

		@NotNullOrEmpty
		public String getLastName() {
			return lastName;
		}

		@NotNullOrEmpty
		public String getGroup() {
			return group;
		}

		public StudentInfo(@NotNullOrEmpty final String firstName, @NotNullOrEmpty final String lastName,
				@NotNullOrEmpty final String group) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.group = group;
		}

	}

}
