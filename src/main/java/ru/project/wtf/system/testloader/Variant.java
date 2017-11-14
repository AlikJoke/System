package ru.project.wtf.system.testloader;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import ru.project.wtf.system.utils.NotNullOrEmpty;

public class Variant {

	private final Integer id;
	private final String text;

	@NotNull
	public Integer getId() {
		return id;
	}

	@NotNullOrEmpty
	public String getText() {
		return text;
	}

	public Variant(@NotNull final Integer id, @NotNullOrEmpty final String text) {
		super();
		this.id = id;
		this.text = text;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Variant)) {
			return false;
		}

		final Variant variant = (Variant) obj;
		return Objects.equals(variant.getId(), id);
	}
}
