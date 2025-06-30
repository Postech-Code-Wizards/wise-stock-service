package br.com.wise.infrastructure.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class MessageService {

    @Inject
    @ConfigProperty(name="quarkus.default-locale", defaultValue = "pt-BR")
    Locale locale;

    public String getMessage(String key, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        String message = bundle.getString(key);
        return MessageFormat.format(message, params);
    }

}