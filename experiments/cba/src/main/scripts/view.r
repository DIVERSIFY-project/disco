#
# Plot the diversity w.r.t the cost and with respect to the robustness
# 
# Franck Chauvel
#

results <- read.csv("results.csv");
head(results);

pdf("costs.pdf", width=6, height=4);


boxplot(
    results$cost ~ results$expected.diversity,
    xlab="Actual Diversity",
    ylab="Estimated Cost"
);

dev.off();

pdf("robustness.pdf", width=6, height=4);
    
boxplot(    
    results$robustnes ~ results$expected.diversity,
    xlab="Actual diversity",
    ylab="Estimated robustness"
);

dev.off();


pdf("diversity_drift.pdf", width=6, height=4);

drift <- results$expected.diversity-results$actual.diversity;
hist(
    drift, 
    breaks=10, 
    xlab="Diversity drift");
 
dev.off();
