# 2021_fall_database_systems

This is the repository of the DatabaseSystems lecture which I attended in the fall semester 2021.

These are the description and codes of the 'B+Tree' and 'DB project' that I submitted as assignments.

**ORDER**
1. B+ tree exaplanation
2. DB project explanation
3. Reflection(느낀점)

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
        ![input example](../READMEsrc/inputexample.png)
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
        ![delete example](../READMEsrc/delete_example.png)
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
          ![search example](../READMEsrc/search_example.png)
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
        ![ranged search example](../READMEsrc/ranged_search_example.png)

---

## DB project

DB 프로젝트는 1.(요구사항 분석)->2.(ER모델을 이용한 개념적 DB 설계)->3.(관계형 스키마를 이용한 논리적(logical) DB 설계)->4.(DBMS 프로그램 개발)로 진행되었습니다.

과제 설명은 4.(DBMS 프로그램 개발)에 대한 내용만 작성하였습니다.
1. 프로젝트 4는 자신이 설계한 DB를 기반으로 하는 어플리케이션 프로그램을 개발하는 것을 목표로 한다. 수강생들은 SQL DDL을 이용하여 데이터베이스 스키마를 생성한 뒤, java와 JDBC 등을 이용해 DBMS 응용 프로그램을 개발할 수 있다. 프로젝트를 진행하며 지난 프로젝트 과정에서 설계한 구조를 수정할 수 있다.

2. 환경
- os: Windows or Mac os
- language: Java(JDBC), Python(PyMySQL)
- DBMS: MySQL

3. 제약사항
- 개발하는 프로그램은 다음 기능들을 반드시 포함해야 한다.
  - insert, update, delete, select 기능
- 모든 function들은 반드시 cli(command line interface)에서 수행될 수 있어야 한다.
 ![DB project example](../READMEsrc/DBproject_example.png)
  - 단, 위 예시는 예시일 뿐이며, 본인만의 프로그램을 구성할 수 있도록 한다.

---

## Reflection(느낀점)

1. B+ tree
-자신만의 B+tree 쓰기 구조를 생각해 index 파일에 기록하고, 다음 명령어 입력 시 이를 다시 읽어 B+tree 구조를 수정 및 기록해야했다. 자신만의 indexing 구조를 고민하는 경험을 할 수 있어서 좋았다.
- 다양한 경우를 나눠서 코드를 생각하고, 코너 케이스들도 고려해야해서 시행착오가 많았다.
- 처음에 10만 개 이상의 값들을 넣었을 때 실행 시간이 매우 오래 걸렸었다. 원인을 찾는데, 처음에는 트리 구조 코드를 비효율적으로 짠 줄 알았다. 트리 코드를 수정하다가, 생각난 것이 string은 immutable class라는 것이었다. 나의 초기 코드는 index 파일을 write할 때 모든 string을 하나로 길게 만든 후에 write하는 구조였는데, 긴 string을 수정해나가는 구조이다보니 오버헤드가 너무 컸었던 것 같다. 그래서 string을 짧은 단위로 index 파일에 여러 번 write하도록 바꾸었더니 실행 시간이 매우 짧아졌다.

2. DB project
- ERdiagram 등 구상 단계에서 생각했던 많은 기능들을 마지막 프로젝트 때에는 시간에 쫓기다보니 많이 삭제해야했다. 또한, 간단하게 생각했던 기능들도 실제로 구현하려고하니 예상보다 많은 노력을 필요로했다.
- 제대로 구상하지 않고, 프로그래밍 단계에서 즉흥적으로 고쳐나가다보니 꼬이기 쉽상이었다. 처음에 구상을 제대로 해야 프로그래밍이 더 수월하겠다는 생각을 했다.
- jdbc 드라이버를 잡는 과정에서 문제가 있었다. 데스크탑과 윈도우 모두에서 문제가 발생했다. 이를 해결하기 위해 많은 시간 검색하고, 재설치, MySQL 배포 버전 변경 등 다양한 시도를 해보았지만, 정확한 원인을 찾지 못했다. 결국 cmd에서 java classpath 설정에 직접 드라이버 path를 넣어주어 해결했다. piazza(수업 질문 플랫폼)를 보니 나와 같은 문제를 겪는 학우가 있어 방법을 알려주었다. 비록 원인은 찾지 못했지만, cmd를 통한 classpath 설정방법을 배우고, 다른 학우에게도 도움을 주어 뿌듯했다.

3. 공통
- 과제들 모두 대략적인 요구사항만 나와있고, 구현 방법 등의 세부 사항은 정해져 있지 않았다. 학생 스스로가 생각해서 과제를 구현하는 방식이었다. 이를 통해 필요 기능들을 구상하고, 코드 구조를 계획하는 좋은 성장 경험을 가졌다.