#' An example of a public add function, which we test with testthat
#'
#' @export
public_testthat_add <- function(a, b) {
  a + b
}

#' An example of a private multiply function, which can also be tested
private_testthat_mult <- function(a, b) {
  a * b
}
