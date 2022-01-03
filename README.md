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
  - Keys and values 
4. Constraints - Internal Structure
5. Constraints - Interface
