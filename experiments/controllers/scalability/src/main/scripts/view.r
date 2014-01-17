#
#
# Plot the result of the sensitivity analyses of the diversity controllers
#
# Franck Chauvel
#

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}

#
# Generate the sensitivity to species count increase
#
pdf("species_scalability.pdf", height=6, width=goldenRatio(5))
data <- read.csv("species-scalability.csv");
strategies <- levels(data$controller);
plot(	data$species,
	data$duration,
	xlab="Species count",
	ylab="Time (in ms.)",
	type="n",
	frame=FALSE,
	log="xy",
	las=1);
ranges <- seq(1, length(strategies));
for (i in ranges) {
	d <- subset(data, controller == strategies[i]);
	lines(d$species, 
		d$duration,
		type="b",
		pch=i,
		lty=i);
}
legend("bottomright", legend=strategies, pch=ranges, lty=ranges, bty="n");
dev.off();

#
# Generate the sensitivity to individuals
#
pdf("individuals_scalability.pdf", height=6, width=goldenRatio(5))
data <- read.csv("individuals-scalability.csv");
strategies <- levels(data$controller);
plot(	data$individuals,
	data$duration,
	xlab="individuals count",
	ylab="Time (in ms.)",
	type="n",
	frame=FALSE,
	log="xy",
	las=1);
ranges <- seq(1, length(strategies));
for (i in ranges) {
	d <- subset(data, controller == strategies[i]);
	lines(d$individuals, 
		d$duration,
		type="b",
		pch=i,
		lty=i);
}
legend("topleft", legend=strategies, pch=ranges, lty=ranges, bty="n");
dev.off();