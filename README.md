# DistributedFileSystem

Hello there!

This is my distributed file system project consisting of
- A Directory Service
- File Servers - includes *replication*
- Client API - includes *caching*
- A fuse client which implements the API (really cool! :D) - *transparent access*

Files and folders both work, as well as text and binary data! :D

Make sure you have a go with the FUSE filesystem!

## Directory Service

The directory service accepts two types of clients - file servers and user clients.
File servers connect to the service and publish their own key.

Clients connect to the service to request reading, writing and deleting files.

When a client requests to read a file, the file path is hashed by the directory service,
and the two closest file servers (by key) are returned. This allows for balanced replication
among file servers.

The directory service also keeps track of metadata, such as the file system layout, updated timestamps,
and servers.

## File Servers

I decided to keep the file servers really simple - the just accept requests for reading, writing and deleteing files
referenced by the file hash of the file. Kotlin makes this very easy! :D

Each server randomly generates a key, which is reported to the directory service for use in file distribution.

## Client API

The client api does most of the work. It gets metadata from the directory service, like file hash, which servers have
this file, and chooses which to contact at random. When updating a file, it uploads it to both of the responsible
servers.

The client also handles caching of files, checking for the lastest timestamp from the dir service, and checking for a
local copy before requesting it from a server.

## Redundency

Each file path is hashed, and the file contents are stored on two servers. These are the two closest servers by key.

## Persistance

As an added bonus, I save all file system data and server keys on exit, so the directory service and file servers may
be restarted and all will be fine! :D

Also, by using a connection per command, this allows for services to be restarted independent of everything else.

## FUSE Filesystem

As an example client, I've implemented a FUSE (Filesystem in Userspace) client which wraps my client API. This allows
you to mount the filesystem locally on Linux (Mac and Windows is possible with more work), so that any application can
transparently access the filesystem! :D Emacs, vim, ranger etc. all work perfectly fine!

I've only implemented a subset of the standard filesystem calls, so not everything works 100%, but could easily be
added with more time! :)

# Running

This project is written in Kotlin. To compile, you'll need the kotlin complier! Alternatively, I've uploaded the latest release as a .jar file which can be run with any Java 8 JVM.

See the latest releases here: https://github.com/cal2195/DistributedFileSystem/releases

## Directory Service

To start the directory service, run:

`java -jar DistributedFileSystem.jar dir`

The service runs on the well known port 8768.

This needs to be started first!

## File Servers

To start file servers, run:

`java -jar DistributedFileSystem.jar file [directory service ip] [my ip] [my port]`

You can start as many as these as you'd like! :)

## FUSE Client

To mount a fuse client (tested on Linux with FUSE installed), run:

`java -jar DistributedFileSystem.jar fuse [mount point path] [directory service ip]`

This will mount the file system at *mount point path*, allowing you to `cd` in and perform normal file system
operations with your shell, or any application on your system!

Any questions, just ask!

Thanks,
Cal
