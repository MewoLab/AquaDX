About placing SQL files:

File should be named `V1000_{version}__{description}.sql` where:
- `{version}` is the sequential ID, please pick the next available number
- `{description}` is a short description of the change

The folders are grouped by 40 changes each, please put the files in the correct folder according to the version number:
- `db/40` : 1-40
- `db/80` : 41-80
- etc.

(this is to avoid having to scroll through a huge list of files when looking for a specific change)
