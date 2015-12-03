# This R script, when executed from the project root
# after the raw csv data already moved to inst/extdata,
# adds binary versions of the data to the package.
raw_train <- read.csv("inst/extdata/train.csv", header=TRUE)
raw_test <- read.csv("inst/extdata/test.csv", header=TRUE)
devtools::use_data(raw_train)
devtools::use_data(raw_test)
