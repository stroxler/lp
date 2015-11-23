#' A .Call hello world which takes no input, returns nothing.
#'
#' (I say it returns nothing, but technically .Call can't be void; it always
#' returns a SEXP. This one returns NULL, which I make concrete by returning
#' it from the R function as well)
#'
#' @export
void_print <- function() {
  result <- .Call("void_print")
  result
}

#' A .Call hello world which returns a hello message to R
#'
#' Note the use of PROTECT and UNPROTECT.
#'
#' One of the major benefits of Rcpp is that it handles details like this
#' for us.
return_a_string <- function() {
  result <- .Call("return_a_string")
  result
}

