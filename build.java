import java.util.spi.ToolProvider;
import java.util.stream.Stream;

class build {
    public static void main(String[] args) {
        System.out.println("Build runs on " + System.getProperty("java.version"));
        // Java 17 = base
        run("javac", 
            "--release=17", 
            "--module=com.github.sormuras.bach.demo.panama.app", 
            "--module-source-path=./*/java", 
            "-d", "classes/java-base");
        // Java 19
        run("javac",           
            "--release=19",
            "--enable-preview",
            "--patch-module=com.github.sormuras.bach.demo.panama.app=com.github.sormuras.bach.demo.panama.app/java-19", 
            "--module-path=classes/base",
            "-d", "classes/java-19/com.github.sormuras.bach.demo.panama.app",
            "com.github.sormuras.bach.demo.panama.app/java-19/module-info.java",
            "com.github.sormuras.bach.demo.panama.app/java-19/demo/Main.java"
            );
        // Create multi-release JAR file
        run("jar",
            "--create", "--file=demo.jar",
            "--main-class", "demo.Main",
            // base
            "-C", "classes/java-base/com.github.sormuras.bach.demo.panama.app", ".",
            // Java 19
            "--release", "19",
            "-C", "classes/java-19/com.github.sormuras.bach.demo.panama.app", "."
        );
    }

    static void run(String name, Object... args) {
        var tool = ToolProvider.findFirst(name).orElseThrow();
        var strings = Stream.of(args).map(Object::toString).toList();
        System.out.println(name + " " + String.join(" ", strings));
        var code = tool.run(System.out, System.err, strings.toArray(String[]::new));
        if (code != 0) throw new RuntimeException(name + " -> " + code);
      }
}
