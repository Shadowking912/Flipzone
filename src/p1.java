import java.util.*;

class dateandtime{
    static String[] months={"January","February","March","April","June","July","August","September","October","November","December"};
    private static int findindex(String x){
        for(int i=0;i<months.length;i++){
            if(months[i].equals(x)){
                return i;
            }
        }
        return -1;
    }
    public static int compare_date(String a1,String a2,String a3,String a4){
        String[] s1=a1.split(" ");
        String[] s2=a2.split(" ");
        String[] t1=a3.split(" ")[0].split(":");
        String[] t2=a4.split(" ")[0].split(":");
        int flag=0;
        if(Integer.parseInt(s2[2])>Integer.parseInt(s1[2])){
            flag=1;
        }
        else if(Integer.parseInt(s2[2])==Integer.parseInt(s1[2])){
            if(findindex(s2[1])>findindex(s1[1])){
                flag=1;
            }
            else if(findindex(s2[1])<findindex(s1[1])){
                flag=0;
            }
            else{
                if(Integer.parseInt(s2[0].replace("th",""))<Integer.parseInt(s1[0].replace("th",""))){
                    flag=0;
                }
                else if(Integer.parseInt(s2[0].replace("th",""))==Integer.parseInt(s1[0].replace("th",""))){
                    if(Integer.parseInt(t2[0])>Integer.parseInt(t1[0])){
                        flag=1;
                    }
                    else if(Integer.parseInt(t2[0])==Integer.parseInt(t1[0])){
                        if(Integer.parseInt(t2[1])<Integer.parseInt(t1[1])) {
                            flag=0;
                        }
                        else if(Integer.parseInt(t2[1])<Integer.parseInt(t1[1])){
                            flag=2;
                        }
                        else{
                            flag=1;
                        }
                    }
                    else{
                        flag=0;
                    }
                }
                else{
                    flag=1;
                }
            }
        }
        else{
            flag=0;
        }
        return flag;
    }
}

class Student{
    Comparator<company> custom=new Comparator<company>(){
        @Override
        public int compare(company c1, company c2) {
            if(c1.getctc()<c2.getctc()){
                return 1;
            }
            else if(c1.getctc()>c2.getctc()){
                return -1;
            }
            return 0;
        }
    };

    private String name;
    private int roll_no;
    private double cgpa;
    private String branch;
    private company placed;
    private String status;
    private int reg=0;
    public PriorityQueue<company> offeredcompanies=new PriorityQueue<company>(custom);
    public ArrayList<company> notapplied=new ArrayList<company>();
    public ArrayList<company> appliedcompanies=new ArrayList<company>();
    Scanner scanner=new Scanner(System.in);


