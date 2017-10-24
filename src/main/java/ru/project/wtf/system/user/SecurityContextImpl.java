package ru.project.wtf.system.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ru.project.wtf.system.properties.Properties;

@Service
public class SecurityContextImpl implements SecurityContext {

	private final Map<String, String> login2passwordMap = new HashMap<>();
	private User currentUser;

	@Autowired
	private Properties props;

	@Override
	public void auth(final User user) throws SecurityException {
		if (isAuthenticated(user)) {
			throw new SecurityException("User is already authenticated");
		}

		if (!user.isStudent()) {
			if (!login2passwordMap.containsKey(user.getId())) {
				throw new SecurityException("Can't find user with the same login!");
			}

			if (!Objects.equals(login2passwordMap.get(user.getId()), user.getPassword())) {
				throw new SecurityException("Incorrect password!");
			}
		}

		this.currentUser = user;
	}

	@Override
	public boolean isAuthenticated(final User user) {
		return currentUser != null && Objects.equals(currentUser.getId(), user.getId());
	}

	@Override
	public User getAuthUser() {
		return currentUser;
	}

	@PostConstruct
	public void init() throws IOException {
		final InputStream securityInfo = this.getClass().getClassLoader()
				.getResourceAsStream(props.getProperty("security.path"));
		if (securityInfo == null) {
			throw new RuntimeException("Security config not found!");
		}

		final java.util.Properties properties = new java.util.Properties();
		properties.load(securityInfo);

		final String[] loginsArray = StringUtils.tokenizeToStringArray(properties.getProperty("login"), ";,");
		final String[] passArray = StringUtils.tokenizeToStringArray(properties.getProperty("password"), ";,");
		for (int i = 0; i < loginsArray.length; i++) {
			if (StringUtils.hasLength(loginsArray[i]) && StringUtils.hasLength(passArray[i])) {
				login2passwordMap.putIfAbsent(loginsArray[i], passArray[i]);
			}
		}
	}

	@Override
	public void logout() {
		this.currentUser = null;
	}
}
