args <- commandArgs(TRUE)
autos_data = read.table(args[1], header=T,sep="\t",fill = TRUE) 
# Graph autos with adjacent bars using rainbow colors

pdf(paste0(args[2],"/yourgraph.pdf"))
plot_colors <- c("blue","red","forestgreen")
max_y <- max(autos_data)
max_x <- max(autos_data)

barplot(as.matrix(autos_data) , main="Vizualization" , ylab= "Graph" , beside=TRUE, col=rainbow(NCOL(autos_data)))

dev.off()