    public Student(String Name,int roll,double Cgpa,String Branch){
        this.name=Name;
        this.roll_no=roll;
        this.cgpa=Cgpa;
        this.branch=Branch;
        this.status="not-applied";
    }
    public String getName(){
        return this.name;
    }
    public void placement_register(){
        if(this.status=="blocked"){
            System.out.println("you have been blocked");
        }
        else {
            if (Institute_placement_cell.Student_reg_open == "None") {
                System.out.println("Placment not yet started");
            } else {
                System.out.println("Enter the date and time in format(date month(in words) year, time:");
                String[] s = scanner.nextLine().split(", ");
                if (dateandtime.compare_date(Institute_placement_cell.Student_reg_open.split(", ")[0], s[0], Institute_placement_cell.Student_reg_open.split(", ")[1], s[1]) >= 1 && dateandtime.compare_date(Institute_placement_cell.Student_reg_close.split(", ")[0], s[0], Institute_placement_cell.Student_reg_close.split(", ")[1], s[1]) == 0) {
                    System.out.println("Registered");
                    this.reg=1;
                    Institute_placement_cell.addstudent(this);
                    ArrayList<company> l1=p1.getcompanies();
                    int t=0;
                    for(int i=0;i<l1.size();i++){
                        if(this.cgpa>=l1.get(i).getcgpa()){
                            notapplied.add(l1.get(i));
                        }
                    }
                } else {
                    System.out.println("Registration closed");
                }
            }
        }
    }
    public void company_register(){
        if(this.status=="blocked"){
            System.out.println("you have been blocked");
        }
        else {
            System.out.print("Enter Company name: ");
            String s = scanner.nextLine();
            if (this.cgpa >= p1.getcompanylist().get(s).getcgpa()) {
                if(status!="offered" || (status=="offered" && p1.getcompanylist().get(s).getctc()>=3*this.placed.getctc())) {
                    System.out.println("Successfully Registered for " + p1.getcompanylist().get(s).getrole() + " Role ");
                    this.appliedcompanies.add(Institute_placement_cell.getcompanylist().get(s));
                    Institute_placement_cell.getcompanylist().get(s).getstudent().add(this);
                    this.status = "applied";
                    notapplied.remove(Institute_placement_cell.getcompanylist().get(s));
                }
                else{
                    System.out.println("Ineligible for this company");
                }
            } else {
                System.out.println("Ineligible for this company");
            }
        }
    }
    public void displaycompanies(){
        if(reg==0){
            System.out.println("Havent registered yet");
        }
        else {
            ArrayList<company> l1 = p1.getcompanies();
            int t = 1;
            for (int i = 0; i < l1.size(); i++) {
                if (this.cgpa >= l1.get(i).getcgpa()) {
                    System.out.println(t + ") " + "CompanyName: " + l1.get(i).getName());
                    System.out.println("   Company role offering: " + l1.get(i).getrole());
                    System.out.println("   Company Package: " + l1.get(i).getctc() + " LPA");
                    System.out.println("   Company CGPA criteria: " + l1.get(i).getcgpa());
                    t++;
                    System.out.println();
                }
            }
            if(t==1){
                System.out.println("Unavailable");
            }
        }
    }
    public void getStatus(){
        if(this.offeredcompanies.size()>0){
            System.out.println("Status: You have been offered by "+this.offeredcompanies.peek().getName()+"!! Please accept the offer");
        }
        else if(this.status=="applied"){
            System.out.println("Status: Unoffered");
        }
        else if(this.status=="offered"){
            company c=this.placed;
            System.out.println("Status: Offered");
            System.out.println("CompanyName: "+c.getName());
            System.out.println("Company role offering: "+c.getrole());
            System.out.println("Company Package: "+c.getctc()+" LPA");
            System.out.println("Company CGPA criteria: "+c.getcgpa());
        }
        else if(this.status=="blocked"){
            System.out.println("Status: Blocked");
        }
        else if(this.status=="not-applied"){
            System.out.println("Status: not-applied");
        }
    }
    public void updatecgpa(double gpa){
        if(this.reg==0){
            this.cgpa=gpa;
        }
        else{
            System.out.println("Cannot update cgpa");
        }
    }
    public void acceptoffer(){
        if(offeredcompanies.isEmpty()){
            System.out.println("No offer available");
        }
        else{
            this.status="offered";
            this.placed=offeredcompanies.poll();
            System.out.println("Congratulations "+this.name+ "!!! You have accepted the offer by "+this.placed.getName());
            this.placed.addstudent(this);
            while(offeredcompanies.isEmpty()==false){
                offeredcompanies.poll().removestudent(this);
            }
        }
    }
    public void rejectoffer(){
        System.out.println("You have rejected the offer by "+offeredcompanies.peek().getName());
        offeredcompanies.poll().removestudent(this);
        if(offeredcompanies.isEmpty()){
            this.status="blocked";
        }
    }
    public int getroll(){
        return this.roll_no;
    }
    public double getcgpa() {
        return this.cgpa;
    }
    public String getbranch(){
        return this.branch;
    }
    public company getplaced() {
        return this.placed;
    }
    public String gstatus(){
        return this.status;
    }
}


class Institute_placement_cell{
    public static String Student_reg_open="None";
    public static String Student_reg_close="None";
    public static String Company_reg_open="None";
    public static String Company_reg_close="None";
    public static ArrayList<Student> registeredstudents= new ArrayList<Student>();
    public static ArrayList<company> registeredcompany= new ArrayList<company>();
    private static Map<String,company> companylist=new HashMap<String,company>();
    private static Map<String,Student> studentlist=new HashMap<String,Student>();
    private static ArrayList<Student> offeredstudents=new ArrayList<Student>();
    static Scanner scanner=new Scanner(System.in);

