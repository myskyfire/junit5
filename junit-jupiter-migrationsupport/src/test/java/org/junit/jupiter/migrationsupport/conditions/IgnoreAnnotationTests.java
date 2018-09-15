/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.migrationsupport.conditions;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.EnableJUnit4MigrationSupport;
import org.junit.rules.ExpectedException;

/**
 * Tests for JUnit 4's {@link Ignore @Ignore} support in JUnit Jupiter.
 *
 * @since 5.4
 */
@EnableJUnit4MigrationSupport
// @ExtendWith(IgnoreCondition.class)
class IgnoreAnnotationTests {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	@Ignore
	void ignored() {
		fail("This method should have been disabled via @Ignore");
	}

	@Test
	@Ignore("Ignore me!")
	void ignoredWithMessage() {
		fail("This method should have been disabled via @Ignore");
	}

	@Test
	// @Ignore
	void notIgnored() {
		/* no-op */
	}

	@Test
	void notIgnoredButUsesExpectedExceptionRule() {
		exception.expect(RuntimeException.class);
		throw new RuntimeException("expected");
	}

}
