
import com.beginsecure.socialnetworkjfx.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import map.domain.Friend;
import map.domain.User;
import map.domain.validators.UserValidator;
import map.domain.validators.ValidationException;
import map.domain.validators.Validator;
import map.repository.Repository;
import map.repository.db.FriendRepositoryDB;
import map.repository.db.UserRepositoryDB;
import map.repository.file.FriendRepository;
import map.repository.file.UserRepository;
import map.repository.memory.InMemoryRepository;
import map.service.Service;
import map.service.ServiceInterface;
import map.ui.Ui;

import java.io.IOException;

public class Main  {

    private static Stage stg;

    public void changeScene(String fxml) throws IOException {
        Parent pane= FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:3580/Users";
        String user = "postgres";
        String password = "PGADMINPASSWORD";
        String queryLoad="SELECT id, first_name, last_name, password FROM public.\"User\"";
        String queryLoadF="SELECT id, user_id, friend_id FROM public.\"Friendship\"";

        Repository repository = new UserRepositoryDB(url,user,password, queryLoad);
        Repository repositoryF = new FriendRepositoryDB(url,user,password,queryLoadF);
        Validator validator = new UserValidator();
//        Repository<Long, User> repoFile = new UserRepository("./data/utilizatori.txt");
//        Repository<Long, Friend> friendRepoFile = new FriendRepository("./data/friend.txt");
        ServiceInterface service=new Service(validator, repository, repositoryF);
        Ui ui = new Ui(service);



        ui.start();

    }
}