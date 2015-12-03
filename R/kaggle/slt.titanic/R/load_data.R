load_clean_data <- function() {
  # combine the train and test data with NA for survived in the train data
  test_survived = rep(NA, nrow(raw_test))
  raw_test_extended <- data.frame(raw_test, Survived = test_survived)
  combined <- rbind(raw_train, raw_test_extended)

  combined$Survived <- combined$Survived == "1"
  combined$Pclass <- as.factor(combined$Pclass)

  train <- combined[1:nrow(raw_train),]
  test <- combined[(nrow(raw_train)+1):nrow(combined),]

  list(train=train, test=test, combined=combined)
}

