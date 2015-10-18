# Code to accompany "Testing in Scala" by Daniel Hinojosa

## Install

On mac, you can just run `brew install scala` and I believe you get `sbt`
without doing anything; if not, you can just `brew install sbt` at worst.
The first time you actually run `sbt`, it may download some dependencies.

## build.sbt

I have some notes on the build.sbt, because not everything about it
is obvious all the time. I also have notes on sbt commands and
functionality, some of which is very cool; as a build tool I think
gradle may be superior, but the continous testing and console features
of `sbt` are powerful enough to perhaps make it worth it.

## Guide to the code itself

The code is pretty simple. If you git grep, you can find some stuff
that is not obvious coming from Java:
 - A few examples of pattern matching for handing type expressions and Options
 - A case of a class whose primary constructor is private
 - At least one example of a trait, plus an object of the same name
 - A Unit (void) method, which has a special optional syntax

Still, overall there are no big surprises in the production code. Mostly,
the author tried to provide just enough meat for the tests.

## Guide to the tests

In alphabetical order, here is a short guide that should be helpful if
you are looking for reference examples on some particular idea:
- AlbumTest: a basic scalatest example, demonstrating most of the
  scalatesting basics...
   - should matching behavior and describe / it blocks
   - GivenWhenThen blocks for more descriptive tests
   - Tags for adding test metadata
   - info calls for more informative output
   - ignore and pending for incomplete or undesired tests
- ArtistJUnitSuite: the name pretty much says it: scalatest supports
  running JUnit tests, and this is a simple demo.
- ArtistTestNGSuite actually does not test Artist, it is just a demo
  of some TestNG stuff not found in Junit. The JUnit version, though,
  could have been converted to TestNG almost without change.
- MustMatcherSpec: a simple test, almost independent of the production
  code, illustrating a lot of the basic scalatest functionality for
  different types of assertions.
- ShouldMatcherSpec: identical to MustMatcherSpec except using the ability
  to substite `must` for `should`
- TrackSpec: this demonstrates exception checking in scalatest. See the
  second test, where we actually get the exception in a variable to check it.
