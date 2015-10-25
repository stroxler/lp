# Chapter 2: intro to spark, patient linkage example

To download the data into a `_data` directory inside the working directory
(which .gitignore will ignore) run `bash get_data.sh`.

## Building

To build the code for the project, you can run `gradle build` from this
directory. If you only want to access the code from the spark shell, you could
also just boot up the shell and use the `:load` command.

## Loading in an interactive shell after building

To run an interactive spark shell against the build, run
```
spark-shell --jars build/libs/ch02.jar
```
and remember that to use any of the classes defined, you need to either 
`import ch2._` or include the prefix for anything you use.

It is also possible to `:load` the code from the shell, bypassing the
build. But the scala interpreter does not respect packages, so this can
be a little problematic.

## Running

Most of the code is intended to be runnable from inside the shell (there
is no batch job defined here, only functions). The highest-level
function, which corresponds to the last page in Chapter 2 (except that I
was lazy and only loaded one data file, not all of them), is the
`apply` method in the `Analysis` class.

To run it, after loading the spark shell witht the jar you can just run ```
ch2.Analysis(sc)
```
