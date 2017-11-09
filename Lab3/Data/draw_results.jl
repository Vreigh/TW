using DataFrames
using Gadfly

function myDraw(name)
	Gadfly.push_theme(:dark)
	home = "/home/filip/Desktop/TW/Lab3/Data/"
	avg = readtable(string(home, name, ".csv"))
	pl = plot(avg, x=:Size, y=:Avg, Guide.title(name), color=:Type, Geom.smooth)
	draw(PNG(string(home, "Charts/", name, ".png"), 8inch, 4inch), pl)
end
