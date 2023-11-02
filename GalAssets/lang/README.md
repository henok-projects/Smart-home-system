# Each language must:
- Have lang_config.yml
- Have formatting.yml
- Have feature_data_rep.yml
- Have as many files suffixed _translations as needed

# Config.yml:
- Have languageName, localeFallbackOrder
- direction: 0 for left to right, 1 for light to left
- localeFallbackOrder: the first element in the list is the final fallback. Note that locale names must match standard

- NOTE: The langCode is the name of the directory: must match the standard language code as this would result in it directly matching what would be native in ios and android


# formatting.yml
- Date format, currency format, ...

# feature_data_rep.yml
- Definitions of how feature data are represented. It belongs to a lang since it may vary from lang to lang

# \_translations.yml suffixed files
- Contains Definitions of translations through keys that define a translation name, and value that defines the translation daa
- The value of a translation must be a list that defines for each locale a translation
 - NOTE: a translation can ommit definitions of certain locales, so long as fallback locale  translations are defined (fallback locale translations are translations provided that are of a lower index in the localeFallbackOrder list in Config.yml)

IMPORTANT:
- The seperation of translations.yml to multiple files that end with _translations.yml is for the sake of convenience
- The different files can't have overlapping keys, if they do, errors will be shown.
- developers would load the all _translations.yml files into one object that allows easy access over a key in any of the files without referencing the file name (accessing a key needn't reference the file name because all the _translations.yml files can be considered as one)

VERY IMP:
- Lists in yml are the final level for which a translation is expected to be found. They must only be used for locale translations:
    '- us: blabla
     - gb: ..'
- If the list showed a translation without the locale, it uses it for any locale

# Versions
- Each yml file has a version for the sake of synchronizing updated versions on the back-end
