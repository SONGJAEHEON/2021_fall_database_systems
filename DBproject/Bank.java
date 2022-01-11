// songjaeheon 2021_fall_DatabaseSystems( )_lecture_DBproject4

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Bank {
    public static void main (String[] args) {
        // Loading JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Obtaining a connection
        Connection conn = null;
        try {
            //"jdbc:mariadb://localhost:3306/test"
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true", "root", "123456");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        //Querying
        Display dp = new Display();
        int hIdx = 1; // History index
        int idx, tmp, tmp2;
        double tmpdbl;
        int input, input1;
        double inputdbl;
        String tmpSsn, tmpPw;
        String inputStr, inputStr1, inputStr2, inputStr3, inputStr4;
        String sql;
        Scanner sc = null;
        Statement stmt = null, stmt1 = null, stmt2 = null, stmt3 = null;
        ResultSet rs = null;
        // executeQuery(): return value is needed like SELECT.
        // executeUpdate(): return value is not needed like INSERT, UPDATE, DELETE.

        try {
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery("SELECT foo FROM bar");
//
//            if (stmt.execute("SELECT foo FROM bar")) {
//                rs = stmt.getResultSet();
//            }
            sc = new Scanner(System.in);
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            stmt2 = conn.createStatement();
            stmt3 = conn.createStatement();

            // Hindex는 History 테이블의 Primary Key를 구성하는 attribute 중 하나입니다. DB 가동 시에 가장 높은 계좌 기록 인덱스를 찾아 그 다음 넘버링을 저장합니다.
            rs = stmt.executeQuery("select Hindex from history");
            while(rs.next())
            {
                if(hIdx <= rs.getInt("Hindex"))
                    hIdx = rs.getInt("Hindex")+1;
            }

            Start:
            while(true)
            {
                dp.showStart();
                input = sc.nextInt();
                InitialSwitch:
                switch (input)
                {
                    case 0:
                        break Start;
                    case 1:
                        // 관리자: 관리자 로그인을 통해 최고관리자/일반관리자로 나뉩니다.
                        dp.showManagerLogIn();
                        System.out.print("0 or Ssn: ");
                        input = sc.nextInt();
                        if(input == 0)
                            continue Start;
                        else
                        {
                            // Manager 테이블에서 Mssn, Password, Position attribute를 가져옵니다.
                            rs = stmt.executeQuery("select Mssn, Password, Position from Manager");
                            while (rs.next())
                            {
                                tmpSsn = rs.getString("Mssn");
                                if (input == Integer.parseInt(tmpSsn))
                                {
                                    System.out.print("Password: ");
                                    inputStr = sc.next();
                                    tmpPw = rs.getString("Password");
                                    if (inputStr.equals(tmpPw))
                                    {
                                        // Chief or not
                                        // 최고관리자: Position 0으로 1명만 존재합니다.
                                        if (rs.getInt("Position") == 0)
                                        {       // Chief
                                            ChiefManager:
                                            while(true)
                                            {
                                                dp.showChiefManagerMenu();
                                                input = sc.nextInt();
                                                switch(input)
                                                {
                                                    case 0:
                                                        System.out.println("Go to Initial Menu");
                                                        continue Start;
                                                    case 1:
                                                        // 관리자 생성: Position1의 일반관리자들을 생성합니다.
                                                        System.out.println();
                                                        System.out.println("Create Manager");
                                                        System.out.print("Set the new manager's ssn: ");
                                                        inputStr = sc.next();
                                                        System.out.println("Set the new manager's name: ");
                                                        inputStr1 = sc.next();
                                                        // position will be 1(not chief).
                                                        System.out.println("Set the new manager's password: ");
                                                        inputStr2 = sc.next();
                                                        // 필요한 value들을 넣어 새 Manager tuple을 생성합니다.
                                                        sql = String.format("insert into manager values ('%s', '%s', 1, '%s') ", inputStr, inputStr1, inputStr2);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("New Manager Has Been Added!");
                                                        continue ChiefManager;
                                                    case 2:
                                                        // 관리자 정보 변경: 관리자의 정보를 변경합니다.
                                                        System.out.println();
                                                        System.out.println("Modify Manager");
                                                        System.out.print("Insert the manager's ssn: ");
                                                        inputStr = sc.next();
                                                        System.out.println("Set the manager's new name: ");
                                                        inputStr1 = sc.next();
                                                        // position cannot be changed.
                                                        System.out.println("Set the manager's new password: ");
                                                        inputStr2 = sc.next();
                                                        // 필요한 value들을 넣어 Manager tuple을 수정합니다.
                                                        sql = String.format("update manager set Mname = '%s', Password = '%s' where Mssn = '%s'", inputStr1, inputStr2, inputStr);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("Manager's Information Has Been Updated!");
                                                        continue ChiefManager;
                                                    case 3:
                                                        // 관리자 삭제: 관리자를 삭제합니다.
                                                        System.out.println();
                                                        System.out.println("Delete Manager");
                                                        System.out.println("Insert the deleting manager's ssn: ");
                                                        inputStr = sc.next();
                                                        System.out.println("Insert the alternating manager's ssn: ");
                                                        inputStr1 = sc.next();
                                                        // 관리자를 삭제하려고 합니다. 담당 고객들의 관리자 정보를 업데이트하기 위해 Manager, Customer 테이블을 equijoin해 정보를 가져옵니다.
                                                        sql = String.format("select Cssn from customer, manager where Cmssn = Mssn");
                                                        rs = stmt.executeQuery(sql);
                                                        while(rs.next())
                                                        {
                                                            // Referential Constraint 위배 방지를 위해, 삭제할 Manager의 Customer들의 Manager를 다른 Manager로 변경합니다.
                                                            sql = String.format("update customer set Cmssn = '%s' where Cmssn = '%s'", inputStr1, inputStr);
                                                            stmt1.executeUpdate(sql);
                                                        }
                                                        // 삭제하고자하는 Manager tuple을 DB에서 삭제합니다.
                                                        sql = String.format("delete from manager where Mssn = '%s'", inputStr);
                                                        stmt.executeUpdate(sql);
                                                        System.out.printf("Manager '%s' Has Been Deleted!%n", inputStr);
                                                        continue ChiefManager;
                                                    case 4:
                                                        // 고객 생성: 고객을 생성합니다.
                                                        System.out.println();
                                                        System.out.println("New Customer Sign Up");
                                                        System.out.print("Set the customer's ssn: ");
                                                        inputStr = sc.next();
                                                        System.out.println("Insert the customer's ID: ");
                                                        inputStr1 = sc.next();
                                                        System.out.println("Insert the customer's name: ");
                                                        inputStr2 = sc.next();
                                                        System.out.println("Insert the customer's phone number: ");
                                                        inputStr3 = sc.next();
                                                        // credit will be 500
                                                        System.out.println("Set the customer's manager's ssn: ");
                                                        inputStr4 = sc.next();
                                                        // 필요한 value들을 넣어 Customer tuple을 생성합니다. Default Credit은 500 입니다.
                                                        sql = String.format("insert into customer values ('%s', '%s', '%s', '%s', DEFAULT, '%s')", inputStr, inputStr1, inputStr2, inputStr3, inputStr4);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("New Customer Has Been Added!");
                                                        continue ChiefManager;
                                                    case 5:
                                                        // 은행 상품 생성: 은행 상품을 생성합니다.
                                                        System.out.println();
                                                        System.out.println("Make Financial Instruments");
                                                        System.out.print("Insert the new instrument's code: ");
                                                        inputStr = sc.next();
                                                        System.out.println("Insert the new instrument's category: ");
                                                        input = sc.nextInt();
                                                        System.out.println("Insert the new instrument's credit standard: ");
                                                        input1 = sc.nextInt();
                                                        System.out.println("Insert the new instrument's interest: ");
                                                        inputdbl = sc.nextDouble();
                                                        // 필요한 value들을 넣어 Financial Instruments tuple을 생성합니다.
                                                        sql = String.format("insert into financialinstruments values ('%s', '%s', '%s', '%s') ", inputStr, input, input1, inputdbl);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("New Financial Instrument Has Been Added!");
                                                        continue ChiefManager;
                                                    case 6:
                                                        // 은행 상품 삭제: 은행 상품을 삭제합니다. (DeadInstruments 테이블에 등록함으로써 신규 가입은 막고, 기존 상품 보유 고객은 그대로 이용할 수 있습니다.)
                                                        System.out.println();
                                                        System.out.println("Delete Financial Instruments");
                                                        System.out.print("Insert the instrument's code: ");
                                                        inputStr = sc.next();
                                                        // 삭제한 Financial Instrument의 Primary Key인 Instrumentscode를 Dead Instruments 테이블에 추가합니다.
                                                        sql = String.format("insert into deadinstruments values ('%s')", inputStr);
                                                        stmt.executeUpdate(sql);
                                                        System.out.printf("Financial Instrument %s Has Been Deleted!%n", inputStr);
                                                        break;
                                                    case 7:
                                                        // 관리자 나열: 관리자 정보를 나열합니다.
                                                        System.out.println();
                                                        System.out.println("List Managers");
                                                        // 관리자들 나열해 출력하려고 합니다. Manager 테이블에서 Position, 이름에 따라 정렬해 정보를 가져옵니다.
                                                        rs = stmt.executeQuery("select Mssn, Mname, Position from Manager order by Position asc, Mname asc");
                                                        idx = 1;
                                                        System.out.println("Index Ssn    Name Position");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            System.out.printf("%5s %10s %d%n", rs.getString("Mssn"), rs.getString("Mname"), rs.getInt("Position") );
                                                            idx++;
                                                        }
                                                        continue ChiefManager;
                                                    case 8:
                                                        // 고객 나열: 고객 정보를 나열합니다.
                                                        System.out.println();
                                                        System.out.println("List Customers");
                                                        // 전체 고객들을 나열해 출력하려고 합니다. Manager와 담당하는 Customer들의 정보를 equijoin을 통해 연결하고, Credit 내림차순으로 정렬해 정보를 가져옵니다.
                                                        rs = stmt.executeQuery("select Cssn, Cname, Cphonenum, Credit, Mssn, Mname from Manager join Customer on manager.Mssn = customer.Cmssn order by Credit desc ");
                                                        idx = 1;
                                                        System.out.println("Index Ssn    Name Phonenumber Credit Mng_ssn Mng_name");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            System.out.printf("%5s %10s %11s %d %5s %10s%n", rs.getString("Cssn"), rs.getString("Cname"), rs.getString("Cphonenum"), rs.getInt("Credit"), rs.getString("Mssn"), rs.getString("Mname") );
                                                            idx++;
                                                        }
                                                        continue ChiefManager;
                                                    case 9:
                                                        // 은행 상품 나열: 은행 상품 정보를 나열합니다.
                                                        System.out.println();
                                                        System.out.println("List Financial Instruments");
                                                        // 은행 상품들을 출력하려고 합니다. (Not In)을 통해 삭제되어 Dead Instruments에 등록된 Financial Instruments들을 제외합니다. Category(자유입출금/대출)와 가입가능 신용점수 기준으로 정렬합니다.
                                                        rs = stmt.executeQuery("select Instrumentscode, Category, Standard, Interest from financialinstruments where Instrumentscode NOT IN (select Dinstrumentscode from deadinstruments where Dinstrumentscode = Instrumentscode) order by Category asc, Standard desc");
                                                        idx = 1;
                                                        System.out.println("Index InstrumentsCode Category Standard Interest");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            System.out.printf("%3s ", rs.getString("Instrumentscode"));
                                                            dp.showCategory(rs.getInt("Category"));
                                                            System.out.printf("%d %4.3f%n", rs.getInt("Standard"), rs.getDouble("Interest"));
                                                            idx++;
                                                        }
                                                        System.out.println("List Dead Instruments");
                                                        // 삭제되어 Dead Instruments에 등록된 은행 상품들도 따로 출력하려고 합니다. DeadInstruments 테이블의 tuple들을 정렬해 가져옵니다.
                                                        rs = stmt.executeQuery("select Instrumentscode, Category, Standard, Interest from financialinstruments join deadinstruments where Instrumentscode = Dinstrumentscode order by Category asc, Standard desc");
                                                        idx = 1;
                                                        System.out.println("Index InstrumentsCode Category Standard Interest");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            System.out.printf("   %3s    ", rs.getString("Instrumentscode"));
                                                            dp.showCategory(rs.getInt("Category"));
                                                            System.out.printf(" %d    %4.3f%n", rs.getInt("Standard"), rs.getDouble("Interest"));
                                                            idx++;
                                                        }
                                                        continue ChiefManager;
                                                    case 10:
                                                        // 계좌 내역 관리: 최고 관리자의 고객의 계좌기록을 보고, 삭제할 수 있습니다.
                                                        System.out.println();
                                                        System.out.println("Manage Account History");
                                                        // 매니저가 담당하는 고객들의 계좌 정보를 출력하려고 합니다. Account, Customer, FinancialInstruments 테이블에서 정보를 equijoin합니다.
                                                        sql = String.format("select Category, Accountnumber, Cssn, Cmssn from account, customer, financialinstruments where Ainstrumentscode = Instrumentscode AND Acssn = Cssn AND Cmssn = '%s' order by Cssn asc", tmpSsn);
                                                        rs = stmt.executeQuery(sql);
                                                        idx = 1;
                                                        System.out.println("Index Category Customer AccountNumber Manager");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            dp.showCategory(rs.getInt("Category"));
                                                            System.out.printf("%5s %13s %5s%n", rs.getString("Cssn"), rs.getString("Accountnumber"), rs.getString("Cmssn") );
                                                            idx++;
                                                        }
                                                        System.out.print("Which account would you like to manage?(put 0 to cancel): ");
                                                        inputStr = sc.next();
                                                        if(inputStr.equals("0"))
                                                            continue ChiefManager;
                                                        // 계좌기록을 출력하려고 합니다. History 테이블에서 계좌기록 정보를 가져와 Hindex 오름차순으로 정렬합니다.
                                                        sql = String.format("select * from history where Haccountnumber = '%s' order by Hindex asc", inputStr);
                                                        rs = stmt.executeQuery(sql);
                                                        System.out.println("HistoryIndex AccountNumber ValueChange");
                                                        while(rs.next())
                                                        {
                                                            System.out.printf("%13s %5s%n", rs.getInt("Hindex"), rs.getString("Haccountnumber"), rs.getInt("Hvaluechange") );
                                                        }
                                                        System.out.print("Which index would you remove?: ");
                                                        input = sc.nextInt();
                                                        // 제거한 계좌의 계좌기록을 History 테이블에서 삭제합니다.
                                                        sql = String.format("delete from history where Hindex = '%s'", input);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("History Has Been Removed!");
                                                        continue ChiefManager;
                                                    default:
                                                        // none.
                                                        break;
                                                }
                                            }
                                        }
                                        else        // not chief manager
                                        {
                                            tmp = Integer.parseInt(rs.getString("Mssn"));
                                            tmp2 = Integer.parseInt(rs.getString("Position"));
                                            Manager:
                                            while(true)
                                            {
                                                dp.showManagerMenu(tmp, tmp2);
                                                input1 = sc.nextInt();
                                                switch(input1)
                                                {
                                                    case 0:
                                                        System.out.println("Go to Initial Menu");
                                                        continue Start;
                                                    case 1:
                                                        // 계좌 내역 관리: 해당 관리자의 고객의 계좌기록을 보고, 삭제할 수 있습니다.
                                                        System.out.println();
                                                        System.out.println("Manage Account History");
                                                        // 매니저가 담당하는 고객들의 계좌 정보를 출력하려고 합니다. Account, Customer, FinancialInstruments 테이블에서 정보를 equijoin합니다.
                                                        sql = String.format("select Category, Accountnumber, Cssn, Cmssn from account, customer, financialinstruments where Ainstrumentscode = Instrumentscode AND Acssn = Cssn AND Cmssn = '%s' order by Cssn asc", tmpSsn);
                                                        rs = stmt.executeQuery(sql);
                                                        idx = 1;
                                                        System.out.println("Index Category Customer AccountNumber Manager");
                                                        while(rs.next())
                                                        {
                                                            System.out.print(idx + " ");
                                                            dp.showCategory(rs.getInt("Category"));
                                                            System.out.printf("    %5s %13s     %5s%n", rs.getString("Cssn"), rs.getString("Accountnumber"), rs.getString("Cmssn") );
                                                            idx++;
                                                        }
                                                        System.out.print("Which account would you like to manage?(put 0 to cancel): ");
                                                        inputStr = sc.next();
                                                        if(inputStr.equals("0"))
                                                            continue Manager;
                                                        // 계좌기록을 출력하려고 합니다. History 테이블에서 계좌기록 정보를 가져와 Hindex 오름차순으로 정렬합니다.
                                                        sql = String.format("select * from history where Haccountnumber = '%s' order by Hindex asc", inputStr);
                                                        rs = stmt.executeQuery(sql);
                                                        System.out.println("HistoryIndex AccountNumber ValueChange");
                                                        while(rs.next())
                                                        {
                                                            System.out.printf("%13s %5s%n", rs.getInt("Hindex"), rs.getString("Haccountnumber"), rs.getInt("Hvaluechange") );
                                                        }
                                                        System.out.print("Which index would you remove?(0 to cancel): ");
                                                        input = sc.nextInt();
                                                        if(input == 0)
                                                            continue Manager;
                                                        // 제거하고자하는 계좌기록을 History 테이블에서 삭제합니다.
                                                        sql = String.format("delete from history where Hindex = '%s'", input);
                                                        stmt.executeUpdate(sql);
                                                        System.out.println("History Has Been Removed!");
                                                        continue Manager;
                                                    default:
                                                        System.out.println("Wrong Command!");
                                                        continue Manager;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    case 2:
                        dp.showCustomerLogIn();
                        System.out.print("0 or Ssn: ");
                        inputStr = sc.next();
                        if(inputStr.equals("0"))
                            continue Start;
                        Customer:
                        while(true)
                        {
                            // Customer 테이블에서 Ssn 정보를 가져옵니다.
                            rs = stmt.executeQuery("select Cssn from Customer");
                            while (rs.next()) {
                                tmpSsn = rs.getString("Cssn");
                                if (inputStr.equals(tmpSsn))
                                {
                                    dp.showCustomerMenu();
                                    System.out.print("Choice: ");
                                    input = sc.nextInt();
                                    switch(input)
                                    {
                                        case 0:
                                            System.out.println();
                                            System.out.println("Go to Initial Menu");
                                            continue Start;
                                        case 1:
                                            // 정보 변경: 고객의 정보를 변경합니다.
                                            System.out.println();
                                            System.out.println("Modify Information");
                                            System.out.println("Insert the customer's new name: ");
                                            inputStr1 = sc.next();
                                            System.out.println("Insert the customer's new phone number: ");
                                            inputStr2 = sc.next();
                                            System.out.println("Insert the customer's new manager's ssn: ");
                                            inputStr3 = sc.next();
                                            // Customer의 정보를 업데이트합니다.
                                            sql = String.format("update customer set Cname = '%s', Cphonenum = '%s', Cmssn = '%s' where Cssn = '%s'", inputStr1, inputStr2, inputStr3, inputStr);
                                            stmt.executeUpdate(sql);
                                            System.out.println("Customer's Information Has Been Modified!");
                                            continue Customer;
                                        case 2:
                                            // 계좌 생성: 계좌를 만듭니다. 계좌의 최대 개수는 (신용점수/100)개로 제한됩니다. 제한 조건에 해당하지 않으면, 상품 정보를 나열하고, 중복되지 않는 계좌번호로 계좌를 생성합니다.
                                            System.out.println("");
                                            System.out.println("Create Account");
                                            // 계좌를 만들고자 합니다. Account, Customer 테이블을 equijoin하여 계좌 갯수, 신용점수 정보를 가져옵니다. 계좌 갯수 제한 조건을 확인하기 위해, count문을 통해 고객의 계좌 갯수를 셉니다.
                                            sql = String.format("select count(Accountnumber) as totalCount, Credit from account join customer on Acssn = Cssn where Cssn = '%s' ", inputStr);
                                            rs = stmt.executeQuery(sql);
                                            rs.next();
                                            tmp = rs.getInt("totalCount");
                                            tmp2 = rs.getInt("Credit");
                                            if(tmp >= tmp2/100)
                                            {
                                                System.out.println("You Have Too Many Accounts!");
                                                continue Customer;
                                            }
                                            dp.showCreateAccountMenu();
                                            System.out.print("Choice: ");
                                            input = sc.nextInt();
                                            switch(input)
                                            {
                                                case 0:
                                                    continue Start;
                                                case 1:
                                                    continue Customer;
                                                case 2:
                                                case 3:
                                                    // 삭제되지 않은 은행 상품을 출력하려고 합니다. FinancialInstruments 테이블에서 Not In 구문을 통해 DeadInstruments 테이블에 존재하는 은행 상품은 제외하고 정보를 가져옵니다.
                                                    sql = String.format("select Instrumentscode, Category, Standard, Interest from financialinstruments where Instrumentscode NOT IN (select Dinstrumentscode from deadinstruments where Dinstrumentscode = Instrumentscode) order by Category asc, Standard desc");
                                                    rs = stmt.executeQuery(sql);
                                                    idx = 1;
                                                    System.out.println("Index InstrumentsCode Category Standard Interest");
                                                    while(rs.next())
                                                    {
                                                        System.out.print(idx + " ");
                                                        System.out.printf("   %3s ", rs.getString("Instrumentscode"));
                                                        dp.showCategory(rs.getInt("Category"));
                                                        System.out.printf("   %d    %4.3f%n", rs.getInt("Standard"), rs.getDouble("Interest"));
                                                        idx++;
                                                    }
                                                    System.out.print("Choose InstrumentsCode Choice: ");
                                                    inputStr1 = sc.next();
                                                    // 고객의 신용점수가 만들고자하는 은행상품의 신용점수기준 이상인지 확인하기 위해 FinancialInstruments 테이블에서 해당 은행상품의 신용점수기준 attribute를 가져옵니다.
                                                    sql = String.format("select Standard from financialinstruments where Instrumentscode = '%s'", inputStr1);
                                                    rs = stmt.executeQuery(sql);
                                                    rs.next();
                                                    tmp = rs.getInt("Standard");
                                                    if(tmp > tmp2)
                                                    {
                                                        System.out.println("The Standard Is Above Your Credit!");
                                                        continue Customer;
                                                    }
                                                    SetAcntNum:
                                                    while(true)
                                                    {
                                                        System.out.print("Set Account Number: ");
                                                        inputStr2 = sc.next();
                                                        // 계좌번호 중복을 피하기 위해, Account 테이블에 존재하는 계좌들의 계좌번호 정보들을 가져옵니다.
                                                        rs = stmt1.executeQuery("select Accountnumber from account");
                                                        while(rs.next())
                                                        {
                                                            if(inputStr2.equals(rs.getString("Accountnumber")))
                                                            {
                                                                System.out.println("Existing Account Number!");
                                                                continue SetAcntNum;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    System.out.print("Set the new account's password (4 Numbers / ex.1234): ");
                                                    inputStr3 = sc.next();
                                                    // 필요한 value들을 넣어 새로운 Account 테이블의 tuple을 생성합니다.
                                                    sql = String.format("insert into account values ('%s', '%s', '%s', '%s', '0')",inputStr1, inputStr2, inputStr3, inputStr);
                                                    stmt.executeUpdate(sql);
                                                    System.out.println("Account Created!");
                                                    continue Customer;
                                                default:
                                                    System.out.println("Wrong Command!");
                                                    continue Customer;
                                            }
                                        case 3:
                                            // 계좌 나열: 고객 자신의 계좌들의 정보를 나열합니다.
                                            System.out.println("");
                                            System.out.println("List My Account");
                                            // 고객의 계좌정보를 출력하려고 합니다. Customer, Account, FinancialInstruments 테이블을 equijoin해 고객의 계좌정보들을 가져옵니다.
                                            sql = String.format("select Category, Accountnumber, Cmssn, Value, Interest from account, financialinstruments, customer where account.Ainstrumentscode = financialinstruments.Instrumentscode AND account.Acssn = customer.Cssn AND Acssn = '%s'", inputStr);
                                            rs = stmt.executeQuery(sql);
                                            idx = 1;
                                            System.out.println("Index Category AccountNumber Value Interest Manager");
                                            while(rs.next())
                                            {
                                                System.out.print(idx + " ");
                                                dp.showCategory(rs.getInt("Category"));
                                                System.out.printf("%13s %10d %4.3f %5s%n", rs.getString("Accountnumber"), rs.getInt("Value"), rs.getDouble("Interest"), rs.getString("Cmssn") );
                                                idx++;
                                            }
                                            continue Customer;
                                        case 4:
                                            // 입금: 계좌에 돈을 입금합니다. 계좌번호 입력 후 자신의 계좌인지 확인합니다.
                                            System.out.println("");
                                            System.out.println("Save");
                                            System.out.print("Choose The Account: ");
                                            inputStr1 = sc.next();
                                            // 입력한 계좌번호가 고객의 계좌인지 확인하기 위해 count문을 통해 고객의 계좌 중 해당 계좌번호를 가진 tuple이 있는지 확인합니다. cnt 값이 0이면 해당 계좌번호를 가진 고객의 계좌가 존재하지 않습니다.
                                            sql = String.format("select count(*) as cnt from account join customer on Acssn = Cssn where Accountnumber = '%s' AND Cssn = '%s'", inputStr1, inputStr);
                                            rs = stmt.executeQuery(sql);
                                            rs.next();
                                            if(rs.getInt("cnt") == 0)
                                            {
                                                System.out.println("Not Your Account!");
                                                continue Customer;
                                            }
                                            System.out.print("How Much Money Would You Save?: ");
                                            input = sc.nextInt();
                                            // 고객의 계좌 잔고에 입금한 금액을 업데이트합니다.
                                            sql = String.format("update Account set Value = Value +'%s' where Accountnumber = '%s'", input, inputStr1);
                                            stmt.executeUpdate(sql);
                                            // History 테이블에 입금 기록 tuple을 추가합니다.
                                            sql = String.format("insert into history values ('%s', '%s', '%s') ", inputStr1, hIdx++, input);
                                            stmt.executeUpdate(sql);
                                            System.out.println("Saving Is Done");
                                            continue Customer;
                                        case 5:
                                            // 출금: 계좌에서 돈을 출금합니다. 계좌번호와, 계좌 비밀번호를 입력 후 본인 계좌가 아닐 시에 거부합니다. 금액이 자신의 신용점수보다 큰 값이라면, 담당 관리자의 비밀번호 입력이 필요합니다.
                                            System.out.println("Withdraw");
                                            System.out.print("Choose The Account: ");
                                            inputStr1 = sc.next();
                                            System.out.print("Insert The Password: ");
                                            inputStr2 = sc.next();
                                            // 입력한 계좌번호가 고객의 계좌인지 확인하기 위해 count문을 통해 고객의 계좌 중 해당 계좌번호를 가진 tuple이 있는지 확인합니다. cnt 값이 0이면 해당 계좌번호를 가진 고객의 계좌가 존재하지 않습니다.
                                            sql = String.format("select count(*) as cnt from account join customer on Acssn = Cssn where Accountnumber = '%s' AND Cssn = '%s'", inputStr1, inputStr);
                                            rs = stmt.executeQuery(sql);
                                            rs.next();
                                            if(rs.getInt("cnt") == 0)
                                            {
                                                System.out.println("Not Your Account!");
                                                continue Customer;
                                            }
                                            // 비밀번호가 맞는지 확인하기 위해 해당 계좌의 비밀번호 정보를 가져옵니다.
                                            sql = String.format("select Password from account where Accountnumber = '%s' ", inputStr1);
                                            rs = stmt.executeQuery(sql);
                                            rs.next();
                                            if(! inputStr2.equals(rs.getString("Password")))
                                            {
                                                System.out.println("Wrong Password!");
                                                continue Customer;
                                            }
                                            System.out.print("How Much Money Would You Withdraw?: ");
                                            input = sc.nextInt();
                                            // 출금은 신용점수의 값보다 많이 출금할 수 없습니다. 그 이상을 출금하기 위해서는 담당 관리자의 비밀번호가 필요합니다. 해당 조건 확인을 위해 Customer, Manager 테이블을 equijoin해 신용점수와 매니저의 비밀번호를 가져옵니다.
                                            sql = String.format("select Credit, Password from customer join manager on customer.Cmssn = manager.Mssn where Cssn = '%s' ", inputStr);
                                            rs = stmt.executeQuery(sql);
                                            rs.next();
                                            if(input > rs.getInt("Credit"))
                                            {
                                                System.out.println("The Amount Is Above Your Limit.");
                                                System.out.print("Insert Your Manager's Password: ");
                                                inputStr2 = sc.next();
                                                if(! inputStr2.equals(rs.getString("Password")))
                                                {
                                                    System.out.println("Wrong Password!");
                                                    continue Customer;
                                                }
                                            }
                                            // 고객의 계좌 잔고에 출금한 금액을 업데이트합니다.
                                            sql = String.format("update Account set Value = Value -'%s' where Accountnumber = '%s'", input, inputStr1);
                                            stmt.executeUpdate(sql);
                                            // History 테이블에 입금 기록 tuple을 추가합니다.
                                            input = -(input);
                                            sql = String.format("insert into history values ('%s', '%s', '%s') ", inputStr1, hIdx++, input);
                                            stmt.executeUpdate(sql);
                                            System.out.println("Withdrawing Is Done");
                                            continue Customer;
                                        default:
                                            System.out.println("Wrong Command!");
                                            continue Customer;
                                    }
                                }
                            }
                        }
                    case 3:
                        // 이자, 신용 정보 업데이트 등의 작업을 수행합니다.
                        System.out.println();
                        System.out.println("You Chose 'One Month Later...'");
                        System.out.println("Interests Calculation...");
                        // 이자 계산에는 해당 계좌의 잔고와 은행상품의 이자 정보가 필요합니다. 이자 계산을 위해 Account, FinancialInstruments 테이블을 equijoin해 정보를 가져옵니다.
                        sql = String.format("select * from account join financialinstruments on Ainstrumentscode = Instrumentscode");
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                            tmp = rs.getInt("Value");
                            tmpdbl = rs.getDouble("Interest");
                            tmp2 = (int)Math.round(tmp*(1+tmpdbl));
                            // 계좌 잔고를 업데이트합니다.
                            sql = String.format("update account set Value = '%s' where Accountnumber = '%s'", tmp2, rs.getString("Accountnumber") );
                            stmt1.executeUpdate(sql);
                            // 이자 반영 기록을 남깁니다. History 테이블에 필요한 value들을 통해 tuple을 추가합니다.
                            sql = String.format("insert into history values ('%s', '%s', '%s') ", rs.getString("Accountnumber"), hIdx, (tmp2-tmp));
                            hIdx++;
                            stmt1.executeUpdate(sql);
                        }
                        System.out.println("Removing Fully Paid Loan Accounts");
                        // 잔고가 0 이상이 된 계좌들을 삭제하려고 합니다. Account, FinancialInstruments 테이블을 equijoin해 계좌와 계좌 Category(자유입출금/대출) 정보를 가져옵니다.
                        sql = String.format("select * from account join financialinstruments on Ainstrumentscode = Instrumentscode");
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                            if(rs.getInt("Category") == 0)
                                continue;
                            if(rs.getInt("Value") >= 0)
                            {
                                // 계좌 Category가 대출이고 잔고가 0 이상이면 계좌를 삭제합니다.
                                sql = String.format("delete from account where Accountnumber = '%s'", rs.getString("Accountnumber"));
                                stmt1.executeUpdate(sql);
                                // 삭제한 계좌의 계좌기록들을 History 테이블에서 제거합니다.
                                sql = String.format("delete from history where Haccountnumber = '%s'", rs.getString("Accountnumber"));
                                stmt1.executeUpdate(sql);
                                System.out.printf("Update: Account With Accountnumber %s Has Been Removed%n", rs.getString("Accountnumber"));
                            }
                        }
                        System.out.println("Recalculating The Credit");
                        // 신용점수를 업데이트하려고 합니다. Customer, Account, FinancialInstruments 테이블을 equijoin하고, 자유입출금계좌(Category: 0)들의 정보를 가져옵니다. group by문을 통해 고객 단위로 정보를 묶습니다. sum문을 통해 잔고의 합 정보를 가져옵니다.
                        sql = String.format("select Cssn, Credit, sum(Value) as totalValue, Credit from customer, account, financialinstruments where Cssn = Acssn AND Ainstrumentscode = Instrumentscode AND Category = '0' group by Cssn");
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                            tmp = rs.getInt("Credit");
                            tmp2 = rs.getInt("totalValue");
                            tmp2 = tmp + tmp2/100;
                            // 신용점수(자유입출금계좌의 잔고 합 / 100)의 변경내용을 업데이트합니다.
                            sql = String.format("update Customer set Credit = '%s' where Cssn = '%s'", tmp2, rs.getString("Cssn") );
                            stmt1.executeUpdate(sql);
                            System.out.printf("Update: %s's Credit Has Changed From %s to %s%n", rs.getString("Cssn"), tmp, tmp2);
                        }
                        System.out.println("Credit Above 900 Customers' Will Be Belonged To Chief Manager");
                        // 신용점수 900 이상의 고객은 담당관리자가 최고관리자로 변경됩니다. 그를 위해 최고 관리자의 Ssn 정보를 가져옵니다.
                        sql = String.format("select Mssn from Manager where Position = '0'");
                        rs = stmt.executeQuery(sql);
                        rs.next();
                        tmpSsn = rs.getString("Mssn");
                        // 신용점수 900 이상이고 담당관리자가 최고관리자가 아닌 고객들의 정보를 <>문을 통해 가져옵니다.
                        sql = String.format("select Cssn, Cname from customer where Credit > 900 AND Cmssn <> '%s'", tmpSsn);
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                            // 신용점수 900 이상이고, 담당관리자가 최고관리자가 아닌 고객들의 관리자를 최고관리자로 변경합니다.
                            sql = String.format("update Customer set Cmssn = '%s' where Cssn = '%s'", tmpSsn, rs.getString("Cssn") );
                            stmt1.executeUpdate(sql);
                            System.out.printf("Update: %s's Manager Is The Chief Manager%n", rs.getString("Cname"));
                        }
                        System.out.println("... Complete");
                        continue Start;
                    default:
                        System.out.println("Wrong Command!");
                        continue;
                }
            }
        } catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
                stmt = null;
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
                conn = null;
            }
        }
    }
}

