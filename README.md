# 2021_fall_database_systems

This is the repository of the DatabaseSystems lecture which I attended in the fall semester 2021.

These are the description and codes of the 'B+Tree' and 'DB project' that I submitted as assignments.

---

## B+ tree implementation assignment

1. Goal
- Implementation of a B+ tree index

2. Environment
- os: Windows or Mac os
- Language: Java or Python
  - C++ language is also allowed, but not recommended
3. Constraints - Overall
- The B+ tree index should be stored in a single file(index file)
- The file contains all the meta information for the index and also the index nodes
- **The Internal organization of the file is not considered in grading**
- The program shoudl provide following functions:
  - Search
    - A single key search and a range search
  - Insertion of a key
  - Deletion of a key
    - The deleted entry should be completely removed form the index and the file
- Assumption
  - Keys and values are all in the integer type
  - **Duplicated keys are not allowed for insertions**
  - The keys in a node are sotred in an **ASCENDING order**
- POLICY
  - Do not copy
  - Do not use libraries that implementing the similar architecture with B+ tree
4. Constraints - Internal Structure
- Each node of a B+ tree index should contain the following data inside:
  - Non-leaf node
    - m: # of keys
    - p: an array of <key, left_child_node> pairs
    - r: a pointer to the rightmost child node
  - Leaf node
    - m: # of keys
    - p: an array of <key, value(or pointer to the value)> pairs
    - r: a pointer to the right sibling node
5. Constraints - Interface
- The program should support command-line interface
- The following commands should be implemented:
  - **Data File Creation**
    - Command: *program -c index_file b*
      - *program*: name of the program
      - *index_file*: name of a new index file
      - *b*: size of each node(max # of child nodes)
    - This command creates a new index file containing an empty index with the node size *b*
      - If the file already exists, it is overwitten
    - Example
      - java bptree -c index.dat 8
  - **Insertion**
    - Command: *program -i index_file data_file*
      - *data_file*: name of the input data file that has a number of key-vale pairs to be inserted
    - This command inserts all the key-value pairs inside the data_file into the index in the index_file
      - The insertion causes the modification of the index file
      - Insertions are performed in the same order of key-value pairs in the data flie
    - The data file is provided as a .csv file
      - Each line of the data file contains a key-value pair
        - <key>, <value>\n
      - Data file example (input.csv)
        ![input example](url)
    - Example
      - java bptree -d index.dat delete.csv
  - **Deletion**
    - Command: *program -d index_file dat_file*
      - *data_file*: name of the input data file that has a number of keys to be deleted
    - This command deletes all the key-value pairs inside the input data file from the index
      - The deletion causes the modification of the index file
      - Deletions are performed in the same order of keys in the data file
    - The input data file is provided as a .csv file
      - Each line of the data file contains only a key value
        - <key>\n
      - Deletion file example (delete.csv)
        ![delete example](url)
  - **Single Key Search**
    - Command: *program -s indexl_file key*
      - *key*: key value to be searched
    - This command returns a value of a pointer to a record with the key
    - Output format
      - Print output to the stdout
      - While searching, the program prints each non-leaf node in the path that the search passes through
        - Print all the keys in the node in a single line
        - <key1>, <key2>, ..., <keyN>\n
      - When the search reaches the leaf node having the search key, print the value matched with the search key
        - <value>\n
        - If not found, print 'NOT FOUND'
      - Example (This is not the same dataset in above example)
        - java bptree -s index.dat 125
          ![search example](url)
  - **Ranged Search**
    - Command: *program -r index_file start_key end_key*
      - *start_key*: lower bound of the ranged search
      - *end_key*: upper bound of the ranged search
    - This program returns the values of pointers to records having the keys within the range provided
    - Output format
      - Print ouput to the *stdout*
      - Print all the key-value pairs with the key between *start_key* and *end_key* (**including start_key and end_key**)
        - <key1>,<value1>\n<key2>,<value2>\n...
      - Note that *start_key* and *end_key* may not be in the index
        - The program prints only the key-value pairs between them
    - Example
      - java bptree -r index.dat 100 200
        ![ranged search example](url)