    public static void setStudent_reg_open(String s1) {
        Student_reg_open=s1;
    }
    public static void setStudent_reg_close(String s2) {
        Student_reg_close=s2;
    }
    public static void setCompany_reg_open(String s1) {
        Company_reg_open=s1;
    }
    public static void setCompany_reg_close(String s2) {
        Company_reg_close=s2;
    }
    public static void updatestudentcgpa(Student student, double gpa) {
        student.updatecgpa(gpa);
    }
    public static void getcompanies(){
        System.out.println("Number of registered Companies: "+Institute_placement_cell.registeredcompany.size());
    }
    public static void getstudentdetails() {
        System.out.print("Enter (name roll) of student: ");
        String s=scanner.nextLine();
        String[] y=s.split(" ");
        Student st1=studentlist.get(y[1]);
        ArrayList<company> a1=new ArrayList<company>();
        Iterator<company> iter=st1.appliedcompanies.iterator();
        System.out.println("\nApplied Companies\n");
        if(st1.notapplied.size()==0 && st1.appliedcompanies.size()==0){
            System.out.println("Not eligible for any company");
        }
        else {
            for (int i = 0; i < st1.notapplied.size(); i++) {
                System.out.println(i + 1 + ") " + "CompanyName: " + st1.appliedcompanies.get(i).getName());
                System.out.println("   Company role offering: " + st1.appliedcompanies.get(i).getrole());
                System.out.println("   Company Package: " + st1.appliedcompanies.get(i).getctc() + " LPA");
                System.out.println("   Company CGPA criteria: " + st1.appliedcompanies.get(i).getcgpa());
                if (st1.offeredcompanies.contains(st1.appliedcompanies.get(i))) {
                    System.out.println("Offered: YES");
                } else {
                    System.out.println("Offered: NO");
                }
            }
            System.out.println("\nNot Applied Companies\n");
            for (int i = 0; i < st1.notapplied.size(); i++) {
                System.out.println(i + 1 + ") " + "CompanyName: " + st1.notapplied.get(i).getName());
                System.out.println("   Company role offering: " + st1.notapplied.get(i).getrole());
                System.out.println("   Company Package: " + st1.notapplied.get(i).getctc() + " LPA");
                System.out.println("   Company CGPA criteria: " + st1.notapplied.get(i).getcgpa());
            }
        }
    }
    public static void getcompanydetails() {
        System.out.print("Enter name of company: ");
        String s=scanner.nextLine();
        if(Institute_placement_cell.companylist.containsKey(s)){
            System.out.println("CompanyName: "+companylist.get(s).getName());
            System.out.println("Company role offering: "+companylist.get(s).getrole());
            System.out.println("Company Package: "+companylist.get(s).getctc()+" LPA");
            System.out.println("Company CGPA criteria: "+companylist.get(s).getcgpa());
        }
        else{
            System.out.println("Company has not registered");
        }

    }
    public static void getaverage() {
        double r=0;
        for(int i=0;i<Institute_placement_cell.offeredstudents.size();i++){
            r=r+Institute_placement_cell.offeredstudents.get(i).getplaced().getctc();
        }
        System.out.println("Average Package: "+r/Institute_placement_cell.offeredstudents.size());
    }
    public static void getprocessresult() {
        System.out.print("Enter name of company: ");
        String s=scanner.nextLine();
        company c=companylist.get(s);
        Random r=new Random();
        ArrayList<Student> temp=new ArrayList<Student>();
        for(int i=0;i<c.getstudent().size();i++){
            temp.add(c.getstudent().get(i));
        }
        if(temp.size()==1){
            Student s1=temp.get(0);
            c.offerstudent(s1);
            s1.offeredcompanies.add(c);
            System.out.println(1+") "+"Name: "+s1.getName());
            System.out.println("   RollNo: "+s1.getroll());
            System.out.println("   CGPA: "+s1.getcgpa());
            System.out.println("   Branch: "+s1.getbranch());
            Institute_placement_cell.offeredstudents.add(s1);
            temp.remove(0);
        }
        else if(temp.size()==0){
            System.out.println("No students applied");
        }
        else {
            int y = r.nextInt(1, temp.size());
            System.out.println("Following are the selected students:");
            for (int i = 0; i < y; i++) {
                int x = r.nextInt(temp.size());
                Student s1 = temp.get(x);
                c.offerstudent(s1);
                s1.offeredcompanies.add(c);
                System.out.println(i + 1 + ") " + "Name: " + s1.getName());
                System.out.println("   RollNo: " + s1.getroll());
                System.out.println("   CGPA: " + s1.getcgpa());
                System.out.println("   Branch: " + s1.getbranch());
                Institute_placement_cell.offeredstudents.add(s1);
                temp.remove(x);
            }
        }

    }
    public static void addstudent(Student student) {
        Institute_placement_cell.registeredstudents.add(student);
        Institute_placement_cell.studentlist.put(Integer.toString(student.getroll()),student);
    }

