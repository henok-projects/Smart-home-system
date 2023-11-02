"""
Created by Liwaa Al Kontar

Property of Galsie Inc.
"""

import os
import re
MAIN_DIR = "brand/theme"
VERSIONED_DIRECTORIES = [
    '/.*', # light, dark
    '/.*/.*', # categories, logo, colors, appicon...
    '/.*/categories/.*', # lighting, security, ...
    '/.*/categories/.*/nodes/.*', # every node in the category
    '/.*/categories/diverse/.*', # every diverse group
    '/.*/categories/.*/nodes/.*/(one|many)', # one and many if nodes have them
    '/.*/categories/.*/nodes/.*/(group|single)', # group and single without one/many
    '/.*/categories/.*/nodes/.*/(one|many)/(group|single)', # one and many if nodes have them
]

def main():
    for directories_regex in VERSIONED_DIRECTORIES:
        version_directory(MAIN_DIR + directories_regex)

def version_directory(directories_regex):
    subdirs_regex = directories_regex.split('/')
    if len(subdirs_regex) == 0:
        return []
    subdirs_matched = ["./"]
    for subdir_regex in subdirs_regex:
        subdirs_matched = [os.path.join(subdir_matched, subdir) for subdir_matched in subdirs_matched for subdir in os.listdir(subdir_matched) if re.match( "%s%s" % (subdir_regex,"(@\d+\.\d+)?" if subdir_regex != ".*" else ""), subdir) and os.path.isdir(os.path.join(subdir_matched, subdir))]
    ## the matched subdirectories are ones to be versioned.
    ## If they already have a version, reset it to 0.0
    # print(subdirs_matched)
    for old_path in subdirs_matched:
        curr_name = old_path.split("/")[-1]
        if re.match(".+(@\d+\.\d+)?", curr_name):
            new_name = re.sub("@\d+\.\d+", "", curr_name) + "@0.0"
            new_path = os.path.join("/".join(old_path.split("/")[:-1]), new_name)
            os.rename(old_path, new_path)
    #print(subdirs_matched)
if __name__ == '__main__':
    main()
    print("Donne")
