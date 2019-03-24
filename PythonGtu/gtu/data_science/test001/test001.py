import numpy as np
from bokeh.plotting import figure, output_file, show
x = np.linspace(0, 5, 50)
y_cos = np.cos(x)
output_file("cosine.html")
p = figure()
p.line(x, y_cos, line_width=2)
show(p)