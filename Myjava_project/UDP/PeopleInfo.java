import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PeopleInfo {
	private static String File_Path = "/home/kangzhaoxiang/文档/互联网开发技术课程资料/2019/Final Exam/Myjava_project/UDP/name.txt";
	private ArrayList<People> peoples = new ArrayList<People>();

	public PeopleInfo() {
		loadPeople();
	}

	public String toString() {
		String details = "INFO&\n";
		for (int i = 0; i < peoples.size(); i++) {
			details += String.valueOf(peoples.get(i).getId()) + " " + peoples.get(i).getName()+"\n";
		}
		System.out.println("details: \n" + details);
		return details;
	}

	public String findNameById(int id) {
		for (int i = 0; i < peoples.size(); i++) {
			if (peoples.get(i).getId() == id) {
				return "NAME&&"+peoples.get(i).getName();
			}
		}
		return "NAME&&No match Name";
	}

	public Boolean addPeople(String line) {
		try {
			StringTokenizer st = new StringTokenizer(line, " ");
			int id = Integer.parseInt(st.nextToken().trim());
			String name = st.nextToken().trim();
			if(!addPeople(id, name)) {
				//System.out.println("Id has existed");
				return false;
			}
			savePeople();
		} catch (Exception e) {
			System.out.println("Problem parsing new people:\n" + e);
		}
		return true;
	}

	public boolean addPeople(int id, String name) {

		for (int i = 0; i < peoples.size(); i++) {
			if (peoples.get(i).getId() == id) {
				return false;
			}
		}
		People people = new People(id, name);
		peoples.add(people);
		return true;
	}

	public void loadPeople() {
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(File_Path));
			while ((line = in.readLine()) != null) {
				addPeople(line);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void savePeople() {
		String line;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(File_Path)), true);
			for (int i = 0; i < peoples.size(); i++) {
				line = String.valueOf(peoples.get(i).getId()) + " " + peoples.get(i).getName();
				out.println(line);
			}
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	class People {
		private int id;
		private String name;

		public People(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
	

}
