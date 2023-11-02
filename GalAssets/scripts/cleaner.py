"""
Cleaner script:
- Ensures no directory name has a space (having a space causes loading errors on the back-end server)
- Removes versioning from all directories

The script to be followed after this is reset versioning
"""
import os
import re
RESET_PATH = "brand/theme"

def reset(path):
    for dir in os.listdir(path):
        foundPath = os.path.join(path, dir)
        if not os.path.isdir(foundPath):
            continue
        newPath = re.sub("@\d+\.\d+", "", foundPath) # remove version
        newPath = re.sub("(\s+)", "_", newPath) # replace space with _
        os.rename(foundPath, newPath)
        #if " " in foundPath:
        print("Renamed", foundPath, "to", newPath)
        reset(newPath)

reset(RESET_PATH)