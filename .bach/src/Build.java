import java.nio.file.Path;
import java.util.List;
import java.util.spi.ToolProvider;
import java.util.stream.Stream;

class Build {
  public static void main(String[] args) {
    System.out.println("Build runs on Java " + System.getProperty("java.version"));
    // J:\tools\vs\2022\Community\VC\Auxiliary\Build\vcvars64.bat
//    run(
//        "cl",
//        "/LD",
//        "demo/native/helloworld.c",
//        "/link",
//        "/EXPORT:helloworld");
    run(
        Path.of("J:/tools/jextract/jextract-22", "runtime", "bin", "java"),
        "--module=org.openjdk.jextract/org.openjdk.jextract.JextractTool",
        "--output","demo/java",
        "--target-package","demo",
        "--library","helloworld",
        "demo/native/helloworld.h");
    run("javac", "-d", "out", "--module-source-path", "./*/java",  "--module=demo");
    run("java", "--enable-native-access=demo", "--module-path=out", "--module=demo/demo.Main");
  }

  private static void run(String name, Object... arguments) {
    var found = ToolProvider.findFirst(name);
    if (found.isPresent()) {
      var args = Stream.of(arguments).map(Object::toString).toArray(String[]::new);
      System.out.println("| " + name + " " + String.join(" ", args));
      var tool = found.get();
      var loader = Thread.currentThread().getContextClassLoader();
      try {
        Thread.currentThread().setContextClassLoader(tool.getClass().getClassLoader());
        var code = tool.run(System.out, System.err, args);
        if (code == 0) return;
        throw new RuntimeException(name + " returned non-zero exit code: " + code);
      } finally {
        Thread.currentThread().setContextClassLoader(loader);
      }
    }
    var program = Path.of(System.getProperty("java.home"), "bin", name);
    run(program, arguments);
  }

  private static void run(Path program, Object... arguments) {
    var args = Stream.of(arguments).map(Object::toString).toArray(String[]::new);
    var path = program.toAbsolutePath();
    var name = path.getFileName().toString();
    System.out.println("| " + name + " " + String.join(" ", args));
    try {
      var builder = new ProcessBuilder(program.toString());
      builder.command().addAll(List.of(args));
      var process = builder.inheritIO().start();
      var code = process.waitFor();
      if (code == 0) return;
      throw new Error(name + " returned non-zero exit code: " + code);
    } catch (Exception exception) {
      throw new RuntimeException(name + " failed.", exception);
    }
  }
}
