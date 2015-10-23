This is a modified version of the wordcount example on 42 of Apress
Pro Hadoop book by Sameer Wadkar, Madhu Siddalingaiah, and Jason Venner.

I made the example better suited to my toy dataset by splitting on commas and
then spaces. I also made it more interesting to me by using a little bit of
Java 8. The formatting is funny because I decided to try and do it in vim,
without an ide... not a good environment to work in.

Oddly, the output seems to have a tabbing issue; 'cottontail' overruns its
own count. Not sure what is up with that. But other than this rather serious
formatting bug, it seems to run!

It is built using apache maven. The pom is set up so that it will build
with java 8 support (see the maven-compiler plugin section), with
apache commons available (see the dependencies section), and with a
"fat-jar" (see the maven-assembly plugin section). This allows you to run
it with the external dependency - but with hadoop excluded - without needing
the --jars option when calling `hadoop jar`.

To build the jar, just run `mvn package` from this directory.
