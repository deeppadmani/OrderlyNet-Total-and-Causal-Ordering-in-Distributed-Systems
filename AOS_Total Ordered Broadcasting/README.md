# Steps to Run This Project
Make sure that the file is compiled on each of the 4 machines


## Before Compilation
change the NetworkConfig.conf update TotalNodes & NetworkNodes as
```NetworkConfig.conf
TotalNodes = no. of node which user gone a use
NetworkNodes = ipaddr port,ipaddr port,ipaddr port,ipaddr port
Sequencer = Sequencer NodeID 
```

## Compilation

```bash
> javac MainApplication.java
```
## Run an Application
```bash
> java MainApplication [nodeId]
```
Example
```bash
> java MainApplication 0
> java MainApplication 1
> java MainApplication 2
> java MainApplication 3
```
