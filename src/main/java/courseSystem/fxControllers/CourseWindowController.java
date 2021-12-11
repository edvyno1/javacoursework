package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Course;
import courseSystem.ds.File;
import courseSystem.ds.Folder;
import courseSystem.hibernateControllers.CourseHibernate;
import courseSystem.hibernateControllers.FileHibernate;
import courseSystem.hibernateControllers.FolderHibernate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CourseWindowController implements Initializable {

    @FXML
    public ListView myCourses;
    @FXML
    public TreeView courseFolderTree;
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);
    FolderHibernate folderHibernate = new FolderHibernate(entityManagerFactory);
    FileHibernate fileHibernate = new FileHibernate(entityManagerFactory);

    private String login;

    public void setUser(String login) {
        this.login = login;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myCourses.getItems().clear();

        List<Course> courses = courseHibernate.getAllCourses(true, -1, -1);
        courses.forEach(c -> myCourses.getItems().add(c.getId() + ":" + c.getDescription()));
    }

    public void populateFolders() {

        String courseId = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Course selectedCourse = courseHibernate.getCourseById(Integer.parseInt(courseId));

        courseFolderTree.setRoot(new TreeItem<String> ("Course folders:"));

        courseFolderTree.setEditable(true);
        courseFolderTree.getRoot().setExpanded(true);
        courseFolderTree.setCellFactory(TextFieldTreeCell.forTreeView());
        courseFolderTree.setOnEditCommit(new EventHandler<TreeView.EditEvent<String>>() {
            @Override
            public void handle(TreeView.EditEvent<String> event) {
                EntityManager em;
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

//        if(folder.getFolderFiles() != null) {
//            for (File file : folder.getFolderFiles()) {
//                addTreeItem(file, treeItem);
//            }
//        }
    }
    private void addTreeItem(File file, TreeItem root) {
        TreeItem<String>treeItem = new TreeItem<>(file.getName());
        root.getChildren().add(treeItem);
    }

    public void newCourseForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("NewCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        newCourseForm.setCourseFormData(login);

        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void openUserTable(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("UserTable.fxml"));
        Parent root = fxmlLoader.load();

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
//
//    public void loadAllUsersForm(ActionEvent actionEvent) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("all-users-form.fxml"));
//        Parent root = fxmlLoader.load();
//
//
//        Scene scene = new Scene(root);
//
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setScene(scene);
//        stage.showAndWait();
//    }

    public void editSelected(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("EditCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        String ID = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        int id = Integer.parseInt(ID);
        Course course = courseHibernate.getCourseById(id);
        newCourseForm.enterByEdit(course);

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
}
