import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class ClubFairSetupPlanner implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        File xmlFile = new File(filename);
        // TODO: YOUR CODE HERE
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList projects = doc.getElementsByTagName("Project");
            doc.getDocumentElement().normalize();

            for (int i = 0; i < projects.getLength(); i++) {
                Node projectNode = projects.item(i);
                if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element projectElement = (Element) projectNode;
                    String name = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                    NodeList tasks = projectElement.getElementsByTagName("Task");
                    List<Task> taskList = new ArrayList<>();
                    for (int j = 0; j < tasks.getLength(); j++) {
                            Element taskElement = (Element) tasks.item(j);
                            int taskID = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                            String taskName = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                            int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());

                            NodeList dependencies = taskElement.getElementsByTagName("DependsOnTaskID");
                            List<Integer> dependenciesList = new ArrayList<>();
                            for (int k = 0; k < dependencies.getLength(); k++) {
                                Element dependsOnTask = (Element) dependencies.item(k);
                                dependenciesList.add(Integer.parseInt(dependsOnTask.getTextContent()));
                            }

                            Task task = new Task(taskID, taskName, duration, dependenciesList);
                            taskList.add(task);
                    }
                    projectList.add(new Project(name, taskList));
                }
            }

        }catch (Exception ignored) {
        }

        return projectList;
    }


}
