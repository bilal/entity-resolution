Entity Resolution
=================

A simple maven project. 

**Prerequisites**: Java and Maven

Compile and package:

```
git clone git://github.com/bilal/entity-resolution.git
cd entity-resolution
mvn assembly:single
```

or you can directly get the `jar` from the target directory.

Run:

```
java -jar target/entity-resolution-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/products.txt path/to/listings.txt
```

The results are written to `results.txt`.


