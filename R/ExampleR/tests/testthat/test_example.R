context('public add example')

test_that('Returns 0 if given two zeros', {
   actual <- public_testthat_add(0, 0)
   expected <- 0
   expect_equal(actual, expected)
})

test_that('Returns 2 if given two ones', {
   actual <- public_testthat_add(1, 1)
   expected <- 2
   expect_equal(actual, expected)
})

context('private mult example')

test_that('Returns 0 if given two zeros', {
   actual <- private_testthat_mult(0, 0)
   expected <- 0
   expect_equal(actual, expected)
})

test_that('Returns 1 if given two ones', {
   actual <- private_testthat_mult(1, 1)
   expected <- 1
   expect_equal(actual, expected)
})
