#' @useDynLib ExampleR
NULL
# ^ if you are using .C or .Call, this is all you would need

#' @importFrom Rcpp sourceCpp
NULL
# ^ you need to add this to use Rcpp

# Note that roxygen can only find comments attached to objects, so it's
# important to use the NULL above (I had a hard time with this at first)
