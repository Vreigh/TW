using DataFrames
using Gadfly

function myDraw()
	Gadfly.push_theme(:dark)
	avg = readtable("/home/filip/Desktop/TW/lab6/data.csv")
	pl = plot(avg, x=:N, y=:Avg, Guide.title("ucztujący filozofowie"), color=:Type, Geom.point, Geom.smooth)
	draw(PNG(string("chart.png"), 8inch, 4inch), pl)
end
