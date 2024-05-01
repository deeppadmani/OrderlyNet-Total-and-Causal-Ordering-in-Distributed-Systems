# Distributed Data Management System

This Java project provides a network receiver class (`Distributed Data Management System`) for reading messages from a socket in a separate thread.

## Overview

The **Distributed Data Management System (DDMS)** is a robust platform designed to facilitate efficient management and coordination of data across distributed networks. Leveraging a decentralized architecture, DDMS enables seamless communication and collaboration between multiple data servers and clients.
## Key Features
- **Decentralized Architecture:** DDMS eliminates single points of failure and enables fault tolerance through its decentralized architecture.
- **Data Replication and Redundancy:** The platform employs data replication techniques to ensure redundancy and availability, enhancing data reliability and resilience against failures.
- **Dynamic Object Placement:** Objects are intelligently placed across servers using a hash function, optimizing load distribution and access times for clients.
- **Concurrent Write Support:** DDMS supports concurrent writes to the same object, ensuring consistency across replicas and preserving the order of updates.
- **Fault Tolerance Mechanisms:** The system incorporates fault tolerance mechanisms to handle network partitions and disruptions gracefully, maintaining data consistency upon network recovery.
- **Scalability and Performance:** DDMS scales effortlessly to meet growing data and user demands, providing reliable data storage and access mechanisms with high performance.

## Getting Started

**To start using DDMS, follow these steps:**
1. **Clone the Repository**: Clone this repository to your local machine.

    ```
    git clone https://github.com/deeppadmani/OrderlyNet-Total-and-Causal-Ordering-in-Distributed-Systems.git
    cd AOS_Project3/src
    ```
2. **Update the `NetworkConfig.conf`**: 
   Update the `TotalServerNodes` ,`TotalClientNodes` ,`ServerNetworkNodes` ,`ClientNetworkNodes`
   ```
   TotalServerNodes = n

   TotalClientNodes = m

   ServerNetworkNodes = [IP] [PORT],[IP] [PORT],...
   ClientNetworkNodes = [IP] [PORT],[IP] [PORT],...
   ```

2. **Compile the Code**: Compile the Java files using `javac`.

    ```
    javac -cp "../lib/json-simple-1.1.1.jar:." *.java
    ```

3. **Run the Application**: Run your Java application as a `Server`, ensuring that the appropriate classpath settings are configured.

    ```
    java -cp "../lib/json-simple-1.1.1.jar:." MainApplication Server [NODEID]
    ```
    **Run your Java application** as a `Client`, ensuring that the appropriate classpath settings are configured.
    ```
    java -cp "../lib/json-simple-1.1.1.jar:." MainApplication Client [NODEID]
    ```

## Dependencies

- This project requires Java SE Development Kit (JDK) installed on your system.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
