# This script generates 4 main charts:
# 
#  - Actual diversity level against robustness
#
#  - Actual diversity level against cost
#
#  - The diversity drift (distribution of discrepancies between
#  expected and actual diversities
# 
#  - Correlation between robustness, diversity and cost (as a treillis)
#
#  - cost of gaining one point of robutness against gaining some diversity



results <- read.csv("results.csv");
head(results);

attach(results);

# --------
#
# We plot here the correlation between absolute cost and absolute
# diversity levels


pdf("costs.pdf", width=6, height=4);

boxplot(
    cost ~ expected.diversity,
    xlab="Actual Diversity",
    ylab="Estimated Cost"
);

dev.off();



# --------
#
# We plot here the correlation between absolute robustness and
# absolute diversity levels

pdf("robustness.pdf", width=6, height=4);
    
boxplot(    
    robustness ~ expected.diversity,
    xlab="Actual diversity",
    ylab="Estimated robustness"
);

dev.off();



# --------
#
# We plot here the distribution of the diversity drift
#

pdf("diversity_drift.pdf", width=6, height=4);

drift <- expected.diversity - actual.diversity;
hist(
    drift, 
    breaks=10, 
    xlab="Diversity drift");
 
dev.off();



# -------- 
#
# We plot here the correlation between cost, robustness and actual
# diversity

pdf("correlation.pdf", width=10, height=6);

pairs(~actual.diversity+cost+robustness)

dev.off();



# -------- 
#
# We plot here the distribution of the relationship between gain
# diversity and price of robustness

pdf("cba.pdf", width=6, height=4);

dg <- actual.diversity - initial.diversity
cg <- cost - initial.cost
rg <- robustness - initial.robustness

plot( cg / rg, dg,
      xlab="Gain in diversity", 
      ylab="Cost of 1 pt. of robustness",
      ylim=c(-1, 1)
);

abline(h = 0, lty=2);
abline(v = 0, lty=2);

dev.off();



detach(results);