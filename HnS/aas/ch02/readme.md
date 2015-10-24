# Chapter 2: intro to spark, patient linkage example

To download the data into a `_data` directory inside the working directory
(which .gitignore will ignore) run `bash get_data.sh`.

To build the code for the project, you can run `gradle build` from this
directory. If you only want to access the code from the spark shell, you could
also just boot up the shell and use the `:load` command.

To run an interactive spark shell against the build, run
```
spark-shell --jars build/libs/ch02.jar
```
and remember that to use any of the classes defined, you need to either 
`import ch2._` or include the prefix for anything you use.
