// songjaeheon 2021_fall_DatabaseSystems( )_lecture_bplustree_implementation

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Bptree {
    // main 함수를 통해 arguments를 분석하고 필요한 함수를 실행합니다.(insertion, deletion, search, rangedSearch)
    public static void main(String[] args) {

        String str = "";
        // 현 위치를 파악하여 파일 i/o를 진행하기 위한 부분입니다.
        String filePath = new File("").getAbsolutePath();

        switch (args[0]) {
            // 인자로 주어진 수를 최대 children 수로 갖는 b+ tree구조를 주어진 index 파일에 write합니다.
            case "-c": {
                String indexPath = filePath + "/" + args[1];
                File file = new File(indexPath);
                PrintWriter outputStream = null;
                try {
                    outputStream = new PrintWriter(new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int size = Integer.parseInt(args[2]);
                outputStream.println(size);
                outputStream.close();
                break;
            }
            // 주어진 index 파일을 읽어 b+ tree를 생성하고, input 파일에 주어진 key, value의 pair들을 추가한 후에 다시 기록합니다.
            // isExists로 유무를 확인하고 insert합니다.
            case "-i": {
                String indexPath = filePath + "/" + args[1];
                String inputPath = filePath + "/" + args[2];
                BufferedReader buffer = null;
                try {
                    Bptree tree = null;
                    tree = new Bptree().readIndex(indexPath);

                    buffer = Files.newBufferedReader(Paths.get(inputPath));
                    int a, b;
                    while ((str = buffer.readLine()) != null) {
                        String[] strArr = null;
                        strArr = str.split(",");
                        a = Integer.parseInt(strArr[0].trim());
                        b = Integer.parseInt(strArr[1].trim());
                        if (!(tree.isExists(a))) {
                            tree.root = tree.insert(a, b);
                        }
                    }
                    new Bptree().writeIndex(tree, indexPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (buffer != null) {
                            buffer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            // 주어진 index 파일을 읽어 b+ tree를 생성하고, delete 파일에 주어진 key에 해당하는  node들을 삭제한 후에 다시 기록합니다.
            // isExists로 유무를 확인하고 delete합니다.
            case "-d": {
                String indexPath = filePath + "/" + args[1];
                String inputPath = filePath + "/" + args[2];
                BufferedReader buffer = null;
                try {
                    Bptree tree = null;
                    tree = new Bptree().readIndex(indexPath);

                    buffer = Files.newBufferedReader(Paths.get(inputPath));
                    int a;
                    while ((str = buffer.readLine()) != null) {
                        a = Integer.parseInt(str.trim());
                        if (tree.isExists(a)) {
                            tree.root = tree.delete(a);
                        }
                    }
                    new Bptree().writeIndex(tree, indexPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (buffer != null) {
                            buffer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            // 한 key를 검색합니다.
            case "-s": {
                String indexPath = filePath + "/" + args[1];
                int key = Integer.parseInt(args[2]);

                Bptree tree = null;
                tree = new Bptree().readIndex(indexPath);
                tree.search(key);
                break;
            }
            // 주어진 범위의 key들을 검색합니다.
            case "-r": {
                String indexPath = filePath + "/" + args[1];
                int startKey = Integer.parseInt(args[2]), endKey = Integer.parseInt(args[3]);

                Bptree tree = new Bptree();
                tree = new Bptree().readIndex(indexPath);
                tree.rangeSearch(startKey, endKey);
                break;
            }
            default:
                System.out.println("Input is not the designated arguments format");
                break;
        }
    }

    // index파일 구조는 'index_file_structure_instruction.txt'에 적어두었습니다.
    public Bptree readIndex(String indexPath){
        Bptree tree = null;
        String str = null;
        String[] strArr = null;
        String[] intList = null;
        BufferedReader buffer = null;
        try{
            buffer = Files.newBufferedReader(Paths.get(indexPath));
            str = buffer.readLine();
            tree = new Bptree();
            tree.setDegree(Integer.parseInt(str.trim()));
            ArrayList<Integer> nodeSizeList = new ArrayList<Integer>();
            ArrayList<Node> nodeList = new ArrayList<Node>();
            while((str = buffer.readLine()) != null){
                strArr = str.split("\t");
                // 형식에 맞춰 Leaf, Internal을 구분합니다.
                char ch = strArr[0].charAt(0);
                if(ch == '\\') {         // leaf node
                    for(int i = 0; i < strArr.length; i++){
                        strArr[i] = strArr[i].replace("\\", "");
                    }
                    for(int i = 0; i < strArr.length; i++) {
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        intList = strArr[i].split(",");
                        for (int j = 0; j < intList.length; j++) {
                            list.add(Integer.parseInt(intList[j].trim()));
                        }
                        LeafNode newNode = new LeafNode(getDegree());
                        newNode.size = list.get(0);
                        if(newNode.size == 0){
                            break;
                        }
                        // Node를 만들어 nodelist에 넣습니다.
                        nodeSizeList.add(list.get(0));
                        for (int j = 1; j < list.size(); j++) {
                            if (j <= list.get(0)) {
                                newNode.arr[j - 1] = list.get(j);
                            } else {
                                newNode.arr2[j - 1 - list.get(0)] = list.get(j);
                            }
                        }
                        nodeList.add(newNode);
                    }
                }else{              // internal node
                    for(int i = 0; i < strArr.length; i++){
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        intList = strArr[i].split(",");
                        for(int j = 0; j < intList.length; j++){
                            list.add(Integer.parseInt(intList[j].trim()));
                        }
                        InternalNode newNode = new InternalNode(getDegree());
                        newNode.size = list.get(0);
                        nodeSizeList.add(list.get(0));
                        for(int j = 1; j < list.size(); j++){
                            newNode.arr[j-1] = list.get(j);
                        }
                        nodeList.add(newNode);
                    }
                }
            }
            // 아래 같은 방식으로 node들을 연결합니다.
            // tmp와 tmp2 변수를 이용해 LeafNode들이 연속되어 있을 때
            // siblingNode 변수로 오른쪽 노드로 포인팅 해놓습니다.
            if(nodeList.size() > 0){
                tree.root = nodeList.get(0);
                Node tmpRoot = tree.root;
                LeafNode tmp = new LeafNode();
                LeafNode tmp2 = new LeafNode();
                int i = 0, j = 1;
                while(j < nodeList.size()){
                    for(int k = 0; k <= nodeSizeList.get(i); k++){
                        tmpRoot.nArr[k] = nodeList.get(j);
                        if(tmpRoot.nArr[k].getClass() == tmpLN.getClass()){
                            tmp = tmp2;
                            tmp2 = (LeafNode) tmpRoot.nArr[k];
                            if(tmp != null){
                                tmp.siblingNode = tmp2;
                            }
                        }
                        j++;
                    }
                    i++;
                    tmpRoot = nodeList.get(i);
                }
            }else{
                tree.root = new LeafNode(getDegree());
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tree;
    }
    // index 파일을 구성하는 방식으로 tree를 기록합니다.
    // queue2는 각 height의 가장 왼쪽 InternalNode를 넣어둬,
    // queue에서 노드를 꺼낼 때 확인해 bplustree의 height가 바뀜을 파악합니다.
    public void writeIndex(Bptree tree, String indexPath){
        BufferedWriter buffer = null;
        try {
            buffer = Files.newBufferedWriter(Paths.get(indexPath));
            buffer.write(tree.degree + newLine);
            BFS(buffer, tree.root);
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void BFS(BufferedWriter buffer, Node node){
        String str = "";
        Node tmp = null;
        Queue<Node> queue = new LinkedList<>();
        Queue<Node> queue2 = new LinkedList<>();
        queue.add(node);
        if(queue.peek().getClass() == tmpIN.getClass()){
            queue2.add(node.nArr[0]);
        }
        while(queue.peek() != null){
            tmp = queue.poll();
            if(tmp.getClass() == tmpLN.getClass()){
                str = str + "\\" + tmp.size + ",";
                for(int i = 0; i < tmp.size; i++){
                    str = str + tmp.arr[i] + ",";
                }
                for(int i = 0; i < tmp.size; i++){
                    str = str + tmp.arr2[i] + ",";
                }
            }else{
                str = str + tmp.size + ",";
                for(int i = 0; i < tmp.size; i++){
                    str = str + tmp.arr[i] + ",";
                    queue.add(tmp.nArr[i]);
                }
                queue.add(tmp.nArr[tmp.size]);
            }
            str = str + "\t";
            if((queue.peek() != null) && (queue2.peek() != null) && (queue.peek() == queue2.peek())){
                str = str + newLine ;
                if(queue2.peek().getClass() == tmpIN.getClass()){
                    queue2.add(queue2.peek().nArr[0]);
                    queue2.remove();
                }
            }
            try{
                buffer.write(str);
                str = "";
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static int degree;
    static String newLine = System.getProperty("line.separator");

    Bptree() {};
    public void setDegree(int degree){
        this.degree = degree;
    }
    public static int getDegree(){
        return degree;
    }

    // Bptree의 variable에 tmpIn, tmpLN을 두어 .getClass()에 활용합니다.
    public static Node root;
    public static Node parent = null;
    public InternalNode tmpIN = new InternalNode();
    public LeafNode tmpLN = new LeafNode();

    public Node insert(int key, int value) { return root.insertVal(key, value); }
    public Node delete(int key) { return root.deleteVal(key); }
    public void search(int key){ root.searchKey(key); }
    public void rangeSearch(int startKey, int endKey){
        root.rangeSearchKey(startKey, endKey);
    }
    public boolean isExists(int key){ return this.root.isExists(key); };

    // abstract class인 Node입니다.
    // 공통 variable들과 @Override할 함수들이 있습니다.
    abstract class Node{
        int degree = 0;
        int size = 0;

        int[] arr = null, arr2 = null;
        Node[] nArr = null;


        abstract Node insertVal(int key, int value);
        abstract Node deleteVal(int key);
        abstract void searchKey(int key);
        abstract void rangeSearchKey(int startKey, int endKey);
        abstract boolean isExists(int key);
        abstract boolean isOverflow();
        abstract boolean isUnderflow();
        abstract boolean isLendable();
        abstract boolean isMergeable();
    }

    class InternalNode extends Node{
//        int[] arr;
//        Node[] nArr;

        InternalNode(){ };

        // InternalNode의 생성자입니다. degree+1로 배열을 생성해, overflow에 대비합니다.
        InternalNode(int degree){
            arr = new int[degree+1];
            nArr = new Node[degree+1];
            this.degree = degree;
            this.size = 0;
        }

        // InternalNode의 insert입니다.
        // parent 변수는 child에서 overflow가 발생 시 부모 node로 올라오는 key입니다.
        // 재귀적으로 호출해, 현재에서 overflow가 연쇄 발생하면 다시 부모 node에서 해결합니다.
        @Override
        Node insertVal(int key, int value){
            parent = null;
            int loc = findLoc(key);
            nArr[loc].insertVal(key, value);
            if(parent != null){
                this.nArr[this.size+1] = this.nArr[this.size];
                for(int i = this.size; i > loc; i--){
                    this.arr[i] = this.arr[i-1];
                    this.nArr[i] = this.nArr[i-1];
                }
                this.size++;
                this.arr[loc] = parent.arr[0];
                this.nArr[loc] = parent.nArr[0];
                this.nArr[loc+1] = parent.nArr[1];
                parent = null;
                if(this.isOverflow()) {
                    int newSize = (int) Math.ceil(this.degree/2.0);
                    InternalNode newSibling = new InternalNode(getDegree());
                    newSibling.nArr[degree-newSize] = this.nArr[degree];
                    for(int i = 0; i < (degree-newSize); i++){
                        newSibling.arr[i] = this.arr[newSize+i];
                        newSibling.nArr[i] = this.nArr[newSize+i];
                    }
                    newSibling.size = this.degree - newSize;
                    this.size = newSize-1;
                    parent = new InternalNode(getDegree());
                    parent.size = 1;
                    parent.arr[0] = this.arr[this.size];
                    parent.nArr[0] = this;
                    parent.nArr[1] = newSibling;
                    return parent;
                }
            }
            return this;
        }

        // InternalNode의 delete입니다.
        // flag 변수는 삭제하는 key 값이 internalNode에 있을 경우 이를 기록합니다.
        // child로 삭제를 재귀적으로 호출하고, child에서 underflow발생 시 이를 8경우로 해결합니다.
        // child의 LeafNode여부, borrow의 좌우 경우, merge의 좌우 경우로 8가지 경우 입니다.
        // flag는 마지막에 getRightChildLeftmost함수를 호출해 올바른 key 값으로 수정합니다.
        @Override
        Node deleteVal(int key){
            boolean flag = false;
            int i;
            for(i = 0; i < this.size; i++){
                if(key == this.arr[i]){
                    flag = true;
                }
                if(key >= this.arr[i]){
                    continue;
                }else{
                    break;
                }
            }
            Node tmpChild = nArr[i];
            tmpChild.deleteVal(key);
            if(tmpChild.isUnderflow()){
                Node tmpChildLeftSibling = null, tmpChildRightSibling = null;
                if(i > 0){
                    tmpChildLeftSibling = this.nArr[i-1];
                }
                if(i <= this.size-1){
                    tmpChildRightSibling = this.nArr[i+1];
                }
                if(tmpChild.getClass() == tmpLN.getClass()){
                    if((tmpChildLeftSibling != null) && (tmpChildLeftSibling.isLendable())){
                        for (int j = tmpChild.size; j > 0; j--){
                            tmpChild.arr[j] = tmpChild.arr[j-1];
                            tmpChild.arr2[j] = tmpChild.arr2[j-1];
                        }
                        tmpChild.size++;
                        tmpChild.arr[0] = tmpChildLeftSibling.arr[tmpChildLeftSibling.size-1];
                        tmpChild.arr2[0] = tmpChildLeftSibling.arr2[tmpChildLeftSibling.size-1];
                        tmpChildLeftSibling.size--;
                        this.arr[i-1] = tmpChildLeftSibling.arr[tmpChildLeftSibling.size];
                    }else if((tmpChildRightSibling != null) && (tmpChildRightSibling.isLendable())){
                        tmpChild.arr[tmpChild.size] = tmpChildRightSibling.arr[0];
                        tmpChild.arr2[tmpChild.size] = tmpChildRightSibling.arr2[0];
                        tmpChild.size++;
                        for (int j = 0; j < tmpChildRightSibling.size-1; j++){
                            tmpChildRightSibling.arr[j] = tmpChildRightSibling.arr[j+1];
                            tmpChildRightSibling.arr2[j] = tmpChildRightSibling.arr2[j+1];
                        }
                        tmpChildRightSibling.size--;
                        this.arr[i] = tmpChildRightSibling.arr[0];
                    }else if((tmpChildLeftSibling != null) && (tmpChildLeftSibling.isMergeable())){
                        flag = false;
                        for (int j = 0; j < tmpChild.size; j++){
                            tmpChildLeftSibling.arr[tmpChildLeftSibling.size] = tmpChild.arr[j];
                            tmpChildLeftSibling.arr2[tmpChildLeftSibling.size] = tmpChild.arr2[j];
                            tmpChildLeftSibling.size++;
                        }
                        for (int j = i - 1; j < (this.size-1); j++){
                            this.arr[j] = this.arr[j+1];
                            this.nArr[j+1] = this.nArr[j+2];
                        }
                        this.size--;
                    }else{      // else if(tmpChildRightSibling.isMergeable()) { 와 같습니다.
                        flag = false;
                        for(int j = 0; j < tmpChildRightSibling.size; j++){
                            tmpChild.arr[tmpChild.size] = tmpChildRightSibling.arr[j];
                            tmpChild.arr2[tmpChild.size] = tmpChildRightSibling.arr2[j];
                            tmpChild.size++;
                        }
                        for(int j = i; j < (this.size-1); j++){
                            this.arr[j] = this.arr[j+1];
                            this.nArr[j+1] = this.nArr[j+2];
                        }
                        this.size--;
                    }
                }else{      // Root가 아닌 InternalNode
                    if((tmpChildLeftSibling != null) && (tmpChildLeftSibling.isLendable())){
                        for (int j = tmpChild.size; j > 0; j--){
                            tmpChild.arr[j] = tmpChild.arr[j-1];
                            tmpChild.nArr[j+1] = tmpChild.nArr[j];
                        }
                        tmpChild.nArr[1] = tmpChild.nArr[0];
                        tmpChild.arr[0] = this.arr[i-1];
                        tmpChild.nArr[0] = tmpChildLeftSibling.nArr[tmpChildLeftSibling.size];
                        this.arr[i-1] = tmpChildLeftSibling.arr[tmpChildLeftSibling.size-1];
                        tmpChild.size++;
                        tmpChildLeftSibling.size--;
                    }else if((tmpChildRightSibling != null) && (tmpChildRightSibling.isLendable())){
                        tmpChild.arr[tmpChild.size] = this.arr[i];
                        tmpChild.nArr[tmpChild.size+1] = tmpChildRightSibling.nArr[0];
                        this.arr[i] = tmpChildRightSibling.arr[0];
                        tmpChild.size++;
                        for (int j = 0; j < (tmpChildRightSibling.size-1); j++){
                            tmpChildRightSibling.arr[j] = tmpChildRightSibling.arr[j+1];
                            tmpChildRightSibling.nArr[j] = tmpChildRightSibling.nArr[j+1];
                        }
                        tmpChildRightSibling.nArr[tmpChildRightSibling.size-1] = tmpChildRightSibling.nArr[tmpChildRightSibling.size];
                        tmpChildRightSibling.size--;
                    }else if((tmpChildLeftSibling != null) && (tmpChildLeftSibling.isMergeable())) {
                        flag = false;
                        tmpChildLeftSibling.arr[tmpChildLeftSibling.size] = this.arr[i - 1];
                        tmpChildLeftSibling.size++;
                        for (int j = 0; j < tmpChild.size; j++) {
                            tmpChildLeftSibling.arr[tmpChildLeftSibling.size] = tmpChild.arr[j];
                            tmpChildLeftSibling.nArr[tmpChildLeftSibling.size] = tmpChild.nArr[j];
                            tmpChildLeftSibling.size++;
                        }
                        tmpChildLeftSibling.nArr[tmpChildLeftSibling.size] = tmpChild.nArr[tmpChild.size];
                        for (int j = i - 1; j < (this.size - 1); j++) {
                            this.arr[j] = this.arr[j + 1];
                            this.nArr[j + 1] = this.nArr[j + 2];
                        }
                        this.size--;
                    }else{      // else if(tmpChildRightSibling.isMergeable()) { 와 같습니다.
                        flag = false;
                        tmpChild.arr[tmpChild.size] = this.arr[i];
                        tmpChild.size++;
                        for(int j = 0; j < tmpChildRightSibling.size; j++){
                            tmpChild.arr[tmpChild.size] = tmpChildRightSibling.arr[j];
                            tmpChild.nArr[tmpChild.size] = tmpChildRightSibling.nArr[j];
                            tmpChild.size++;
                        }
                        tmpChild.nArr[tmpChild.size] = tmpChildRightSibling.nArr[tmpChildRightSibling.size];
                        for(int j = i; j < (this.size-1); j++){
                            this.arr[j] = this.arr[j + 1];
                            this.nArr[j + 1] = this.nArr[j + 2];
                        }
                        this.size--;
                    }
                }
            }
            if((this.size == 0) && (this.getClass() == tmpIN.getClass())){     // collapse
                root = root.nArr[0];
            }else if(flag){
                if(this.getClass()==tmpIN.getClass()){
                    this.arr[i-1] = this.getRightChildLeftmost(i-1);
                }
            }
            return root;
        }

        // 재귀적으로 search를 호출합니다.
        @Override
        void searchKey(int key){
            int loc = findLocSearchKey(key);
            this.nArr[loc].searchKey(key);
        }

        @Override
        void rangeSearchKey(int startKey, int endKey){
            int loc = findLoc(startKey);
            this.nArr[loc].rangeSearchKey(startKey, endKey);
        }

        // key의 존재여부를 확인하는 isExists 함수입니다. 재귀적으로 호출해 존재 여부를 확인합니다.
        // while문으로 overhead를 줄이려고 했습니다.
        @Override
        boolean isExists(int key){
            int i = 0;
            Node tmp = this;
            while(tmp.getClass() == tmpIN.getClass()) {
                for (i = 0; i < tmp.size; i++) {
                    if(key == tmp.arr[i]){
                        return true;
                    }else if (key > tmp.arr[i]) {
                        continue;
                    }else{
                        break;
                    }
                }
                tmp = tmp.nArr[i];
            }
            return tmp.isExists(key);
        }

        // InternalNode에서 overflow, underflow, lendable(borrow해줄 수 있는지), mergeable등을 판단합니다.
        @Override
        boolean isOverflow() {
            if(this.size == this.degree){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isUnderflow(){
            if(this.size < (int)(this.degree-1)/2){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isLendable(){
            if(this.size > (int)(this.degree-1)/2){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isMergeable(){
            if(this.size <= (int)(this.degree-1)/2){
                return true;
            }else{
                return false;
            }
        }

        // tree 안에서 올바른 key 위치를 찾는데 활용됩니다.
        // insert, search, rangedSearch 등에 활용됩니다.
        int findLoc(int key){
            for(int i = 0; i < size; i++){
                if(key >= this.arr[i]){
                    continue;
                }else{
                    return i;
                }
            }
            return size;
        }

        int findLocSearchKey(int key){
            System.out.print(this.arr[0]);
            if(key < this.arr[0]){
                System.out.println();
                return 0;
            }
            int i;
            for(i = 1; i < size; i++){
                System.out.print("," + this.arr[i]);
                if(key < this.arr[i]){
                    break;
                }
            }
            System.out.println();
            return i;
        }

        // InternalNode의 key에 삭제한 key가 있을 경우 올바른 key를 찾습니다.
        int getRightChildLeftmost(int idx){
            Node tmp = null;
            tmp = this.nArr[idx+1];
            while(tmp.getClass() != tmpLN.getClass()){
                tmp = tmp.nArr[0];
            }
            return tmp.arr[0];
        }
    }

    // LeafNode는 variable siblingNode를 가져 오른쪽 LeafNode로의 포인터를 가집니다.
    class LeafNode extends Node{
        LeafNode siblingNode;      // a pointer to the right sibling node.

        LeafNode(){ };

        LeafNode(int degree){
            arr = new int[degree+1];  // for keys
            arr2 = new int[degree+1];  // for values
            this.degree = degree;
            size = 0;
            siblingNode = null;
        }

        // LeafNode의 insert입니다.
        // overflow 발생 시 부모 node로 parent node를 올려 부모 node에서 처리합니다.
        @Override
        Node insertVal(int key, int value){
            size++;
            int i;
            for(i = 0; i < size-1; i++){
                if(key > arr[i]) continue;
                else break;
            }
            if(i == size-1){
                arr[i] = key;
                arr2[i] = value;
            }else{
                for(int j = size; j > i; j--){
                    arr[j] = arr[j-1];
                    arr2[j] = arr2[j-1];
                }
                arr[i] = key;
                arr2[i] = value;
            }
            if(this.isOverflow()){   // 오버플로우 발생하는 경우
                int newSize = this.degree/2;
                LeafNode newSibling = new LeafNode(getDegree());
                for(i = 0; i < (degree-newSize); i++){
                    newSibling.arr[i] = this.arr[newSize+i];
                    newSibling.arr2[i] = this.arr2[newSize+i];
                }
                newSibling.size = this.size - newSize;
                this.size = newSize;
                newSibling.siblingNode = this.siblingNode;
                this.siblingNode = newSibling;
                // 가운데 노드를 부모 노드로 올려줘야함
                parent = new InternalNode(getDegree());
                parent.size = 1;
                parent.arr[0] = newSibling.arr[0];
                parent.nArr[0] = this;
                parent.nArr[1] = newSibling;
                return parent;
            }
            return this;
        }

        // LeafNode의 deletion입니다.
        @Override
        Node deleteVal(int key){
            int i;
            for(i = 0; i < this.size; i++){
                if(key == this.arr[i])
                    break;
            }
            for(int j = i; j < this.size-1; j++){
                this.arr[j] = this.arr[j+1];
                this.arr2[j] = this.arr2[j+1];
            }
            this.size--;
            return this;
        }

        // LeafNode의 search와 rangedSearch입니다.
        // rangedSearch에서 LeafNode의 siblingNode가 활용됩니다.
        @Override
        void searchKey(int key){
            for(int i = 0; i < size; i++){
                if(key == this.arr[i]){
                    System.out.println(arr2[i]);
                    return;
                }
            }
            System.out.println("NOT FOUND");
        }

        @Override
        void rangeSearchKey(int startKey, int endKey){
            boolean flag = true;
            int i;
            for(i = 0; i < size; i++){
                if(startKey > this.arr[i]){
                    continue;
                }else{
                    flag = false;
                    LeafNode tmp = this;
                    while(tmp.arr[i] <= endKey){
                        System.out.println(tmp.arr[i] + "," + tmp.arr2[i]);
                        i++;
                        if(i == tmp.size){
                            if(tmp.siblingNode != null){
                                tmp = tmp.siblingNode;
                                i = 0;
                            }
                        }
                    }
                    return;
                }
            }
            if(flag){
                if(this.siblingNode != null){
                    LeafNode tmp = this.siblingNode;
                    i = 0;
                    while(tmp.arr[i] <= endKey){
                        System.out.println(tmp.arr[i] + "," + tmp.arr2[i]);
                        i++;
                        if(i == tmp.size){
                            if(tmp.siblingNode != null){
                                tmp = tmp.siblingNode;
                                i = 0;
                            }
                        }
                    }
                }
            }
        }

        // key의 존재여부를 확인하는 isExists 함수와
        // LeafNode에서 overflow, underflow, lendable(borrow해줄 수 있는지), mergeable등을 판단합니다.
        @Override
        boolean isExists(int key) {
            for(int i = 0; i < this.size; i++){
                if(key == this.arr[i]){
                    return true;
                }else{
                    continue;
                }
            }
            return false;
        }

        @Override
        boolean isOverflow() {
            if(this.size == this.degree){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isUnderflow(){
            if(this.size < (int)(this.degree/2)){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isLendable(){
            if(this.size > (int)(this.degree/2)){
                return true;
            }else{
                return false;
            }
        }

        @Override
        boolean isMergeable(){
            if(this.size <= (int)(this.degree/2)){
                return true;
            }else{
                return false;
            }
        }
    }
}