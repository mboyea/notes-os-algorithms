# Project 3
## Java Compilation Steps
1.) Ensure version 19 or newer [Java SDK](https://www.oracle.com/java/technologies/downloads/) is installed and referencable by PATH  
2.) Open a terminal in the project directory.  
3.) Build & run the project with the following commands.  
Build: `javac App.java`  
Run: `java App`  
Build & Run: `javac App.java && java App`  

## Project objective
Implement and experiment with the Banker's algorithm presented in the section titled Dynamic deadlock avoidance.
## Description
The Banker's algorithm uses a claim graph consisting of processes, multi-unit resources, request edges, allocation edges, and claim edges.
The graph can be represented by a set of arrays:
•	The number of units of each resource is represented as a one-dimensional array R[m], where m is the number of resources and each entry R[j] records the number of units of resource Rⱼ .
•	The maximum claims are represented as a two-dimensional array P[n][m] where each entry P[i][j] contains an integer that records the maximum number of units of resource Rⱼ that process pᵢ will ever request.
•	The allocation edges and the request edges are represented using analogous arrays.
## Assignment
1.	Develop an interactive program that first reads the description of a system from the command line or from a file. The description consists of the number of processes, the number of resources, the numbers of units within each resource, and the maximum claims of each process.
2.	Using the given information, the program creates the current representation of the system (the set of arrays).
3.	The program then enters an interactive session during which the user inputs commands of the form:
        request(i, j, k) or release(i, j, k),
where i is a process number, j is a resource number, and k is the number of units of Rⱼ the process pᵢ is requesting or releasing.
For each request operation, the program responds whether the request has been granted or denied.
4.	Demonstrate the functioning of the banker's algorithm by inputting a sequence of requests and releases that, without using the Banker's algorithm, would lead to a deadlock.
