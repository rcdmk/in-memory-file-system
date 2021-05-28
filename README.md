# In-memory Filesystem Scala Playground

An in-memory implementation of a simulated Unix shell with basic operations on an in-memory filesystem representation.

## Supported operations

- **cat** - reads file contents
- **cd** - changes current working directory
- **echo** - prints to std out, outputs to a file or appends output to a file
- **exit** - terminate app
- **ls** - lists directory contents
- **mkdir** - creates a directory
- **pwd** - prints working directory path
- **rm** - remove a file or directory (if empty)
- **touch** - creates an empty file

Only basic operations are supported at the moment. Piping is not supported.

## Executing

To execute this project locally, navigate to the root of the repository and run

```bash
$ sbt run
```

## Testing

To run the project tests, navigate to the root of the repository and run

```bash
$ sbt test
```
