#include <R.h>
#include <Rdefines.h>
#include <stdio.h>

SEXP void_print() {
  Rprintf("This is an example of a 'void' .Call: return NULL\n");
  return R_NilValue;
}

SEXP return_a_string() {
  SEXP result;
  PROTECT(result = NEW_CHARACTER(1));
  SET_STRING_ELT(result, 0, mkChar("Here's an example returned string"));
  UNPROTECT(1);
  return result;
}
