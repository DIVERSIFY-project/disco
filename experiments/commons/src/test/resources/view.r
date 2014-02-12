#
# Sample R script used to visualise the mock experiment
#
# Franck Chauvel
#

data <- read.csv("test.csv");

pdf("test.pdf", height=6, width=8);

plot(data$rnorm);

dev.off();