    public static Map<String, company> getcompanylist() {
        return companylist;
    }

    public static void getdetails() {
        System.out.println("The number of offered students: "+offeredstudents.size());
        for(int i=0;i<offeredstudents.size();i++){
            System.out.println(i + 1 + ") " + "Name: " + offeredstudents.get(i).getName());
            System.out.println("   RollNo: " + offeredstudents.get(i).getroll());
            System.out.println("   CGPA: " + offeredstudents.get(i).getcgpa());
            System.out.println("   Branch: " + offeredstudents.get(i).getbranch());
            System.out.println("   Company: " + offeredstudents.get(i).getplaced().getName());
            System.out.println("   Role: " + offeredstudents.get(i).getplaced().getrole());
            System.out.println("   Salary: " + offeredstudents.get(i).getplaced().getctc());
        }
        int u=0,b=0;
        for(int i=0;i<registeredstudents.size();i++){
            if(registeredstudents.get(i).gstatus()=="Unoffered"){
                u++;
            }
            else if(registeredstudents.get(i).gstatus()=="blocked"){
                b++;
            }
        }
        System.out.println("The number of Unoffered students: "+u);
        System.out.println("The number of Unoffered students: "+b);
    }
}


class company{
    private ArrayList<Student> studentslist=new ArrayList<Student>();
    private ArrayList<Student> studentsoffered=new ArrayList<Student>();
    private String name;
    private String role;
    private double ctc;
    private double min_cgpa;
    private int reg;
    Scanner scanner=new Scanner(System.in);


    public company(String c_name,String c_role,double c_ctc,double c_min_cgpa){
        this.name=c_name;
        this.role=c_role;
        this.ctc=c_ctc;
        this.min_cgpa=c_min_cgpa;

    }
    public void update_role(){
        if(this.reg==1){
            System.out.println("Cannot update after registration");
        }
        else{
            System.out.print("Enter new Role: ");
            String s = scanner.nextLine();
            this.role=s;
        }

    }
    public void update_package(){
        if(this.reg==1){
            System.out.println("Cannot update after registration");
        }
        else {
            System.out.print("Enter new package: ");
            double y = scanner.nextDouble();
            scanner.nextLine();
            this.ctc = y;
        }
    }
    public void update_cgpa(){
        if(this.reg==1){
            System.out.println("Cannot update after registration");
        }
        else {
            System.out.print("Enter new cgpa criteria: ");
            double y = scanner.nextDouble();
            scanner.nextLine();
            this.min_cgpa = y;
        }
    }
    public String getName() {
        return this.name;
    }
    public double getcgpa() {
        return this.min_cgpa;
    }
    public void institute_drive_register(){
        if(Institute_placement_cell.Company_reg_open=="None"){
            System.out.println("Registration not started");
        }
        else {
            System.out.println("Enter the date and time in format(date month(in words) year, time:");
            String[] s = scanner.nextLine().split(", ");
            if (dateandtime.compare_date(Institute_placement_cell.Company_reg_open.split(", ")[0], s[0], Institute_placement_cell.Company_reg_open.split(", ")[1], s[1]) >= 1 && dateandtime.compare_date(Institute_placement_cell.Company_reg_close.split(", ")[0], s[0], Institute_placement_cell.Company_reg_close.split(", ")[1], s[1]) == 0) {
                Institute_placement_cell.getcompanylist().put(this.name,this);
                Institute_placement_cell.registeredcompany.add(this);
                System.out.println("Registered");
            } else {
                System.out.println("Registration closed");
            }
        }
    }
    public double getctc() {
        return ctc;
    }
    public String getrole() {
        return role;
    }
    public void addstudent(Student s1){
        studentslist.add(s1);
    }
    public void removestudent(Student s1){
        studentsoffered.remove(s1);
    }
    public ArrayList<Student> getstudent(){
        return studentslist;
    }
    public void offerstudent(Student student) {
        this.studentsoffered.add(student);
    }
}


