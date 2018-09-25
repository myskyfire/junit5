/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.engine.script;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.junit.platform.commons.JUnitException;

class JavaCompilerScriptEngine extends AbstractScriptEngine implements Compilable {

	JavaCompilerScriptEngine() {
		context.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return compile(script).eval(context);
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		// TODO Create and register `JavaCompilerScriptEngineFactory`
		throw new UnsupportedOperationException();
	}

	@Override
	public CompiledScript compile(String script) {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("import javax.script.*;");
		builder.append("\n");
		builder.append("public class CompiledJavaScript extends CompiledScript {\n");
		builder.append("\n");
		builder.append("    private final ScriptEngine engine;\n");
		builder.append("\n");
		builder.append("    public CompiledJavaScript(ScriptEngine engine) {\n");
		builder.append("        this.engine = engine;\n");
		builder.append("    }\n");
		builder.append("\n");
		builder.append("    public Object eval(ScriptContext context) throws ScriptException {\n");
		// TODO Provide helpers around 'context' parameter.
		for (String scriptLine : script.split("\\R")) {
			builder.append("        ");
			builder.append(scriptLine);
		}
		builder.append("    }\n");
		builder.append("\n");
		builder.append("    public ScriptEngine getEngine() {\n");
		builder.append("        return engine;\n");
		builder.append("    }\n");
		builder.append("\n");
		builder.append("}\n");
		try {
			Class<?> evaluatorClass = JavaCompilerUtils.compile("CompiledJavaScript", builder.toString());
			Object evaluatorObject = evaluatorClass.getConstructor(ScriptEngine.class).newInstance(this);
			return (CompiledScript) evaluatorObject;
		}
		catch (Exception e) {
			throw new JUnitException("Compilation failed for: " + builder, e);
		}
	}

	@Override
	public CompiledScript compile(Reader script) {
		throw new UnsupportedOperationException();
	}
}
