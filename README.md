# DistributedFileSystem

Hello there!

This is my distributed file system project consisting of
- A Directory Service
- File Servers
- Client API
- A fuse client which implements the API (really cool! :D)

For the features, I also implemented caching and replication among file servers!

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

Any questions, just ask!

Thanks,
Cal
