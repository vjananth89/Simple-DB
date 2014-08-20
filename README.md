Simple-DB
=========

This is an implementation of Professor Sam Madden's Simple DB Assignment at MIT.

SimpleDB is a multi-user transactional database server written in Java, which interacts with Java client programs via JDBC. 
Consequently, the system is intentionally bare-bones. 
 

In this repository, an extended Java-based database management was 
implemented with the core modules required to access data stored on disk.


The repository implemented consists of 5 parts, namely:
1) Fields and Tuples
2) Catalog
3) BufferPool
4) HeapPage access method
5) HeapFile access method

Fields and Tuples
=================

Tuples in SimpleDB are relatively simple. They consist of a collection of Field objects, one per
field in the Tuple. Field is an interface that different data types (e.g., integer, string) implement.
Tuple objects are created by the underlying access methods (e.g., heap files, or B+-trees), as
described later. Tuples also have a type (or schema), called a tuple descriptor, represented by a
TupleDesc object. This object consists of a collection of Type objects, one per field in the tuple,
each of which describes the type of the corresponding field.

Catalog
========

The catalog in SimpleDB consists of a list of the tables and schemas of the tables that are
currently in the database. You need to implement two methods (getTupleDesc() and getDbFile())
of the Catalog class that currently throw UnsupportedOperationExceptions. These methods need
to use member variables (e.g., dbFiles, tds, dbFileIds) of the Catalog class. Associated with
each table is a TupleDesc object that allows operators to determine the types and number of fields
in a table

BufferPool
==========

The buffer pool is responsible for caching pages in memory that have been recently read from
disk. All operators read and write pages from various files on disk through this buffer pool.
The buffer pool consists of a fixed number of pages, defined by the numPages parameter to the
BufferPool constructor.

HeapPage Access Method
======================

SimpleDB stores heap files on disk in more or less the same format they are stored in memory.
Each file consists of page data arranged consecutively on disk. Each page consists of one or more
bytes representing the header, followed by the “BufferPool.PAGE SIZE - [# header bytes]” bytes
of actual page content. The number tuples that can fit into a page is defined by the formula:
tupsPerPage = floor((BufferPool.PAGE SIZE * 8) / (tuple size * 8 + 1)), where tuple size
is the size of a tuple in the page in bytes. The idea here is that each tuple requires one additional
bit of storage in the header. We compute the number of bits in a page (by mulitplying PAGE SIZE
by 8), and divide this quantity by the number of bits in a tuple (including this extra header bit) to
get the number of tuples per page. The floor operation rounds down to the nearest integer number
of tuples (we don’t want to store partial tuples on a page!)


Running Ant Build Targets in Eclipse
=====================================

If you want to run commands such as “ant test” or “ant systemtest,” right click on build.xml
in the Package Explorer. Select “Run As”, and then “Ant Build...” (note: don’t be confused with
“Ant Build”, which would not show a set of build targets to run). Then, in the “Targets” tab
of the next screen, check off the targets you want to run (probably “dist” and one of “test” or
“systemtest”). This should run the build targets and show you the results in Eclipse’s console
window.
```
If you run the unit tests using the test build target, you will see output similar to:
Buildfile: /Users/jhh/Documents/workspace/simpledb-1/build.xml
compile:
[mkdir] Created dir: /Users/jhh/Documents/workspace/simpledb-1/bin/src
[javac] Compiling 31 source files to /Users/jhh/Documents/workspace/simpledb-1/bin/src
testcompile:
[mkdir] Created dir: /Users/jhh/Documents/workspace/simpledb-1/bin/test
[javac] Compiling 10 source files to /Users/jhh/Documents/workspace/simpledb-1/bin/test
test:
[junit] Running simpledb.CatalogTest
[junit] Testsuite: simpledb.CatalogTest
[junit] Tests run: 3, Failures: 0, Errors: 2, Time elapsed: 0.027 sec
[junit] Tests run: 3, Failures: 0, Errors: 2, Time elapsed: 0.027 sec
[junit] Testcase: getTupleDesc took 0.007 sec
[junit] Caused an ERROR
[junit] Implement this
[junit] java.lang.UnsupportedOperationException: Implement this
[junit] at simpledb.Catalog.getTupleDesc(Catalog.java:86)
[junit] at simpledb.CatalogTest.getTupleDesc(CatalogTest.java:27)
[junit] Testcase: getTableId took 0.002 sec
[junit] Testcase: getDbFile took 0 sec
[junit] Caused an ERROR
[junit] Implement this
[junit] java.lang.UnsupportedOperationException: Implement this
[junit] at simpledb.Catalog.getDbFile(Catalog.java:96)
[junit] at simpledb.CatalogTest.getDbFile(CatalogTest.java:58)
BUILD FAILED
/Users/jhh/Documents/workspace/simpledb-1/build.xml:106: The following error occurred while executing this line:
/Users/jhh/Documents/workspace/simpledb-1/build.xml:58: Test simpledb.CatalogTest failed
Total time: 2 seconds
```


The above output indicates that two errors occurred. This is because the current code doesn’t
yet work. As you complete more components of the system, you will work towards passing additional
unit tests.
Running Individual Unit and System Tests in Eclipse
5To run a unit test or system test (both are JUnit tests, and can be initialized the same way),
go to the Package Explorer tab on the left side of your screen. Under the “simpledb-1” project,
open the “test” directory. Unit tests are found in the “simpledb” package, and system tests are
found in the “simpledb.systemtests” package. To run one of these tests, select the test (they are
all called *Test.java - don’t select TestUtil.java or SystemTestUtil.java), right click on it,
select “Run As”, and select “JUnit Test”. This will bring up a JUnit tab, which will tell you the
status of the individual tests within the JUnit test suite, and will show you exceptions and other
errors that will help you debug problems.


