# Visualization of the performance of diversity controllers in a
# single run.
#
# Franck Chauvel

goldenRatio <- function(height) {
	return (height * ((1 + sqrt(5)) /2));
}

#png("results.png", height=500, width=goldenRatio(500))
pdf("results.pdf", height=5, width=goldenRatio(5))
#win.metafile("results.emf", height=5, width=goldenRatio(5))


data <- read.table(file="results.csv", header=TRUE, sep=",");

plot(data$time, data$diversity, 
		ylim=c(0, 1),
		xlim=range(data$time),
		xlab="Time (steps)", 
		ylab="Diversity",
		axes=FALSE, 
		type="n",	
		pch=19)



axis(1, tck=0.02, las=1, cex.axis=1)
axis(2, at=seq(0, 1, 0.1), tck=0.02, las=1, cex.axis=1)

abline(h=data$reference[1], lty=2);
text(0, data$reference[1]-0.05, "Reference Diversity", pos=4, cex=.8);

lgd <- data.frame(pch=NULL, lty=NULL, name=NULL);

i <- 0;
for (run in as.vector(unique(data$strategy))) {
    i <- i + 1;
    if (i == 2) {
       i <- i+1	
    }
    lgd <- rbind(lgd, data.frame(pch=i, lty=i, name=run));
    d <- subset(data, strategy==run);
    lines(d$time, d$diversity, type="o", lty=i, pch=i);
}

box(lty=1, lwd=2);

legend("topright",	
	 legend=lgd$name,
       bty="n",
	 pch=lgd$pch,
       cex=0.8,
       lty=lgd$lty);

dev.off()