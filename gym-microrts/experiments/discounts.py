import math
import torch

def exponential(r, d, k):
    return r * math.exp(-k*d)

def hyperbola(r, d, k, s):
    return r/math.pow(1+k*d, s)

def exponential_d(r, d, k):
    return r * math.exp(-k*d)

def hyperbola_d(r, d, k, s):
    return r/torch.pow(1+k*d, s)