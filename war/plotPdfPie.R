args <- commandArgs(TRUE)
autos_data <- read.table(args[1], header=T,sep="\t",fill = TRUE) 
pdf(paste0(args[2],"/yourgraph.pdf"))

print(autos_data)
lastCol  = ncol(autos_data)

pie(autos_data[[1]], main="Data Points", col=rainbow(length(cars)),
   labels=c("DataPoint1","DataPoint2","DataPoint3","DataPoint4","DataPoint5"))

dev.off()
