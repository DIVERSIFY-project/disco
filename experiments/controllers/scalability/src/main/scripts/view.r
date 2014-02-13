#
#
# Plot the result of the scalability analyses of the diversity controllers
#
# Franck Chauvel
#

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}

data <- read.csv("results.csv")

strategies <- levels(data$strategy);
cs <- 1 + seq(1, length(strategies));

#
# Generate the sensitivity to species count increase
#
pdf("individuals.pdf", height=6, width=goldenRatio(5))

boxplot(data$duration ~ data$strategy + data$individual.count, 
        col=cs, 
        xlab="Total number of individuals",
        ylab="Duration (ms.)",
        axes=FALSE);

gn <- length(unique(data$individual.count));
bn <- length(strategies);       
axis(1, at=seq(1.5, gn * bn, bn), labels=unique(data$individual.count));
axis(2, las=1);

legend("topleft", legend=strategies, fill=cs, bty="n");

dev.off();

#
# Generate the sensitivity to individuals
#
pdf("species.pdf", height=6, width=goldenRatio(5))

boxplot(data$duration ~ data$strategy + data$species.count, 
        col=cs, 
        xlab="Total number of species",
        ylab="Duration (ms.)",
        axes=FALSE);

gn <- length(unique(data$species.count));
bn <- length(strategies);       
axis(1, at=seq(1.5, gn * bn, bn), labels=unique(data$species.count));
axis(2, las=1);

legend("topleft", legend=strategies, fill=cs, bty="n");

dev.off();
