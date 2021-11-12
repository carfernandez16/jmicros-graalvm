package com.jmicros.graalvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Predicate;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private void run() {
        logger.info("Simple graalvm Java App");

        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        Bindings bindings = graalEngine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("polyglot.js.allowHostAccess", true);
        bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);
        try {
            graalEngine.eval(loadContent("script.js"));
        } catch (ScriptException e) {
            logger.error("Cannot initialize graalvm js engine", e);
        } catch (IOException e) {
            logger.error("Cannot load script.js", e);
        }
    }

    public static String loadContent(String fileName) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream connectPayload = loader.getResourceAsStream(fileName);
        return readFromInputStream(connectPayload);
    }

    public static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static void main(String[] args) {
        new App().run();
    }

}
