# Using Component Colors:
## Overview
- Each theme can have its own color group and name that is since the app doesn't reference the theme colors directly
- Instead, it references component colors names defined in component_colors.yml
- component_colors.yml defines many componentColorNames, and for each, the theme color for each theme type

## Benefits 
- Can add new themes, be sure to add the color mapping in component_colors
- Can rename colors and add new ones, be sure to update the color maping in component_colors
- Can modify color values, without the need to update anything else

## Additional Benefits
- By having a componentColor, can easily change the colors for a certain component
- Note: Nothing enforces having a new defined component color for a new component, colors can be a componentColor easily re-used for as many colors as possible; however, if that were enforced by the developer through implementation, it makes modifying colors remotely practical.  

# Using Component Images

