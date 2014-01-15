#
# Plot the results of the sensitivity analysis of the various diversity metrics.
#
# Franck Chauvel
#

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}


# Generate the sensitivity to distribution

pdf("individuals.pdf", height=6, width=goldenRatio(5))


individuals <- read.csv(file="individuals.csv", sep=",", header=TRUE); 


data <- subset(individuals, kind == " normalised");


plot(x=NULL,
     y=NULL,
     xlab="Percentage of the population in specie no. 1",
     ylab="Diversity Level",
     xlim=range(0, 50),
     ylim=range(0, 1),
     las=1,
     frame=FALSE
);

for (i in seq(1, 3)) {
	d <- subset(data, metric == levels(data$metric)[i]);
	lines(d$specie..1, d$value, type="l", lty=i);
}

legend("bottomright", legend=levels(data$metric), lty=c(1, 2, 3), bty="n");

dev.off();


#
# Generate the view of the sensitivity to species count
#

pdf("species.pdf", height=6, width=goldenRatio(5))


species <- read.csv(file="species.csv", sep=",", header=TRUE);


data <- subset(species, kind == " normalised");

plot(x=NULL,
     y=NULL,
     xlab="Species Count",
     ylab="Diversity Level",
     xlim=range(0, 100),
     ylim=range(0, 1),
     las=1,
     frame=FALSE
);

for (i in seq(1, 3)) {
	d <- subset(data, metric == levels(data$metric)[i]);
	lines(d$species.count, d$value, type="l", lty=i);
}

legend("bottomright", legend=levels(data$metric), lty=c(1, 2, 3), bty="n");


dev.off(); 