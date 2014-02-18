#
# Plot the results of the sensitivity analysis of the various diversity metrics.
#
# Franck Chauvel
#

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}


# Generate the sensitivity to distribution

pdf("results.pdf", height=10, width=goldenRatio(10))

results <- read.csv(file="results.csv"); 

plot(results);

dev.off();
