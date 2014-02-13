#
# Plot the results of the sensitivity analysis of the various diversity metrics.
#
# Franck Chauvel
#

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}


# Generate the sensitivity to distribution

pdf("distribution.pdf", height=6, width=goldenRatio(5))

distribution <- read.csv(file="distribution.csv", sep=",", header=TRUE); 

plot(x=NULL,
     y=NULL,
     xlab="Percentage of the population in specie no. 1",
     ylab="Diversity Level",
     xlim=range(50, 100),
     ylim=range(0, 1),
     las=1,
     frame=FALSE
);

names <- levels(distribution$metric);

for (i in seq(1, length(names))) {
	d <- subset(distribution, metric == names[i]);
	lines(d$Specie.no.1, d$normalised, type="l", lty=i);
}

legend("bottomleft", legend=levels(distribution$metric), lty=seq(1, length(names)), bty="n");

dev.off();


#
# Generate the view of the sensitivity to species count
#

pdf("species.pdf", height=6, width=goldenRatio(5))


species <- read.csv(file="species.csv", sep=",", header=TRUE);


#data <- subset(species, kind == "normalised");

plot(x=NULL,
     y=NULL,
     xlab="Species Count",
     ylab="Diversity Level",
     xlim=range(0, 100),
     ylim=range(0, 1),
     las=1,
     frame=FALSE
);

names <- levels(species$metric);
for (i in seq(1, length(names))) {
	d <- subset(species, metric == names[i]);
	lines(d$species.count, d$normalised, type="l", lty=i);
}

legend("topleft", legend=levels(species$metric), lty=seq(length(names)), bty="n");


dev.off(); 


# Generate the sensitivity to individuals

pdf("individuals.pdf", height=6, width=goldenRatio(5))

individuals <- read.csv(file="individuals.csv", sep=",", header=TRUE); 

plot(x=NULL,
     y=NULL,
     xlab="Total number of individuals",
     ylab="Diversity Level",
     xlim=range(100, 200),
     ylim=range(0, 1),
     las=1,
     frame=FALSE
);

names <- levels(individuals$metric);

for (i in seq(1, length(names))) {
	d <- subset(individuals, metric == names[i]);
	lines(d$individuals.count, d$normalised, type="l", lty=i);
}

legend("bottomright", legend=levels(individuals$metric), lty=seq(1, length(names)), bty="n");

dev.off();
