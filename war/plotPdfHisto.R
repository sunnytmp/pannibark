args <- commandArgs(TRUE)
autos_data <- read.table(args[1], header=F,sep="\t",fill = TRUE) 
pdf(paste0(args[2],"/yourgraph.pdf"))

lastCol  = ncol(autos_data)
autos <- ""

autos <- as.vector(autos_data)

print (autos)
# Compute the largest y value used in the autos
#max_num <- max(autos_data)

# Create uneven breaks
brk=ncol(autos_data)
col = "blue"
main="Vizualisation Of Data"
xlab="Data"
ylab="Frequency"
fra1=TRUE

autos <- c(autos$V1,autos$V2,autos$V3)
hist(autos,col="blue",breaks=3,xlim=c(0,max(autos)),xlab="Data Points",main="Aggregation",las=1,freq=T,right=T)
#hist(autos, col=heat.colors(length(brk)),xlim=c(0,100),  breaks=12, right=F, main="Probability Density",las=1, cex.axis=0.8, freq=F)

dev.off()
