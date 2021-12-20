package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.*;
import courseSystem.hibernateControllers.CourseHibernate;
import courseSystem.hibernateControllers.FileHibernate;
import courseSystem.hibernateControllers.FolderHibernate;
import courseSystem.hibernateControllers.UserHibernate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseWindowController implements Initializable {

    @FXML
    public ListView myCourses;
    @FXML
    public TreeView courseFolderTree;
    @FXML
    public ContextMenu courseContext;
    @FXML
    public MenuItem addFolder;
    @FXML
    public MenuItem delFolder;
    @FXML
    public Menu courseMenu;
    @FXML
    public Menu userMenu;
    @FXML
    public MenuItem showUsers;
    @FXML
    public MenuItem addModButton;
    @FXML
    public Button enrollButton;
    @FXML
    public Button newCourseButton;
    @FXML
    public ContextMenu courseMenuContext;
    @FXML
    public MenuItem addItem;
    @FXML
    public MenuItem editItem;
    @FXML
    public MenuItem deleteItem;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);
    FolderHibernate folderHibernate = new FolderHibernate(entityManagerFactory);
    FileHibernate fileHibernate = new FileHibernate(entityManagerFactory);



    private User user;

    public void setUser(User user) {
        this.user = userHibernate.getUserById(user.getId());
        courseContext.styleProperty().set("-fx-background-color: white");

        if(this.user.getUserType().equals(UserType.VIEWER)){
            addFolder.setVisible(false);
            delFolder.setVisible(false);
            courseContext.styleProperty().set("-fx-background-color: background");
            userMenu.setVisible(false);
            newCourseButton.setVisible(false);
            courseMenu.setVisible(false);
            addItem.setVisible(false);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
            addModButton.setVisible(false);
            courseMenuContext.styleProperty().set("-fx-background-color: background");
        }
        if(this.user.getUserType().equals(UserType.CREATOR)){
            addFolder.setVisible(false);
            delFolder.setVisible(false);
            courseContext.styleProperty().set("-fx-background-color: background");
            userMenu.setVisible(false);
//            newCourseButton.setVisible(false);
//            courseMenu.setVisible(false);
            addItem.setVisible(false);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
            addModButton.setVisible(false);
//            courseMenuContext.styleProperty().set("-fx-background-color: background");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myCourses.getItems().clear();

        List<Course> courses = courseHibernate.getAllCourses(true, -1, -1);
        courses.forEach(c -> myCourses.getItems().add(c.getId() + ":" + c.getDescription()));
    }

    public void populateFolders() {
        String courseId;
        try{
            courseId = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        }
        catch (Exception e){
            System.out.println("No course selected");
            return;
        }
        Course selectedCourse = courseHibernate.getCourseById(Integer.parseInt(courseId));
        setEnrollButtonText();
        setCourseInformation(selectedCourse);
        if(!this.user.getUserType().equals(UserType.ADMIN)){
            for(Course c : this.user.getMyModeratedCourses()){
                System.out.println(c.getId() + " c ID + selected ID " + selectedCourse.getId());
                if(c.getId() == selectedCourse.getId()){
                    System.out.println("SHOW");
                    addFolder.setVisible(true);
                    delFolder.setVisible(true);
                    courseContext.styleProperty().set("-fx-background-color: white");
                    editItem.setVisible(true);
                    deleteItem.setVisible(true);
                    addModButton.setVisible(true);
                    courseMenuContext.styleProperty().set("-fx-background-color: white");
                    break;
                } else {
                    System.out.println("HIDE");
                    addFolder.setVisible(false);
                    delFolder.setVisible(false);
                    courseContext.styleProperty().set("-fx-background-color: background");
                    editItem.setVisible(false);
                    deleteItem.setVisible(false);
                    addModButton.setVisible(false);
                    courseMenuContext.styleProperty().set("-fx-background-color: background");
                }
            }
        }

        courseFolderTree.setRoot(new TreeItem<String> ("Course folders:"));
        courseFolderTree.setEditable(true);
        courseFolderTree.getRoot().setExpanded(true);
        courseFolderTree.setCellFactory(TextFieldTreeCell.forTreeView());
        courseFolderTree.setOnEditCommit(new EventHandler<TreeView.EditEvent<String>>() {
            @Override
            public void handle(TreeView.EditEvent<String> event) {
                //EntityManager em;
                String selectedItem = event.getOldValue();
                if(selectedItem.contains(".")){
                    File file = fileHibernate.getFileByName(selectedItem);
                    file.setName(event.getNewValue());
                    fileHibernate.updateFile(file);
                }
                else {
                    Folder folder = folderHibernate.getFolderByTitle(event.getOldValue());
                    folder.setTitle(event.getNewValue());
                    folderHibernate.updateFolder(folder);
                }
            }
        });

        for(Folder folders : selectedCourse.getCourseFolders()){
            addTreeItem(folders, courseFolderTree.getRoot());
        }

    }
    private void addTreeItem(Folder folder, TreeItem parentFolder) {
        TreeItem<String> treeItem = new TreeItem<>(folder.getTitle());
        parentFolder.getChildren().add(treeItem);
        if(folder.getSubFolders() != null) {
            for (Folder sub : folder.getSubFolders()) {
                addTreeItem(sub, treeItem);
            }
        }

        if(folder.getFolderFiles() != null) {
            for (File file : folder.getFolderFiles()) {
                addTreeItem(file, treeItem);
            }
        }
    }
    private void addTreeItem(File file, TreeItem root) {
        TreeItem<String>treeItem = new TreeItem<>(file.getName());
        root.getChildren().add(treeItem);
    }

    public void newCourseForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("NewCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        newCourseForm.setUser(user);
        System.out.println("new course form set user" + user);

        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openUserTable(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("UserTable.fxml"));
        Parent root = fxmlLoader.load();
        UserTableController userTableController = fxmlLoader.getController();
        userTableController.setUser(user);
        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void addFolder(ActionEvent event){
        Folder folder;
        TreeItem<String> treeItem = (TreeItem<String>) courseFolderTree.getSelectionModel().getSelectedItem();
        if(treeItem == courseFolderTree.getRoot()){
            String courseId = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
            Course course = courseHibernate.getCourseById(Integer.parseInt(courseId));
            folder = new Folder("New folder", course);
        }
        else{
            String selectedItem = treeItem.getValue();
            System.out.println(selectedItem);
            Folder parentFolder = folderHibernate.getFolderByTitle(selectedItem);
            folder = new Folder("New folder", parentFolder);
        }

        folderHibernate.createFolder(folder);
        addTreeItem(folder, treeItem);
        for(TreeItem<String> string : treeItem.getChildren()){
            if(string.getValue().equals("New folder")){
                treeItem.setExpanded(true);
                courseFolderTree.edit(string);
                break;
            }
        }
    }

    public void removeFolder(String selectedItem){
        Folder folder = folderHibernate.getFolderByTitle(selectedItem);
        folderHibernate.deleteFolder(folder);
    }

    public void fileTreeDelete(ActionEvent event){
        TreeItem<String> treeItem = (TreeItem<String>) courseFolderTree.getSelectionModel().getSelectedItem();
        String selectedItem = treeItem.getValue();
        treeItem.getParent().getChildren().remove(treeItem);
        if(selectedItem.contains(".")){removeFile(selectedItem);}
        else removeFolder(selectedItem);
    }

    @FXML
    public void addFile(ActionEvent actionEvent) throws IOException {
        File file = null;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        java.io.File fileChosen = chooser.showOpenDialog(myCourses.getScene().getWindow());
        String fileName = fileChosen.getName();
        TreeItem<String> treeItem = (TreeItem<String>) courseFolderTree.getSelectionModel().getSelectedItem();
        if(treeItem == courseFolderTree.getRoot()){
            //warning?
        }
        else{
            String selectedItem = treeItem.getValue();
            //System.out.println(selectedItem);
            Folder parentFolder = folderHibernate.getFolderByTitle(selectedItem);
            file = new File(fileName, parentFolder);
        }
        fileHibernate.createFile(file);
        addTreeItem(file, treeItem);
    }
    public void removeFile(String selectedItem){
        File file = fileHibernate.getFileByName(selectedItem);
        fileHibernate.deleteFile(file);
    }

    public void addModToCourse(ActionEvent event) throws IOException {
        int selected = Integer.parseInt(myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0]);
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("ModsTable.fxml"));
        Parent root = fxmlLoader.load();

        ModController tableController = fxmlLoader.getController();
        tableController.setCourse(selected);

        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void editSelected(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("EditCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        String ID = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        int id = Integer.parseInt(ID);
        Course course = courseHibernate.getCourseById(id);
        newCourseForm.enterByEdit(course);
        newCourseForm.setUser(this.user);
        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelected(ActionEvent event) {

        String ID = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        int id = Integer.parseInt(ID);
        Course course = courseHibernate.getCourseById(id);
        id = myCourses.getSelectionModel().getSelectedIndex();
        myCourses.getItems().remove(id);
        courseHibernate.deleteCourse(course);
    }

    public void enrollToCourse() {
        int selected = Integer.parseInt(myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0]);
        Course course = courseHibernate.getCourseById(selected);

        Person person = userHibernate.getPersonById(user.getId());
        List<Person> students = course.getStudents();
        List<Integer> studentIdList = new ArrayList<>();

        for(Person p : students) {
            studentIdList.add(p.getId());
        }
        if(studentIdList.contains(user.getId())){

            List <Course> MyEnrolledCourses = new ArrayList<Course>();

            for(Course c : person.getMyEnrolledCourses()){
                if(c.getId()!=course.getId()){
                    System.out.println(c);
                    MyEnrolledCourses.add(c);
                }
            }
            //System.out.println("studentlist: " + studentIdList.toString());
            //System.out.println(course.getId());
            //enrollButton.setText("Enroll to course");
            //System.out.println("ENROLL");
            person.setMyEnrolledCourses(MyEnrolledCourses);

        } else{
            //System.out.println("studentlist: " + studentIdList.toString());
            //System.out.println(course.getId());
            person.getMyEnrolledCourses().add(course);
            //System.out.println("DELIST");
            //enrollButton.setText("Delist from course");

        }
        userHibernate.editUser(person);
        populateFolders();
    }

    public void setEnrollButtonText() {
        Person person = (Person) userHibernate.getPersonById(user.getId());


        List<Course> myEnrolledCourses = person.getMyEnrolledCourses();
        //System.out.println("START ENROLLEDC: " + myEnrolledCourses);
        List<Integer> enrolledId = new ArrayList<>();
        for(Course enrolled : myEnrolledCourses){
            enrolledId.add(enrolled.getId());
        }

        int selected = Integer.parseInt(myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0]);
        Course course = courseHibernate.getCourseById(selected);
        if(enrolledId.contains(course.getId())){
            //System.out.println("enrolledlist: " + myEnrolledCourses.toString());
            //System.out.println(course.getId());
            //System.out.println("DELIST");
            enrollButton.setText("Delist from course");
        } else{
            //.out.println("enrolledlist: " + myEnrolledCourses.toString());
            //System.out.println(course.getId());
            //System.out.println("ENROLL");
            enrollButton.setText("Enroll to course");


        }
    }

    public void setCourseInformation(Course course){
        setStudentCourseInformation(course);
        setModeratorCourseInformation(course);
    }

    public void setModeratorCourseInformation(Course course) {
        List<User> modList = course.getCourseModerator();
        List<Integer> modIdList = new ArrayList<>();
        for(User mod : modList){
            modIdList.add(mod.getId());
        }
        if(modIdList.contains(user.getId()) || user.getUserType() == UserType.ADMIN) {
            addModButton.setVisible(true);
            courseFolderTree.setVisible(true);
//            addFolder.setVisible(false);
//            deleteFolder.setVisible(false);
//            addFile.setVisible(false);
        } else{
            addModButton.setVisible(false);
        }
    }

    public void setStudentCourseInformation(Course course) {
        List<Person> studentList = course.getStudents();
        List<Integer> studentIdList = new ArrayList<>();
        for(Person student : studentList){
            studentIdList.add(student.getId());
        }
        System.out.println(studentIdList);
        if(studentIdList.contains(user.getId())){
            System.out.println("visible");
            courseFolderTree.setVisible(true);
//            addFolder.setVisible(false);
//            deleteFolder.setVisible(false);
//            addFile.setVisible(false);
        } else{
            System.out.println("invisible");
            courseFolderTree.setVisible(false);
        }

    }

}
