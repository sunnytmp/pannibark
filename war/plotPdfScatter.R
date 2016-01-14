args <- commandArgs(TRUE)
gpa <- read.table(args[1],h=T,fill = TRUE)

pdf(paste0(args[2],"/yourgraph.pdf"))

library(car)
scatterplot(GPA~Hours, reg.line=lm, smooth=TRUE,spread=TRUE, boxplots='xy', span=0.5, data=gpa)

dev.off()
