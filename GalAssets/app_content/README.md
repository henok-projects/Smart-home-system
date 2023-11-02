# Overview
Some views of Galsie have content that uses models which are available locally.
The reason they are locally available is to render those views without the need to need of an internet connection.

# Being part of GalAssets
Being part of galassets, app_content_model_data synchronizes its data from GCS (Galsie's Cloud servers) whenever there is internet availability. Synchronization requires versioning to either be in-file, or in the directory name.

# File versioned
app content json files are file versioned (other option would be directory versoned) through 'assets_file_version' key 
