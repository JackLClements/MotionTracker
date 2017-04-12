# MotionTracker

## Overview  
Android Wear application that tracks user motion and formats it directly into a WEKA compatible .arff file.

## Requirements  
An android phone and android wear device, supporting API level 23(?) or higher.

## To Do  
* Physics processing to determine how sensor data is written - In progress
* Decide on whether to normalise data - In progress
* Data link to phone to write to file - Complete, bugs if you open the phone app first. Likely related to connection blocking on API, make sure to check.
* File layout(s) for WEKA integration
* Settings menu on phone-side
* Potential cloud integration
