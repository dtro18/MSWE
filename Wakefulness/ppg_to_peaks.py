import pandas as pd
import numpy as np
from scipy.signal import find_peaks
from scipy.ndimage import gaussian_filter1d


def extract_csv_data_pandas(file_path):
    df = pd.read_csv(file_path)
    return df

# def detect_peaks(df):
#     peaks = []
#     for i in range(1, len(df) - 1):
#         if (df['ppg'].iloc[i] > df['ppg'].iloc[i-1] and 
#             df['ppg'].iloc[i] > df['ppg'].iloc[i+1]):
#             peaks.append({
#                 'time': df['time'].iloc[i],
#                 'ppg': df['ppg'].iloc[i]
#             })
#     print(len(peaks))
#     return pd.DataFrame(peaks)

file_path = 'C:\ml shit\Wakefulness\ppg_sample.csv'

df = extract_csv_data_pandas(file_path)
# Convert pandas series into np array
time = df["time"].values
ppg = df["ppg"].values

smoothed_data = gaussian_filter1d(ppg, sigma=2)
peaks, properties = find_peaks(smoothed_data, height=500, prominence=50)

print("Peaks found at indices:", peaks)
for peak_idx in peaks:
    print("Time: " + str(time[peak_idx])) 
    print("Value: " + str(ppg[peak_idx])) 

# print(peaks)

# Accessing specific columns
# Possible approaches to plateaus - delete duplicate values