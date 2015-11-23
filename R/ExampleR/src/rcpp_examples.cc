#include <Rcpp.h>

//' An Rcpp example which demonstrates matrices, vectors, and lists.
//'
//' Notice that we don't have to write an R wrapper; Rcpp does this for us
//' for every function including [[Rcpp::export]] as a comment above it. Als
//' note that in order to make the function public and get documentation, we
//' need an roxygen block, which has almost the same semantics as in R.
//'
//' Note: (this isn't obvious as of adding Rcpp) one disadvantage of using
//'       Rcpp instead of .Call is that the compile time goes up quite a lot.
//'       Nonetheless, the code is far easier to read.
//'
//' @export
//[[Rcpp::export]]
Rcpp::List rcpp_datatypes_example() {
  Rcpp::CharacterVector x = Rcpp::CharacterVector::create("foo", "bar");
  Rcpp::NumericVector y = Rcpp::NumericVector::create(0.0, 1.0);
  Rcpp::List unnamed_list = Rcpp::List::create(x, y);
  int n = 3;
  Rcpp::NumericMatrix numeric_matrix(n, n);
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      numeric_matrix(i, j) = i == j ? 1 : 0;
    }
  }
  Rcpp::List out = Rcpp::List::create(
      Rcpp::Named("unnamed_list") = unnamed_list,
      Rcpp::Named("numeric_matrix") = numeric_matrix
  );
  return out;
}
