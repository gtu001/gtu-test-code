import math
from scipy.stats import norm


def pnorm(val):
    norm.cdf(val) 
    
def qnorm(val):
    norm.ppf(val) 


'''
    Students across the country average 7.02 hours of sleep per night 
        according to the National Sleep Foundation
    In a poll of 202 students at our university the average hours of sleep 
        per night was 6.90 hours with a standard deviation of 0.84 hours.
    Our alternative hypothesis is the average sleep of students at our university
         is below the national average for college students.
    We will use an alpha value of 0.05 which means the results are significant 
        f the p-value is below 0.05.
        
原文在此        
    https://towardsdatascience.com/statistical-significance-hypothesis-testing-the-normal-curve-and-p-values-93274fa32687
'''
    


# Calculate the results
z_score = (6.90 - 7.02) / (0.84 / math.sqrt(202)) 
p_value = pnorm(z_score)

# Print our results
print('The p-value is %0:5f for a z-score of %0.5f.', p_value, z_score)

# "The p-value is 0.02116 for a z-score of -2.03038."