public class p1 {
    private static Scanner scanner=new Scanner(System.in);
    private static Map<String,Student> studentslist=new HashMap<String,Student>();
    private static Map<String,company> companylist=new HashMap<String,company>();
    private static ArrayList<company> companies=new ArrayList<>();
    public static void main(String[] args){
        while(true) {
            System.out.println("Welcome to FutureBuilder");
            System.out.println("1) Enter the Application");
            System.out.println("2) Exit the Application");
            System.out.println("");
            int x = scanner.nextInt();
            if (x == 1) {
                while(true) {
                    System.out.println("Choose the mode you want to enter in:");
                    System.out.println("1) Enter as Student Mode");
                    System.out.println("2) Enter as Company Mode");
                    System.out.println("3) Enter as Placement Cell Mode");
                    System.out.println("4) Return to main Application");
                    x = scanner.nextInt();
                    scanner.nextLine();
                    if (x == 1) {
                        student_mode();
                    } else if (x == 2) {
                        company_mode();
                    } else if (x == 3) {
                        placement_cell_mode();
                    } else if (x == 4) {
                        break;
                    }
                    else{
                        System.out.println("Invalid option");
                    }
                }
            }
            else if (x == 2) {
                System.exit(0);
            }
            else {
                System.out.println("Invalid option");
            }
        }
    }
    public static void student_mode(){
        int x;
        while(true) {
            System.out.println("Choose the Student Query");
            System.out.println("1) Enter as a Student");
            System.out.println("2) Add students");
            System.out.println("3) Back");
            x = scanner.nextInt();
            scanner.nextLine();
            if (x == 1) {
                System.out.println("name and rollno(format: name roll): ");
                String s = scanner.nextLine();
                String[] t = s.split(" ");
                Student student1 = studentslist.get(t[1]);
                System.out.println();
                while (true) {
                    System.out.println("Welcome " + student1.getName());
                    System.out.println("1) Register for Placement Drive");
                    System.out.println("2) Register for Company");
                    System.out.println("3) Get all available companies");
                    System.out.println("4) Get current Status");
                    System.out.println("5) Update CGPA");
                    System.out.println("6) Accept offer");
                    System.out.println("7) Reject offer");
                    System.out.println("8) Back");
                    System.out.println();
                    x = scanner.nextInt();
                    if (x == 1) {
                        student1.placement_register();
                    } else if (x == 2) {
                        student1.company_register();
                    } else if (x == 3) {
                        student1.displaycompanies();
                    } else if (x == 4) {
                        student1.getStatus();
                    } else if (x == 5) {
                        System.out.println("Enter new cgpa: ");
                        double y=scanner.nextDouble();
                        Institute_placement_cell.updatestudentcgpa(student1,y);
                    } else if (x == 6) {
                        student1.acceptoffer();
                    } else if (x == 7) {
                        student1.rejectoffer();
                    } else if (x == 8) {
                        return;
                    } else {
                        System.out.println("Invalid choice\n");
                    }
                }
            } else if (x == 2) {
                System.out.print("Number of students to add: ");
                x = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Please add students in format:\nName\nrollno\nCGPA(floating point)\nBranch\n\n");
                while (x > 0) {
                    String name = scanner.nextLine();
                    int roll = scanner.nextInt();
                    double cgpa = scanner.nextDouble();
                    scanner.nextLine();
                    String branch = scanner.nextLine();
                    Student student = new Student(name, roll, cgpa, branch);
                    studentslist.put(roll + "", student);
                    System.out.println();
                    x--;
                }
            } else if (x == 3) {
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
    public static void company_mode() {
        int x;
        double y;
        while(true) {
            System.out.println("Choose the Company Query");
            System.out.println("1) Add Company and Details");
            System.out.println("2) Choose company");
            System.out.println("3) Get available companies");
            System.out.println("4) Back");
            x = scanner.nextInt();
            scanner.nextLine();
            if (x == 1) {
                String name = scanner.nextLine();
                String role = scanner.nextLine();
                double ctc = scanner.nextDouble();
                double min_cgpa = scanner.nextDouble();
                scanner.nextLine();
                company comp = new company(name, role, ctc, min_cgpa);
                companylist.put(name, comp);
                companies.add(comp);
            } else if (x == 2) {
                System.out.println("Choose to enter into mode of available companies: ");
                System.out.println();
                for (int i = 0; i < companies.size(); i++) {
                    System.out.println(i+1 +") " +companies.get(i).getName());
                }
                System.out.print("\nEnter choice: ");
                x = scanner.nextInt();
                company c = companies.get(x - 1);
                while (true) {
                    System.out.println("\nWelcome " + c.getName());
                    System.out.println("1) Update role");
                    System.out.println("2) update package");
                    System.out.println("3) update cgpa criteria");
                    System.out.println("4) register to institute drive");
                    System.out.println("5) Back");
                    x = scanner.nextInt();
                    scanner.nextLine();
                    if (x == 1) {
                        c.update_role();
                    } else if (x == 2) {
                        c.update_package();
                    } else if (x == 3) {
                        c.update_cgpa();
                    } else if (x == 4) {
                        c.institute_drive_register();
                    } else if (x == 5) {
                        break;
                    } else {
                        System.out.println("Invalid choice\n");
                    }
                }
            } else if (x == 3) {
                for (int i = 0; i < companies.size(); i++) {
                    System.out.println(i + 1 + ") " + "CompanyName: " + companies.get(i).getName());
                    System.out.println("   Company role offering: " + companies.get(i).getrole());
                    System.out.println("   Company Package: " + companies.get(i).getctc() + " LPA");
                    System.out.println("   Company CGPA criteria: " + companies.get(i).getcgpa());
                    System.out.println();
                }
            } else if (x == 4) {
                break;
            } else {
                System.out.println("Invalid Choice");
            }
        }

    }
    public static void placement_cell_mode(){
        int x;
        String s;
        double y;
        while(true) {
            System.out.println("\nWelcome IIITD Placement Cell: ");
            System.out.println("1) Open Student Registrations");
            System.out.println("2) Open Company Registrations");
            System.out.println("3) Get Number of Student Registrations");
            System.out.println("4) Get Number of Company Registrations");
            System.out.println("5) Get Number of Offered/Unoffered/Blocked Students");
            System.out.println("6) Get Student Details");
            System.out.println("7) Get Company Details");
            System.out.println("8) Get Average Package");
            System.out.println("9) Get Company Process Results");
            System.out.println("10) Back");
            x = scanner.nextInt();
            scanner.nextLine();
            if(x==1){
                if(Institute_placement_cell.Company_reg_close!="None") {
                    System.out.println("Fill in the details:-\n" +
                            "1) Set the Opening time for Student registrations\n" +
                            "2) Set the Closing time for Student registrations\n");
                    String s1 = scanner.nextLine();
                    String s2 = scanner.nextLine();
                    if (dateandtime.compare_date(Institute_placement_cell.Company_reg_close.split(", ")[0], s1.split(", ")[0], Institute_placement_cell.Company_reg_close.split(", ")[1], s1.split(", ")[1]) == 1) {
                        Institute_placement_cell.setStudent_reg_open(s1);
                        Institute_placement_cell.setStudent_reg_close(s2);
                    } else {
                        System.out.println("Student registration must be after Company registrations are over");
                    }
                }
                else {
                    System.out.println("Student registration must be after Company registrations are over");
                }
            }
            else if(x==2){
                System.out.println("Fill in the details:-\n" +
                        "1) Set the Opening time for company registrations\n" +
                        "2) Set the Closing time for company registrations\n");
                String s1=scanner.nextLine();
                String s2=scanner.nextLine();
                Institute_placement_cell.setCompany_reg_open(s1);
                Institute_placement_cell.setCompany_reg_close(s2);
            }
            else if(x==3){
                System.out.println("Number of registered Students: "+Institute_placement_cell.registeredstudents.size());
            }
            else if(x==4){
                Institute_placement_cell.getcompanies();
            }
            else if(x==5){
                Institute_placement_cell.getdetails();
            }
            else if(x==6){
                Institute_placement_cell.getstudentdetails();


            }
            else if(x==7){
                Institute_placement_cell.getcompanydetails();
            }
            else if(x==8){
                Institute_placement_cell.getaverage();
            }
            else if(x==9){
                Institute_placement_cell.getprocessresult();
            }
            else if(x==10){
                break;
            }
            else{
                System.out.println("Invalid choice\n");
            }
        }
    }
    public static ArrayList<company> getcompanies(){
        return companies;
    }
    public static Map<String,company> getcompanylist(){
        return companylist;
    }
}
