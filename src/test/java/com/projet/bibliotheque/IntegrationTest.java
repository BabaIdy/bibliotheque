package com.projet.bibliotheque;

import com.projet.bibliotheque.config.AsyncSyncConfiguration;
import com.projet.bibliotheque.config.EmbeddedKafka;
import com.projet.bibliotheque.config.EmbeddedSQL;
import com.projet.bibliotheque.config.JacksonConfiguration;
import com.projet.bibliotheque.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { BibliothequeApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
