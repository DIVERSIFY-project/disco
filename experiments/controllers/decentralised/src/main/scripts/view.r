pdf("decentralized.pdf", heigh=8, width=6);

view_pop <- function(pData, n, title) {
     r <- max(pData$run)
     step <- subset(pData, step == n & run == r);
     plot(step$x, step$y,  main=title, xlab="x", ylab="y", type="n", las=1, frame=FALSE);
     points(step$x, step$y, col=step$specie + 2, pch=16);
}

pData <- read.csv("population.csv");
view_pop(pData, min(pData$step), "Initial Population");
view_pop(pData, max(pData$step), "Final Population");



dData <- read.csv("diversity.csv");
dd <- aggregate(error ~ step, dData, quantile);
plot(dd$step, dd[,2][,3], main="Tracking Error Evolution", xlab="Simulation steps", ylab="Tracking Error", type="l", ylim=c(0,1), las=1, lty=1, lwd=2, frame=FALSE);
lines(dd$step, dd[,2][,1], lty=2);
lines(dd$step, dd[,2][,5], lty=2);
lines(dd$step, dd[,2][,2], lty=1, lwd=1);
lines(dd$step, dd[,2][,4], lty=1, lwd=1);
legend("topright", legend=c("median", "min and max", "2nd and 4th quartile"), lty=c(1, 2, 1), lwd=c(2, 1, 1), bty="n") 

dev.off();