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


par(mar = c(5, 5, 1, 1), mgp = c(4, 1, 0))

setAlpha <- function(my.colors) {
    return(paste(my.colors, sep="", "55"));
}

results <- read.csv(file="results.csv"); 

weight <- results$diversity.before
impact <- abs((results$diversity.after - results$diversity.before));
delta <- abs((results$target.specie - results$source.specie) / results$individual.count) ;

#Create the colours scale
breaks <- seq(min(weight), max(weight), length.out=8);
slices <- cut(weight, breaks=breaks, include.lowest=TRUE);
my.colors <- colorRampPalette(c("#0000FF", "#FF0000"))(length(breaks));

plot(   impact ~ delta,
        col= setAlpha(my.colors[slices]), 
        pch=19,
        cex=1.5,
        ann=FALSE,
        las=1);
        
mtext(side = 1, text = "delta between source and target species", line = 3)
mtext(side = 2, text = "diversity impact", line = 4)

        
legend("bottomright", title="Initial Diversity", col=setAlpha(my.colors[seq(1:length(breaks))]), legend=levels(slices), pch=19, cex=1);

dev.off();