// 반복되는 cmd창 출력을 함수로 만들었습니다.
class Display{
    void showStart()
    {
        System.out.println();
        System.out.println("Initial Menu");
        System.out.println("0. Exit");
        System.out.println("1. Manager Menu(Log In)");
        System.out.println("2. Customer Menu(Log In");
        System.out.println("3. One Month Later...");
        System.out.print("Input: ");
    }
    void showManagerLogIn()
    {
        System.out.println();
        System.out.println("Manager LogIn");
        System.out.println("0. Go to Initial Menu");
        System.out.println("Insert your Ssn and Password one by one");
    }
    void showChiefManagerMenu()
    {
        System.out.println();
        System.out.println("Chief Manager Menu");
        System.out.println("0. Go to Initial Menu");
        System.out.println("1. Create Manager");
        System.out.println("2. Modify Manager");
        System.out.println("3. Delete Manager");
        System.out.println("4. New Customer Sign Up");
        System.out.println("5. Make Financial Instruments");
        System.out.println("6. Delete Financial Instruments");
        System.out.println("7. List Managers");
        System.out.println("8. List Customers");
        System.out.println("9. List Financial Instruments");
        System.out.println("10. Manage Account History");
    }
    void showManagerMenu(int ssn, int position)
    {
        System.out.println();
        System.out.print("Manager Menu");
        System.out.println(" ( Mssn: " + ssn + " Position: " + position + " )");
        System.out.println("0. Go to Initial Menu");
        System.out.println("1. Manage Account History");
    }
    void showCustomerLogIn()
    {
        System.out.println();
        System.out.println("Customer LogIn");
        System.out.println("0. Go to Initial Menu");
        System.out.println("Insert your Ssn");
    }
    void showCustomerMenu()
    {
        System.out.println();
        System.out.println("Customer Menu");
        System.out.println("0. Go to Initial Menu");
        System.out.println("1. Modify Information");
        System.out.println("2. Create Account");
        System.out.println("3. List My Account");
        System.out.println("4. Save");
        System.out.println("5. Withdraw");
    }
    void showCreateAccountMenu()
    {
        System.out.println();
        System.out.println("Create Account Menu");
        System.out.println("0. Go to Initial Menu");
        System.out.println("1. Cancel");
        System.out.println("2. Cat0: Check");
        System.out.println("3. Cat1: Loan");
    }
    void showCategory(int cat)
    {
        switch(cat)
        {
            case 0:
                System.out.print(" Check ");
                break;
            case 1:
                System.out.print(" Loan ");
                break;
            default:
                // none.
                break;
        }
    }
}