args <- commandArgs(TRUE)
#autos_data = read.table(args,header=T,sep="\t")

autos_data = file(args[1],open="r")

#Now reading each line from the file. Gives tab separated columns.
linn=readLines(autos_data)

#Reading for R
autos_data_4R = read.table(args[1], header=T,sep="\t")

max_y <- max(autos_data_4R)
max_x <- max(autos_data_4R)
plot_colors <- c("blue","red","forestgreen")

pdf(paste0(args[2],"/yourgraph.pdf"))



lastCol  = ncol(autos_data_4R)
plot(autos_data_4R[[lastCol]], type="l", col=plot_colors[1],
 ylim=c(0,max_y),axes=TRUE, ann=TRUE)

for (i in 1:lastCol) {

  lines(autos_data_4R[[i]], type="l", pch=23-i, lty=i+3,
    col=plot_colors[i])

}

# Make y axis with horizontal labels that display ticks at 
# every 4 marks. 4*0:max_y is equivalent to c(0,4,8,12).
#axis(2, las=1, at=4*0:max_y)

# Create box around plot
box()


# Create a title with a red, bold/italic font
title(main="Data Representation", col.main="red", font.main=4)

# Label the x and y axes with dark green text
#title(main="Data Points")
title(ylab= "Aggregate Like",)

# Create a legend at (1, max_y) that is slightly smaller 
# (cex) and uses the same line colors and points used by 
# the actual plots
legend(1, max_y, names(autos_data_4R), cex=0.8, col=plot_colors, 
  pch=21:23, lty=1:5);
   
# Turn off device driver (to flush output to png)
dev.off()

#Column Count
























