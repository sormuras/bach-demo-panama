# bach-demo-panama
Multi-release application demo using project Panama's API

##

```text
$ java build.java

Build runs on 19

javac --release=17 --module=com.github.sormuras.bach.demo.panama.app --module-source-path=./*/java -d classes/java-base

javac --release=19 --enable-preview --patch-module=com.github.sormuras.bach.demo.panama.app=com.github.sormuras.bach.demo.panama.app/java-19 --module-path=classes/base -d classes/java-19/com.github.sormuras.bach.demo.panama.app com.github.sormuras.bach.demo.panama.app/java-19/module-info.java com.github.sormuras.bach.demo.panama.app/java-19/demo/Main.java
Note: com.github.sormuras.bach.demo.panama.app/java-19/demo/Main.java uses preview features of Java SE 19.
Note: Recompile with -Xlint:preview for details.

jar --create --file=demo.jar --main-class demo.Main -C classes/java-base/com.github.sormuras.bach.demo.panama.app . --release 19 -C classes/java-19/com.github.sormuras.bach.demo.panama.app .
entry: META-INF/versions/19/demo/Main.class, has a class version incompatible with an earlier version
invalid multi-release jar file demo.jar deleted
Exception in thread "main" java.lang.RuntimeException: jar -> 1
        at build.run(build.java:40)
        at build.main(build.java:24)
```
