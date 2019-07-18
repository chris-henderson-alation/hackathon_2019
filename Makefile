java_run: lib
	javac HelloWorld.java && java -Djava.library.path=mylib/target/debug/ HelloWorld
	javac fastyaml/src/main/java/Main.java

.PHONY: lib

javah:
	cd fastyaml/src/main/java/ && javah serialize.YAML

lib:
	cd mde && cargo build --release