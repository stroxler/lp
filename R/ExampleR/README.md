# Example R project

This example R project aims to demonstrate a software development
project in R, with the following components
  - unit testing via testthat
  - compiled code, both using .Call and Rcpp
  - a reasonable command-line workflow, driven by a makefile (this would
    be less necessary in Rstudio, or if you work exclusively in R; I like
    to use makefiles to 'wrap' language-specific build systems in part
    because it helps me memorize fewer features).

## To create

Run this command in the parent directory where you'd like the package to live:
```
package_name=ExampleR
R -e "devtools::create('$package_name')"
cd $package_name
```

## To call into C code in src using .Call

Include somewhere in your package (the conventional place for "anywhere"
`roxygen` tags is `zzz.R`) the line
```
#' @useDynLib ExampleR
NULL
```
This will cause roxygen to set up your NAMESPACE properly to load your
own source code; this eliminates the need for `dyn.load` calls that you
would need if you compiled / ran your code outside of the 
control of R's package system.

(The change to NAMESPACE is very simple; the reason you want to use
`roxygen` for doing this is not because it's complicated, but because
using `roxygen` to generate your NAMESPACE is an all-or-nothing
proposition and has many other benefits).

To compile and document your code - which are both part of building,
since document includes making the NAMESPACE file - run
```
R -e

## To add Rcpp support to the package

You can add the DESCRIPTION fields needed to use Rcpp via
devtools, by running `R -e "devtools::use_rcpp()"`.
You'll also need to add in some R file (by convention `zzz.R`)
```
#' @importFrom Rcpp sourceCpp
NULL
```
(aw well as the same `@useDynLib` roxygen tag used above) in order to
make use of Rcpp; this causes `roxygen2` to set your NAMESPACE correctly.

Note that when you use Rcpp, compilation tends to be slower. But you
don't have to write R wrappers; Rcpp handles both the R and C wrappers.
You *do* need to have roxygen tags (which start with `//'` in C++) for
any C++ function that you want exported.

You can use C++11 features in your C++ code if you want, but at this point
R doesn't generally support it: to do it, you have to
  - give up, for now, on submitting to CRAN
  - add a `Sys` call to add C++11 support; there's a comment in the
    `makefile` that shows what to do
  - ignore warnings or else figure out how to unset `-Wc++11` from the
    default R compiler flags

## Unit testing with testthat

You can set up using `testthat` by running `devtools::use_testthat()`.
This creates
 - a `tests` directory
 - a file `tests/testthat.R`, which you should not need to modify
 - a `tests/testthat` folder, in which you will put all your unit tests

The actual tests take the form of `context()` calls to give high-level
information about tests (in many cases you'll have just one per file),
plus individual tests which are calls to `test_that` and consist of
a description string followed by a block of code giving the test.

Note: `testthat` supports precision error by default; `expect_equal`
allows roundoff error. To test exact equality you would use `expect_identical.`
Some other useful testthat assertions are `expect_less_than` and
`expect_true`.

## Vignettes (and external data)

To create a vignette named `demo` using devtools, you can run
`R -e 'devtools::use_vignette("demo")'`, which will create
a `vignettes/` directory if it does not exist, and make a template
`demo.Rmd` file in that directory.

Since this package is only used to demo `R` packages, I use the
vignette to demonstrate loading raw data from a package.

Unfortunately, building the vignettes from the command line isn't
convenient. The best you can easily do is run `devtools::build()`,
which will by default create a tarball in the parent directory
of the project; the vignettes then live in `inst/doc/`.

If you install your package, you can also use `devtools::build_vignettes()`,
which builds them in-place; this won't work if you aren't installing
and you load stuff from your library though. The `devtools::build` command
does a temp install to get around this isue which `build_packages` does not
do.

To see how you can load external data, look at the top of
`vignettes/demo.Rmd`.

## TODO

I think the one significant high-level part of an R package that this
demo is missing is 'normal' data, which would go in `data/` rather than
`inst/extdata`, in the form of `.RData` files.

In case I don't wind up adding this feature to `ExampleR`, I want to 
mention the following:
 - Hadley Wickham's packages ebook has an example of this. His discussion
   is a little short on specifics, so google around a bit.
 - you generally want to use devtools::use_data to add the data to
   the project
